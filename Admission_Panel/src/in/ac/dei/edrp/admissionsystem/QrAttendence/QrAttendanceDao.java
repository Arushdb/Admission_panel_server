package in.ac.dei.edrp.admissionsystem.QrAttendence;

import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;

public interface QrAttendanceDao {

    QrAttendanceBean getStudentByQr(QrAttendanceBean input);

    int saveAttendance(QrAttendanceBean input);

    int checkLogin(QrAttendanceBean input);
}
