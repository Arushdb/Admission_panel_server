package in.ac.dei.edrp.admissionsystem.QrAttendence;

import com.google.gson.Gson;
import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import javax.servlet.ServletOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;



public class QrAttendanceController extends MultiActionController {

    private QrAttendanceDao qrAttendanceDao;

    public void setQrAttendanceDao(QrAttendanceDao qrAttendanceDao) {
        this.qrAttendanceDao = qrAttendanceDao;
    }

    private final Gson gson = new Gson();

    /* ================= LOGIN ================= */
    public void checkLogin(HttpServletRequest request, HttpServletResponse response) {

        JSONObject obj = new JSONObject();

        try {
            prepareResponse(request, response);

            String jsonBody = getRequestJson(request);
            if (isBlank(jsonBody)) {
                obj.put("status", "ERROR");
                obj.put("message", "Request body missing");
                writeResponse(response, obj);
                return;
            }

            QrAttendanceBean input = gson.fromJson(jsonBody, QrAttendanceBean.class);

            if (input == null || isBlank(input.getUserName()) || isBlank(input.getPassword())) {
                obj.put("status", "ERROR");
                obj.put("message", "Username or password missing");
                writeResponse(response, obj);
                return;
            }

            int count = qrAttendanceDao.checkLogin(input);

            if (count == 1) {
                obj.put("status", "OK");
                obj.put("message", "Login successful");
            } else {
                obj.put("status", "INVALID");
                obj.put("message", "Invalid username or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            obj.put("status", "ERROR");
            obj.put("message", "Login failed");
        }

        writeResponse(response, obj);
    }

    /* ================= FETCH STUDENT ================= */
    public void fetchStudent(HttpServletRequest request, HttpServletResponse response) {
    	
    	 System.out.println("===== fetchStudent() STARTED =====");

        JSONObject obj = new JSONObject();

        try {
            prepareResponse(request, response);

            String jsonBody = getRequestJson(request);
            System.out.println("Received JSON: " + jsonBody);
            
            if (isBlank(jsonBody)) {
            	System.out.println("JSON body is blank");
                obj.put("status", "ERROR");
                obj.put("message", "Request body missing");
                writeResponse(response, obj);
                return;
            }

            QrAttendanceBean input = gson.fromJson(jsonBody, QrAttendanceBean.class);
            
            System.out.println("Parsed userName: " + input.getUserName());
            System.out.println("Parsed QR raw value: " + input.getApplication_number());

            if (input == null || isBlank(input.getApplication_number())) {
            	System.out.println("Application number missing in input");
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR data missing");
                writeResponse(response, obj);
                return;
            }

            // Parse QR
            String[] parts = input.getApplication_number().trim().split("\\|");
            System.out.println("QR parts length: " + parts.length);
            if (parts.length < 4) {
            	System.out.println("QR format invalid");
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR format invalid");
                writeResponse(response, obj);
                return;
            }

            String qrAppNo = parts[0].trim();
            String qrName  = parts[1].trim();
            String qrDob   = parts[2].trim();
            String qrProgram = parts[3].trim();
            
            System.out.println("QR Application No: " + qrAppNo);
            System.out.println("QR Name: " + qrName);
            System.out.println("QR DOB: " + qrDob);
            System.out.println("QR Program ID: " + qrProgram);

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(qrAppNo);
            param.setProgram_id(qrProgram); 
            
            System.out.println("Calling getStudentByQr() for appNo: " + qrAppNo);

            QrAttendanceBean dbData = qrAttendanceDao.getStudentByQr(param);
            
            System.out.println("Returned from getStudentByQr()");

            if (dbData == null) {
            	System.out.println("No student found in DB");
                obj.put("status", "INVALID_QR");
                obj.put("message", "Student not found");
                writeResponse(response, obj);
                return;
            }
            
            System.out.println("DB Student Name: " + dbData.getStudent_name());
            System.out.println("DB DOB: " + dbData.getDob());
            System.out.println("DB Program ID: " + dbData.getProgram_id());

            boolean nameMatch = qrName.equalsIgnoreCase(dbData.getStudent_name().trim());
            boolean dobMatch = normalizeDob(qrDob).equals(normalizeDob(dbData.getDob()));
            
            System.out.println("Name Match: " + nameMatch);
            System.out.println("DOB Match: " + dobMatch);

            if (!nameMatch || !dobMatch) {
            	System.out.println("QR data mismatch with DB");
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR data does not match");
                writeResponse(response, obj);
                return;
            }
            
            System.out.println("Checking authority...");

            // Authority check
            QrAttendanceBean authBean = new QrAttendanceBean();
            authBean.setUserName(input.getUserName());
            authBean.setProgram_id(dbData.getProgram_id());
            authBean.setAttendance_type(input.getAttendance_type());

            Integer authority = qrAttendanceDao.checkAttendanceAuthority(authBean);
            
            System.out.println("Authority result: " + authority);

            if (authority == null || authority == 0) {
            	System.out.println("User has no authority");
                obj.put("status", "NO_AUTHORITY");
                obj.put("message", "User does not have authority");
                writeResponse(response, obj);
                return;
            }
            
            System.out.println("Checking admission config...");

            // Admission config check
            QrAttendanceBean configBean = new QrAttendanceBean();
            configBean.setProgram_id(dbData.getProgram_id());
            configBean.setAttendance_type(input.getAttendance_type());

            Integer configExists = qrAttendanceDao.checkAdmissionConfig(configBean);
            
            System.out.println("Config result: " + configExists);

            if (configExists == null || configExists == 0) {
            	
            	System.out.println("Admission setup not ready");
                obj.put("status", "CONFIG_NOT_READY");
                obj.put("message", "Admission Setup is not Ready.");
                writeResponse(response, obj);
                return;
            }

            String baseUrl = request.getScheme() + "://" +
                    request.getServerName() + ":" +
                    request.getServerPort() +
                    request.getContextPath();
            
            System.out.println("Everything OK. Preparing response...");

            obj.put("status", "OK");
            obj.put("application_number", dbData.getApplication_number());
            obj.put("program_id", dbData.getProgram_id());
            obj.put("program_name", dbData.getProgram_name());
            obj.put("student_name", dbData.getStudent_name());
            obj.put("dob", dbData.getDob());
            obj.put("attendance_type", input.getAttendance_type());
            obj.put("photo_url", baseUrl + "/qrAttendance/getApplicantImage.htm?appNo=" +
                    dbData.getApplication_number() +
                    "&programId=" + dbData.getProgram_id() +
                    "&type=photo");

            obj.put("signature_url", baseUrl + "/qrAttendance/getApplicantImage.htm?appNo=" +
                    dbData.getApplication_number() +
                    "&programId=" + dbData.getProgram_id() +
                    "&type=signature");
            
            
            System.out.println("Photo Path: " + dbData.getPhoto_path());
            System.out.println("Signature Path: " + dbData.getSignature_path());

        } catch (Exception e) {
            e.printStackTrace();
            obj.put("status", "ERROR");
            obj.put("message", "Fetch failed");
        }

        writeResponse(response, obj);
    }
    

    /* ============================================================
                       IMAGE STREAMING
       ============================================================ */
    public void getApplicantImage(HttpServletRequest request,
            HttpServletResponse response) {

System.out.println("===== getApplicantImage() STARTED =====");

String appNo = request.getParameter("appNo");
String programId = request.getParameter("programId");
String type = request.getParameter("type");

System.out.println("AppNo: " + appNo);
System.out.println("ProgramId: " + programId);
System.out.println("Type: " + type);

if (isBlank(appNo) || isBlank(programId) || isBlank(type)) {
System.out.println("Missing parameters");
response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
return;
}

try {

QrAttendanceBean param = new QrAttendanceBean();
param.setApplication_number(appNo);
param.setProgram_id(programId);   // ✅ IMPORTANT FIX

QrAttendanceBean dbData = qrAttendanceDao.getStudentByQr(param);

if (dbData == null) {
System.out.println("Student not found in DB");
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
return;
}

String baseDir = dbData.getPhoto_path(); // both photo & signature use same base folder

System.out.println("Base Directory from DB: " + baseDir);

if (isBlank(baseDir)) {
System.out.println("Base directory is blank");
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
return;
}

//baseDir = baseDir.trim();

baseDir = baseDir.trim();

//Detect OS
String os = System.getProperty("os.name").toLowerCase();
System.out.println("Detected OS: " + os);

if (os.contains("win")) {
 // Convert Linux path to Windows path
 if (baseDir.startsWith("/home/")) {
     baseDir = "D:" + baseDir.replace("/", "\\");
 }
}

System.out.println("Final BaseDir After OS Fix: " + baseDir);

// Remove trailing slash (Linux & Windows safe)
if (baseDir.endsWith("/") || baseDir.endsWith("\\")) {
baseDir = baseDir.substring(0, baseDir.length() - 1);
}

File directory = new File(baseDir);

if (!directory.exists() || !directory.isDirectory()) {
System.out.println("Directory does not exist: " + baseDir);
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
return;
}

// ✅ Fixed filenames
String fileName = "photo".equalsIgnoreCase(type)
? "photo.jpg"
: "signature.jpg";

File imageFile = new File(directory, fileName);

System.out.println("Full Image Path: " + imageFile.getAbsolutePath());

if (!imageFile.exists() || !imageFile.isFile()) {
System.out.println("Image file not found!");
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
return;
}

response.setContentType("image/jpeg");
response.setContentLengthLong(imageFile.length());

try (InputStream in = new FileInputStream(imageFile);
OutputStream out = response.getOutputStream()) {

byte[] buffer = new byte[8192];
int bytesRead;

while ((bytesRead = in.read(buffer)) != -1) {
out.write(buffer, 0, bytesRead);
}

out.flush();
}

System.out.println("Image served successfully.");

} catch (Exception e) {
e.printStackTrace();
response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}
}
    /* ============================================================
                       MARK ATTENDANCE
       ============================================================ */
    public void markAttendance(HttpServletRequest request, HttpServletResponse response) {

        JSONObject obj = new JSONObject();

        try {
            prepareResponse(request, response);
            String jsonBody = getRequestJson(request);

            if (isBlank(jsonBody)) {
                obj.put("status", "ERROR");
                obj.put("message", "Request body missing");
                writeResponse(response, obj);
                return;
            }

            QrAttendanceBean input = gson.fromJson(jsonBody, QrAttendanceBean.class);

            int result = qrAttendanceDao.saveAttendance(input);

            if (result == 1) {
                obj.put("status", "SUCCESS");
                obj.put("attendance_state", input.getStatus());
                obj.put("message", "Attendance saved");
            } else if (result == 0) {
                obj.put("status", "DUPLICATE");
                obj.put("attendance_state", input.getStatus());
                obj.put("message", "Attendance already marked");
            } else {
                obj.put("status", "FAILED");
                obj.put("message", "Attendance not saved");
            }

        } catch (Exception e) {
            e.printStackTrace();
            obj.put("status", "ERROR");
            obj.put("message", "Attendance marking failed");
        }

        writeResponse(response, obj);
    }



    /* ================= GENERATE EXCEL ================= */
    public void generateExcel(HttpServletRequest request,
                              HttpServletResponse response) {

        System.out.println("===== generateExcel() STARTED =====");

        XSSFWorkbook workbook = null;
        ServletOutputStream out = null;

        try {

            String userName = request.getParameter("userName");
            System.out.println("Received userName: " + userName);

            if (isBlank(userName)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username missing");
                return;
            }

            List<QrAttendanceBean> list = qrAttendanceDao.getExcelReport(userName);
            if (list == null) list = new ArrayList<>();

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Attendance Report");

            /* ---------- HEADER STYLE ---------- */
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            int rowNum = 0;

            Row header = sheet.createRow(rowNum++);
            String[] columns = {
                    "Attendance Type",
                    "Program Name",
                    "Application Number",
                    "Status",
                    "Attendance Time"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            /* ---------- DATA ROWS ---------- */
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

            /* ---------- AUTO SIZE ---------- */
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            /* ---------- RESPONSE CONFIG ---------- */
            response.reset();
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment; filename=AttendanceReport.xlsx");
            response.setHeader("Cache-Control",
                    "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            out = response.getOutputStream();
            workbook.write(out);
            out.flush();

            System.out.println("===== generateExcel() COMPLETED =====");

        } catch (org.apache.catalina.connector.ClientAbortException e) {
            // Normal when user cancels download
            System.out.println("Client aborted download.");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try { if (workbook != null) workbook.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
        }
    }

    /* ================= HELPERS ================= */

    private void prepareResponse(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }

    private String getRequestJson(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void writeResponse(HttpServletResponse response, JSONObject obj) {
        try {
            PrintWriter out = response.getWriter();
            out.print(obj.toJSONString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String normalizeDob(String dob) {
        if (dob == null) return "";
        return dob.replaceAll("[^0-9]", "");
    }
}
