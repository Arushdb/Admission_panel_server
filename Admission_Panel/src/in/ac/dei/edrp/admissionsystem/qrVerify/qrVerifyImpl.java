package in.ac.dei.edrp.admissionsystem.qrVerify;

import com.ibatis.sqlmap.client.SqlMapClient;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import in.ac.dei.edrp.admissionsystem.Bean.qrVerifyBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class qrVerifyImpl implements qrVerifyDao {

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }
    
    @Override
    public List<Map<String, Object>> getProgramList() {
        try {
            return sqlMapClient.queryForList("qrVerify.getProgramList");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insertqrVerify(qrVerifyBean log) {
        try {
            Object result = sqlMapClient.insert("qrVerify.insertQrVerify", log);
            return result == null ? 0 : (Integer) result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Map<String, Object> getStudentByApplicationNumber(String applicationNumber, String programId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("applicationNumber", applicationNumber);
            params.put("programId", programId);

            return (Map<String, Object>) sqlMapClient.queryForObject(
                "qrVerify.getStudentByApplicationNumber", params
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateVerification(qrVerifyBean log) {
        try {
            sqlMapClient.update("qrVerify.updateVerification", log);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
