package in.ac.dei.edrp.admissionsystem.QrAttendence;

import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.*;

public class QrAttendanceDaoImpl extends SqlMapClientDaoSupport
        implements QrAttendanceDao {

    /* ================= LOGIN PROCESS ================= */

    @Override
    public Map<String,Object> processLogin(QrAttendanceBean input){

        Map<String,Object> result = new HashMap<>();

        try{

            if(input==null || input.getUserName()==null || input.getPassword()==null){

                result.put("status","ERROR");
                result.put("message","Username or password missing");
                return result;
            }

            int count = checkLogin(input);

            if(count==1){

                result.put("status","OK");
                result.put("message","Login successful");

            }else{

                result.put("status","INVALID");
                result.put("message","Invalid username or password");
            }

        }catch(Exception e){

            result.put("status","ERROR");
            result.put("message","Login processing failed");
        }

        return result;
    }


    /* ================= FETCH STUDENT ================= */

    @Override
    public Map<String,Object> processFetchStudent(QrAttendanceBean input){

        Map<String,Object> result = new HashMap<>();

        try{

            if(input==null || input.getApplication_number()==null){

                result.put("status","INVALID_QR");
                result.put("message","QR data missing");
                return result;
            }

            String[] parts =
                    input.getApplication_number().trim().split("\\|");

            if(parts.length < 4){

                result.put("status","INVALID_QR");
                result.put("message","QR format invalid");
                return result;
            }

            String qrAppNo = parts[0].trim();
            String qrName = parts[1].trim();
            String qrDob = parts[2].trim();
            String qrProgram = parts[3].trim();

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(qrAppNo);
            param.setProgram_id(qrProgram);

            QrAttendanceBean dbStudent =
                    (QrAttendanceBean)getSqlMapClientTemplate()
                    .queryForObject("QrAttendance.getStudentByQr",param);

            if(dbStudent==null){

                result.put("status","INVALID_QR");
                result.put("message","Student not found");
                return result;
            }

            boolean nameMatch =
                    qrName.equalsIgnoreCase(dbStudent.getStudent_name().trim());

            boolean dobMatch =
                    normalizeDob(qrDob).equals(
                            normalizeDob(dbStudent.getDob()));

            if(!nameMatch || !dobMatch){

                result.put("status","INVALID_QR");
                result.put("message","QR data mismatch");
                return result;
            }

            /* ---------- Authority Check ---------- */

            QrAttendanceBean authBean = new QrAttendanceBean();
            authBean.setUserName(input.getUserName());
            authBean.setProgram_id(dbStudent.getProgram_id());
            authBean.setAttendance_type(input.getAttendance_type());

            Integer authority = checkAttendanceAuthority(authBean);

            if(authority==0){

                result.put("status","NO_AUTHORITY");
                result.put("message","User not authorized");
                return result;
            }

            /* ---------- Admission Config Check ---------- */

            QrAttendanceBean configBean = new QrAttendanceBean();
            configBean.setProgram_id(dbStudent.getProgram_id());
            configBean.setAttendance_type(input.getAttendance_type());

            Integer config = checkAdmissionConfig(configBean);

            if(config==0){

                result.put("status","CONFIG_NOT_READY");
                result.put("message","Admission Setup is not Ready.");
                return result;
            }

            /* ---------- Duplicate Attendance ---------- */

            QrAttendanceBean dupBean = new QrAttendanceBean();
            dupBean.setApplication_number(dbStudent.getApplication_number());
            dupBean.setProgram_id(dbStudent.getProgram_id());
            dupBean.setAttendance_type(input.getAttendance_type());

            Integer duplicate = checkAttendance(dupBean);

            if(duplicate>0){

                result.put("status","ATTENDANCE_EXISTS");
                result.put("message","Attendance already marked");
                return result;
            }

            result.put("status","OK");
            result.put("student",dbStudent);

        }catch(Exception e){

            result.put("status","ERROR");
            result.put("message","Fetch student failed");
        }

        return result;
    }


    /* ================= MARK ATTENDANCE ================= */

    @Override
    public Map<String,Object> processMarkAttendance(QrAttendanceBean input){

        Map<String,Object> result = new HashMap<>();

        try{

            int saveResult = saveAttendance(input);

            if(saveResult==1){

                result.put("status","SUCCESS");
                result.put("message","Attendance saved");

            }else if(saveResult==0){

                result.put("status","DUPLICATE");
                result.put("message","Attendance already marked");

            }else{

                result.put("status","FAILED");
                result.put("message","Attendance not saved");
            }

        }catch(Exception e){

            result.put("status","ERROR");
            result.put("message","Attendance save failed");
        }

        return result;
    }


    /* ================= LOGIN CHECK ================= */

    @Override
    public int checkLogin(QrAttendanceBean input){

        Integer count = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkLogin",input);

        return count==null ? 0 : count;
    }


    /* ================= DUPLICATE CHECK ================= */

    @Override
    public Integer checkAttendance(QrAttendanceBean input){

        Integer count = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate",input);

        return count==null ? 0 : count;
    }


    /* ================= AUTHORITY CHECK ================= */

    @Override
    public Integer checkAttendanceAuthority(QrAttendanceBean input){

        Integer count = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceAuthority",input);

        return count==null ? 0 : count;
    }


    /* ================= ADMISSION CONFIG ================= */

    @Override
    public Integer checkAdmissionConfig(QrAttendanceBean input){

        Integer count = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAdmissionConfig",input);

        return count==null ? 0 : count;
    }


    /* ================= SAVE ATTENDANCE ================= */

    @Override
    public int saveAttendance(QrAttendanceBean input){

        if(input==null) return -1;

        Integer exists = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate",input);

        if(exists!=null && exists>0){
            return 0;
        }

        getSqlMapClientTemplate()
                .update("QrAttendance.insertAttendance",input);

        return 1;
    }


    /* ================= EXCEL REPORT ================= */

    @Override
    public void generateExcelReport(String userName,
                                    HttpServletResponse response) {

        XSSFWorkbook workbook = null;
        OutputStream out = null;

        try {

            List<QrAttendanceBean> list =
                    getSqlMapClientTemplate()
                    .queryForList("QrAttendance.getExcelReport", userName);

            if (list == null) {
                list = new ArrayList<>();
            }

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Attendance Report");

            /* HEADER STYLE */

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] columns = {
                    "Attendance Type",
                    "Program Name",
                    "Application Number",
                    "Status",
                    "Attendance Time"
            };

            int rowNum = 0;

            Row header = sheet.createRow(rowNum++);

            for (int i = 0; i < columns.length; i++) {

                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            /* DATA ROWS */

            for (QrAttendanceBean bean : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        bean.getAttendance_type() == null ? "" : bean.getAttendance_type());

                row.createCell(1).setCellValue(
                        bean.getProgram_name() == null ? "" : bean.getProgram_name());

                row.createCell(2).setCellValue(
                        bean.getApplication_number() == null ? "" : bean.getApplication_number());

                row.createCell(3).setCellValue(
                        bean.getStatus() == null ? "" : bean.getStatus());

                row.createCell(4).setCellValue(
                        bean.getAttendance_time() == null ? "" : bean.getAttendance_time());
            }

            /* AUTO SIZE */

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            /* RESPONSE */

            response.reset();
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=AttendanceReport.xlsx");

            response.setHeader("Cache-Control",
                    "no-cache, no-store, must-revalidate");

            response.setHeader("Pragma", "no-cache");

            response.setDateHeader("Expires", 0);

            out = response.getOutputStream();

            workbook.write(out);

            out.flush();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try { if (workbook != null) workbook.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
        }
    }
    /* ================= IMAGE FILE PATH ================= */

    @Override
    public File getApplicantImageFile(String appNo,
                                      String programId,
                                      String type) {

        try {

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(appNo);
            param.setProgram_id(programId);

            QrAttendanceBean dbData =
                    (QrAttendanceBean) getSqlMapClientTemplate()
                            .queryForObject("QrAttendance.getStudentByQr", param);

            if (dbData == null) {
                System.out.println("Student not found in DB");
                return null;
            }

            String baseDir = dbData.getPhoto_path();

            if (baseDir == null || baseDir.trim().isEmpty()) {
                System.out.println("Photo path empty");
                return null;
            }

            baseDir = baseDir.trim();

            System.out.println("Base directory from DB: " + baseDir);

            /* OS detection */

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {

                if (baseDir.startsWith("/home/")) {
                    baseDir = "D:" + baseDir.replace("/", "\\");
                }
            }

            /* remove trailing slash */

            if (baseDir.endsWith("/") || baseDir.endsWith("\\")) {
                baseDir = baseDir.substring(0, baseDir.length() - 1);
            }

            File directory = new File(baseDir);

            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("Directory not found: " + baseDir);
                return null;
            }

            String fileName;

            if ("photo".equalsIgnoreCase(type)) {
                fileName = "photo.jpg";
            } else {
                fileName = "signature.jpg";
            }

            File imageFile = new File(directory, fileName);

            System.out.println("Final image path: " + imageFile.getAbsolutePath());

            if (!imageFile.exists()) {
                System.out.println("Image file missing");
                return null;
            }

            return imageFile;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /* ================= DOB NORMALIZATION ================= */

    private String normalizeDob(String dob){

        if(dob==null) return "";

        return dob.replaceAll("[^0-9]","");
    }
    
    @Override
    public Map<String,Object> processVerifyAttendance(QrAttendanceBean input){

        Map<String,Object> result = new HashMap<>();

        try{

            int save = saveVerifiedAttendance(input);

            if(save==1){

                result.put("status","SUCCESS");
                result.put("message","Attendance verified");

            }else if(save==0){

                result.put("status","DUPLICATE");
                result.put("message","Attendance already marked");

            }else{

                result.put("status","FAILED");
                result.put("message","Attendance not saved");
            }

        }catch(Exception e){

            e.printStackTrace();

            result.put("status","ERROR");
            result.put("message","Verify attendance failed");
        }

        return result;
    }
    
    public int saveVerifiedAttendance(QrAttendanceBean input){

        if(input==null) return -1;

        input.setStatus("VERIFIED");   // IMPORTANT

        Integer exists = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate",input);

        if(exists!=null && exists>0){
            return 0;
        }

        getSqlMapClientTemplate()
                .insert("QrAttendance.insertVerifiedAttendance",input);

        return 1;
    }
    
    
    @Override
    public Map<String,Object> processUnverifyAttendance(QrAttendanceBean input){

        Map<String,Object> result = new HashMap<>();

        try{

            if(input == null){
                result.put("status","ERROR");
                result.put("message","Invalid request");
                return result;
            }

            // Set status explicitly (do not rely on Android)
            input.setStatus("UNVERIFIED");

            // Handle null reason
            if(input.getReason() == null){
                input.setReason("");
            }

            int saveResult = saveUnverifiedAttendance(input);

            if(saveResult == 1){

                result.put("status","SUCCESS");
                result.put("message","Attendance marked as unverified");

            }else if(saveResult == 0){

                result.put("status","DUPLICATE");
                result.put("message","Attendance already exists");

            }else{

                result.put("status","FAILED");
                result.put("message","Attendance not saved");
            }

        }catch(Exception e){

            e.printStackTrace();

            result.put("status","ERROR");
            result.put("message","Unverify attendance failed");
        }

        return result;
    }
    
    public int saveUnverifiedAttendance(QrAttendanceBean input){

        if(input==null) return -1;

        input.setStatus("UNVERIFIED");   // IMPORTANT

        Integer exists = (Integer)getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate",input);

        if(exists!=null && exists>0){
            return 0;
        }

        getSqlMapClientTemplate()
                .insert("QrAttendance.insertUnverifiedAttendance",input);

        return 1;
    }
}