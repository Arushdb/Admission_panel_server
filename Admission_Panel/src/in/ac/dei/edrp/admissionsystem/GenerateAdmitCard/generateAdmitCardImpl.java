package in.ac.dei.edrp.admissionsystem.GenerateAdmitCard;

import java.nio.file.Paths;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.EntranceTestPaper;

public class generateAdmitCardImpl extends SqlMapClientDaoSupport implements GenerateAdmitCardDao {

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBean> getPrograms() {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getPrograms");
    }
    
    @Override
    public GenerateAdmitCardBean getRetMonthYear() {
        return (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("generateAdmitCard.getRetMonthYear");
    }
   

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBean> getApplicantsEntrance(String programId) {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getApplicantsEntrance", programId);
    }

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBean> getApplicantsDirect(String programId) {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getApplicantsDirect", programId);
    }

    @SuppressWarnings("unchecked")
    public List<EntranceTestPaper> getEntranceTestPapers(String applicationNumber, String programId) {
        GenerateAdmitCardBean param = new GenerateAdmitCardBean();
        param.setApplicationNumber(applicationNumber);
        param.setProgramId(programId);
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getETForAdmCard", param);
    }

    public void updateAdmitCardPath(String registrationNumber, String admitCardPath) {
        GenerateAdmitCardBean param = new GenerateAdmitCardBean();
        param.setRegistrationNumber(registrationNumber);
        param.setAdmitCardPath(admitCardPath);  // use as-is
        getSqlMapClientTemplate().update("generateAdmitCard.updateAdmitCardPath", param);
    }


    public void setAdmitCardAvailable(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            System.out.println("[WARN] Skipping setAdmitCardAvailable - registrationNumber is null/empty");
            return;
        }
        getSqlMapClientTemplate().update("generateAdmitCard.setAdmitCardAvailable", registrationNumber);
    }

    public void setAdmitCardPublished(String programId) {
        getSqlMapClientTemplate().update("generateAdmitCard.setAdmitCardPublished", programId);
    }

    // --- Get Session Year ---
    public GenerateAdmitCardBean getSessionYear() {
        return (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("generateAdmitCard.getsessiondate");
    }

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBean> getOtherPrograms(String applicationNumber, String selectedProgramId) {
        GenerateAdmitCardBean param = new GenerateAdmitCardBean();
        param.setApplicationNumber(applicationNumber);
        param.setProgramId(selectedProgramId); // ✅ maps to #programId#
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getOtherPrograms", param);
    }
}
