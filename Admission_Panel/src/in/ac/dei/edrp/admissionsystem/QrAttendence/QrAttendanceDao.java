package in.ac.dei.edrp.admissionsystem.QrAttendence;

import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import java.util.List;

public interface QrAttendanceDao {

    QrAttendanceBean getStudentByQr(QrAttendanceBean input);

    int saveAttendance(QrAttendanceBean input);

    Integer checkAttendance(QrAttendanceBean input);

    Integer checkAttendanceAuthority(QrAttendanceBean input);

    Integer checkAdmissionConfig(QrAttendanceBean input);

    int checkLogin(QrAttendanceBean input);

    List<QrAttendanceBean> getExcelReport(String userName);
}