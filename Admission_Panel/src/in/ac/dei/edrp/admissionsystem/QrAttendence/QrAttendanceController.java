package in.ac.dei.edrp.admissionsystem.QrAttendence;

import com.google.gson.Gson;
import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

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

    /* ================= FETCH STUDENT (QR VALIDATION) ================= */
    public void fetchStudent(HttpServletRequest request, HttpServletResponse response) {

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

            if (input == null || isBlank(input.getApplication_number())) {
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR data missing");
                writeResponse(response, obj);
                return;
            }

            // Parse QR
            String[] parts = input.getApplication_number().trim().split("\\|");
            if (parts.length < 3) {
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR format invalid");
                writeResponse(response, obj);
                return;
            }

            String qrAppNo = parts[0].trim();
            String qrName  = parts[1].trim();
            String qrDob   = parts[2].trim();

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(qrAppNo);

            QrAttendanceBean dbData = qrAttendanceDao.getStudentByQr(param);

            if (dbData == null) {
                obj.put("status", "INVALID_QR");
                obj.put("message", "Student not found");
                writeResponse(response, obj);
                return;
            }

            // Validate
            boolean nameMatch = qrName.equalsIgnoreCase(dbData.getStudent_name().trim());
            boolean dobMatch = normalizeDob(qrDob).equals(normalizeDob(dbData.getDob()));

            if (!nameMatch || !dobMatch) {
                obj.put("status", "INVALID_QR");
                obj.put("message", "QR data does not match");
                writeResponse(response, obj);
                return;
            }

            // Build URLs
            String baseUrl = request.getScheme() + "://" +
                    request.getServerName() + ":" +
                    request.getServerPort() +
                    request.getContextPath();

            String photoUrl = baseUrl + "/qrAttendance/getApplicantImage.htm?appNo=" +
                    dbData.getApplication_number() + "&type=photo";

            String signatureUrl = baseUrl + "/qrAttendance/getApplicantImage.htm?appNo=" +
                    dbData.getApplication_number() + "&type=signature";

            obj.put("status", "OK");
            obj.put("application_number", dbData.getApplication_number());
            obj.put("program_id", dbData.getProgram_id());
            obj.put("program_name", dbData.getProgram_name());
            obj.put("student_name", dbData.getStudent_name());
            obj.put("dob", dbData.getDob());
            obj.put("photo_url", photoUrl);
            obj.put("signature_url", signatureUrl);

        } catch (Exception e) {
            e.printStackTrace();
            obj.put("status", "ERROR");
            obj.put("message", "Fetch failed");
        }

        writeResponse(response, obj);
    }

    /* ================= IMAGE STREAM ENDPOINT ================= 
    public void getApplicantImage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println(">>> getApplicantImage() CALLED <<<");

        try {
            String appNo = request.getParameter("appNo");
            String type  = request.getParameter("type"); // photo | signature

            if (isBlank(appNo) || isBlank(type)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(appNo);

            QrAttendanceBean dbData = qrAttendanceDao.getStudentByQr(param);

            if (dbData == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String filePath = null;

            if ("photo".equalsIgnoreCase(type)) {
                filePath = dbData.getPhoto_path();
            } else if ("signature".equalsIgnoreCase(type)) {
                filePath = dbData.getSignature_path();
            }

            if (isBlank(filePath)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            File imageFile = new File(filePath);

            System.out.println("PHOTO PATH = " + dbData.getPhoto_path());
            System.out.println("SIGNATURE PATH = " + dbData.getSignature_path());
            System.out.println("IMAGE FILE = " + imageFile.getAbsolutePath());
            System.out.println("EXISTS     = " + imageFile.exists());

            if (!imageFile.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.reset();
            response.setContentType("image/jpeg");
            response.setContentLengthLong(imageFile.length());
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            try (InputStream in = new BufferedInputStream(new FileInputStream(imageFile));
                 OutputStream out = new BufferedOutputStream(response.getOutputStream())) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } ===*/
    
    /* ================= IMAGE STREAM ENDPOINT ================= */
    public void getApplicantImage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println(">>> getApplicantImage() CALLED <<<");

        try {
            String appNo = request.getParameter("appNo");
            String type  = request.getParameter("type"); // photo | signature

            if (isBlank(appNo) || isBlank(type)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            QrAttendanceBean param = new QrAttendanceBean();
            param.setApplication_number(appNo);

            QrAttendanceBean dbData = qrAttendanceDao.getStudentByQr(param);
            if (dbData == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String baseDir;
            String fileName;

            if ("photo".equalsIgnoreCase(type)) {
                baseDir = dbData.getPhoto_path();
                fileName = "photo.jpg";
            } else if ("signature".equalsIgnoreCase(type)) {
                baseDir = dbData.getSignature_path();
                fileName = "signature.jpg";
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            File imageFile = new File(baseDir + File.separator + fileName);

            System.out.println("FINAL FILE = " + imageFile.getAbsolutePath());
            System.out.println("EXISTS     = " + imageFile.exists());
            System.out.println("IS FILE    = " + imageFile.isFile());
            System.out.println("CAN READ  = " + imageFile.canRead());

            if (!imageFile.exists() || !imageFile.isFile()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.reset();
            response.setContentType("image/jpeg");
            response.setContentLengthLong(imageFile.length());

            try (InputStream in = new FileInputStream(imageFile);
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= MARK ATTENDANCE ================= */
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

            int count = qrAttendanceDao.saveAttendance(input);

            if (count > 0) {
                obj.put("status", "SUCCESS");
                obj.put("message", "Attendance saved");
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

    /* ================= HELPERS ================= */
    private void prepareResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
