package in.ac.dei.edrp.admissionsystem.QrAttendence;

import in.ac.dei.edrp.admissionsystem.Bean.QrAttendanceBean;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

public class QrAttendanceDaoImpl extends SqlMapClientDaoSupport
        implements QrAttendanceDao {

    @Override
    public QrAttendanceBean getStudentByQr(QrAttendanceBean input) {

        if (input == null || input.getApplication_number() == null) {
            return null;
        }

        input.setApplication_number(input.getApplication_number().trim());

        return (QrAttendanceBean) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.getStudentByQr", input);
    }

    /* ================= INSERT ATTENDANCE ================= */

    @Override
    public int saveAttendance(QrAttendanceBean input) {

        if (input == null) {
            return -1;
        }

        // 🔹 duplicate check again for safety
        Integer exists = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate", input);

        if (exists != null && exists > 0) {
            return 0; // already exists
        }

        getSqlMapClientTemplate()
                .update("QrAttendance.insertAttendance", input);

        return 1;
    }

    /* ================= CHECK DUPLICATE (FETCH TIME) ================= */

    @Override
    public Integer checkAttendance(QrAttendanceBean input) {

        if (input == null) {
            return 0;
        }

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate", input);

        return count == null ? 0 : count;
    }

    /* ================= AUTHORITY ================= */

    @Override
    public Integer checkAttendanceAuthority(QrAttendanceBean input) {

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceAuthority", input);

        return count == null ? 0 : count;
    }

    /* ================= LOGIN ================= */

    @Override
    public int checkLogin(QrAttendanceBean input) {

        if (input == null) {
            return 0;
        }

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkLogin", input);

        return count == null ? 0 : count;
    }

    /* ================= ADMISSION CONFIG ================= */

    @Override
    public Integer checkAdmissionConfig(QrAttendanceBean input) {

        if (input == null) {
            return 0;
        }

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAdmissionConfig", input);

        return count == null ? 0 : count;
    }

    /* ================= EXCEL REPORT ================= */

    @Override
    public List<QrAttendanceBean> getExcelReport(String userName) {

        return getSqlMapClientTemplate()
                .queryForList("QrAttendance.getExcelReport", userName);
    }
}