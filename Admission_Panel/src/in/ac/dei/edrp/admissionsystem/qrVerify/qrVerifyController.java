package in.ac.dei.edrp.admissionsystem.qrVerify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import in.ac.dei.edrp.admissionsystem.Bean.qrVerifyBean;

public class qrVerifyController extends MultiActionController {

    private qrVerifyDao qrVerifyDao;

    public void setqrVerifyDao(qrVerifyDao qrVerifyDao) {
        this.qrVerifyDao = qrVerifyDao;
    }

    // 1️⃣ Fetch program list
    public void getProgramList(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> programList = qrVerifyDao.getProgramList();
        Map<String, Object> model = new HashMap<>();
        model.put("data", programList);

        writeJson(response, model);
    }

    // 2️⃣ Scan QR - fetch student info
    public void scanQr(HttpServletRequest request, HttpServletResponse response) {
        String applicationNumber = request.getParameter("applicationNumber");
        String programId = request.getParameter("programId");

        Map<String, Object> student = qrVerifyDao.getStudentByApplicationNumber(applicationNumber, programId);

        Map<String, Object> model = new HashMap<>();
        if (student != null && !student.isEmpty()) {
            model.put("exists", true);
            model.put("student", student);
        } else {
            model.put("exists", false);
        }

        writeJson(response, model);
    }

    // 3️⃣ Mark as VERIFIED
    public void markVerified(HttpServletRequest request, HttpServletResponse response) {
        String logIdStr = request.getParameter("logId");
        String status = request.getParameter("status"); // Y / N
        String operator = (String) request.getSession().getAttribute("userName");

        int logId = Integer.parseInt(logIdStr);

        qrVerifyBean log = new qrVerifyBean();
        log.setId(logId);
        log.setVerificationStatus(status);
        log.setVerifiedBy(operator);

        qrVerifyDao.updateVerification(log);

        Map<String, Object> model = new HashMap<>();
        model.put("success", true);

        writeJson(response, model);
    }

    // ✅ Utility method to write JSON to response
    private void writeJson(HttpServletResponse response, Object data) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(data);
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
