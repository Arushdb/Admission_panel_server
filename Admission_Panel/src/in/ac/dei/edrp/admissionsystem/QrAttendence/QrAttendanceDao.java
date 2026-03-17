package in.ac.dei.edrp.admissionsystem.QrAttendence;

import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

public interface QrAttendanceDao {

    /* ================= LOGIN ================= */

    Map<String, Object> processLogin(QrAttendanceBean input);


    /* ================= FETCH STUDENT ================= */

    Map<String, Object> processFetchStudent(QrAttendanceBean input);


    /* ================= MARK ATTENDANCE ================= */

    Map<String, Object> processMarkAttendance(QrAttendanceBean input);


    /* ================= LOGIN CHECK ================= */

    int checkLogin(QrAttendanceBean input);


    /* ================= DUPLICATE ATTENDANCE CHECK ================= */

    Integer checkAttendance(QrAttendanceBean input);


    /* ================= AUTHORITY CHECK ================= */

    Integer checkAttendanceAuthority(QrAttendanceBean input);


    /* ================= ADMISSION CONFIG CHECK ================= */

    Integer checkAdmissionConfig(QrAttendanceBean input);


    /* ================= SAVE ATTENDANCE ================= */

    int saveAttendance(QrAttendanceBean input);


    /* ================= EXCEL REPORT ================= */

    void generateExcelReport(String userName, HttpServletResponse response);
    
    

    /* ================= APPLICANT IMAGE ================= */

    File getApplicantImageFile(String appNo, String programId, String type);
    
    Map<String,Object> processVerifyAttendance(QrAttendanceBean input);

    Map<String,Object> processUnverifyAttendance(QrAttendanceBean input);

}