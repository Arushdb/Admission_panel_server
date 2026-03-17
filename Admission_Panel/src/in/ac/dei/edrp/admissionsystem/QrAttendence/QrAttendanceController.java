package in.ac.dei.edrp.admissionsystem.QrAttendence;

import com.google.gson.Gson;
import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.Map;

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

            prepareResponse(request,response);

            String jsonBody = getRequestJson(request);

            QrAttendanceBean input =
                    gson.fromJson(jsonBody,QrAttendanceBean.class);

            Map<String,Object> result =
                    qrAttendanceDao.processLogin(input);

            if("OK".equals(result.get("status"))) {
            	 HttpSession session = request.getSession(true);

                 session.setAttribute("username",input.getUserName());
            }

            obj.putAll(result);

        } catch(Exception e){

            obj.put("status","ERROR");
            obj.put("message","Login failed");
        }

        writeResponse(response,obj);
    }


    /* ================= FETCH STUDENT ================= */

    public void fetchStudent(HttpServletRequest request,
                             HttpServletResponse response) {

        JSONObject obj = new JSONObject();

        try {

            prepareResponse(request,response);

            String jsonBody = getRequestJson(request);

            QrAttendanceBean input =
                    gson.fromJson(jsonBody,QrAttendanceBean.class);

            /* username from session */

            HttpSession session = request.getSession();

            if(session == null){
             System.out.println("Seesion is null");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String userName = (String)session.getAttribute("username");
            System.out.println("user name "+userName);

            if(userName == null){

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            input.setUserName(userName);

            Map<String,Object> result =
                    qrAttendanceDao.processFetchStudent(input);

            obj.putAll(result);

            if("OK".equals(result.get("status"))) {

                QrAttendanceBean student =
                        (QrAttendanceBean) result.get("student");

                String baseUrl =
                        request.getScheme()+"://"+
                        request.getServerName()+":"+
                        request.getServerPort()+
                        request.getContextPath();

                obj.put("application_number",student.getApplication_number());
                obj.put("program_id",student.getProgram_id());
                obj.put("program_name",student.getProgram_name());
                obj.put("student_name",student.getStudent_name());
                obj.put("dob",student.getDob());
                obj.put("attendance_type",input.getAttendance_type());

                obj.put("photo_url",
                        baseUrl+"/qrAttendance/getApplicantImage.htm?appNo="
                        +student.getApplication_number()
                        +"&programId="+student.getProgram_id()
                        +"&type=photo");

                obj.put("signature_url",
                        baseUrl+"/qrAttendance/getApplicantImage.htm?appNo="
                        +student.getApplication_number()
                        +"&programId="+student.getProgram_id()
                        +"&type=signature");
            }

        } catch(Exception e){

            obj.put("status","ERROR");
            obj.put("message","Fetch failed");
        }

        writeResponse(response,obj);
    }


    /* ================= VERIFY ================= */

    public void verifyAttendance(HttpServletRequest request,
                                 HttpServletResponse response) {
    	
    	
    	

        JSONObject obj = new JSONObject();

        try {

            prepareResponse(request,response);

            String json = getRequestJson(request);

            System.out.println("VERIFY REQUEST: " + json);

            QrAttendanceBean input =
                    gson.fromJson(json,QrAttendanceBean.class);
            
            HttpSession session = request.getSession(false);

            if(session == null){
                response.setStatus(401);
                return;
            }

            String userName = (String)session.getAttribute("username");

            if(userName == null){
                response.setStatus(401);
                return;
            }

            if(input.getUserName()==null){
                obj.put("status","ERROR");
                obj.put("message","Username missing");
                writeResponse(response,obj);
                return;
            }

            Map<String,Object> result =
                    qrAttendanceDao.processVerifyAttendance(input);

            obj.putAll(result);

        }catch(Exception e){

            e.printStackTrace();

            obj.put("status","ERROR");
            obj.put("message","Verify attendance failed");
        }

        writeResponse(response,obj);
    }


    /* ================= UNVERIFY ================= */

    public void unverifyAttendance(HttpServletRequest request,
                                   HttpServletResponse response) {

        JSONObject obj = new JSONObject();

        try {

            prepareResponse(request,response);

            String json = getRequestJson(request);

            System.out.println("UNVERIFY REQUEST: " + json);

            QrAttendanceBean input =
                    gson.fromJson(json,QrAttendanceBean.class);
            
            HttpSession session = request.getSession(false);

            if(session == null){
                response.setStatus(401);
                return;
            }

           String userName = (String)session.getAttribute("username");

            if(userName == null){
                response.setStatus(401);
                return;
            }

            Map<String,Object> result =
                    qrAttendanceDao.processUnverifyAttendance(input);

            obj.putAll(result);

        }catch(Exception e){

            e.printStackTrace();

            obj.put("status","ERROR");
            obj.put("message","Unverify attendance failed");
        }

        writeResponse(response,obj);
    }


    /* ================= IMAGE STREAM ================= */

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
response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
return;
}

try {

File imageFile = qrAttendanceDao.getApplicantImageFile(appNo, programId, type);

if (imageFile == null || !imageFile.exists()) {
System.out.println("Image not found");
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

System.out.println("Image served successfully");

} catch (Exception e) {

e.printStackTrace();
response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}
}
    
    
    public void generateExcel(HttpServletRequest request,
            HttpServletResponse response) {

System.out.println("===== generateExcel() STARTED =====");

try {

/* Username from session */

	HttpSession session = request.getSession(false);

	if(session == null){

	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    return;
	}

	String userName = (String)session.getAttribute("username");

/* fallback if session missing */

if (isBlank(userName)) {
userName = request.getParameter("userName");
}

System.out.println("Excel requested by user: " + userName);

if (isBlank(userName)) {
response.sendError(HttpServletResponse.SC_BAD_REQUEST,
      "Username missing");
return;
}

/* call DAO */

qrAttendanceDao.generateExcelReport(userName, response);

System.out.println("===== generateExcel() COMPLETED =====");

} catch (Exception e) {

e.printStackTrace();

try {
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
} catch (Exception ignored) {}
}
}

    /* ================= HELPERS ================= */
    
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void prepareResponse(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }

    private String getRequestJson(HttpServletRequest request) {

        try(BufferedReader reader = request.getReader()){

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine())!=null){
                sb.append(line);
            }

            return sb.toString();

        } catch(Exception e){
            return null;
        }
    }

    private void writeResponse(HttpServletResponse response, JSONObject obj) {

        try{

            PrintWriter out = response.getWriter();
            out.print(obj.toJSONString());
            out.flush();

        } catch(Exception ignored){}
    }
}