package in.ac.dei.edrp.admissionsystem.qrVerify;

import java.util.List;
import java.util.Map;
import in.ac.dei.edrp.admissionsystem.Bean.qrVerifyBean;

public interface qrVerifyDao {

    int insertqrVerify(qrVerifyBean log); // existing insert

    // New methods
    List<Map<String, Object>> getProgramList();

    Map<String, Object> getStudentByApplicationNumber(String applicationNumber, String programId);

    void updateVerification(qrVerifyBean log);
}
