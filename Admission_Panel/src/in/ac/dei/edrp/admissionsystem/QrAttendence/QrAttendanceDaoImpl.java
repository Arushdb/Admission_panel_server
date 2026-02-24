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

        // 🔹 Trim to avoid QR whitespace issues
        input.setApplication_number(input.getApplication_number().trim());

        return (QrAttendanceBean) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.getStudentByQr", input);
    }

  //  @Override
  //  public int saveAttendance(QrAttendanceBean input) {

     //   if (input == null) {
     //       return 0;
     //   }

     //   return getSqlMapClientTemplate()
      //          .update("QrAttendance.insertAttendance", input);
 //   }
    
    
    @Override
    public int saveAttendance(QrAttendanceBean input) {

        if (input == null) {
            return -1;
        }

        // 1️⃣ check duplicate
        Integer exists = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceDuplicate", input);

        if (exists != null && exists > 0) {
            return 0;   // already exists
        }

        // 2️⃣ insert
        getSqlMapClientTemplate()
                .update("QrAttendance.insertAttendance", input);

        return 1;       // inserted
    }

    
    @Override
    public Integer checkAttendanceAuthority(QrAttendanceBean input) {

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAttendanceAuthority", input);

        return count == null ? 0 : count;
    }


    @Override
    public int checkLogin(QrAttendanceBean input) {

        if (input == null) {
            return 0;
        }

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkLogin", input);

        return count == null ? 0 : count;
    }
    
    @Override
    public Integer checkAdmissionConfig(QrAttendanceBean input) {

        if (input == null) {
            return 0;
        }

        Integer count = (Integer) getSqlMapClientTemplate()
                .queryForObject("QrAttendance.checkAdmissionConfig", input);

        return count == null ? 0 : count;
    }
    
    public List<QrAttendanceBean> getExcelReport(String userName) {
        return getSqlMapClientTemplate()
            .queryForList("QrAttendance.getExcelReport", userName);
    }


}
