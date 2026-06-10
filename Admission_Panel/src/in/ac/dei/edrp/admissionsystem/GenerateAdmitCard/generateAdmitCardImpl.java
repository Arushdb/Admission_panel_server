package in.ac.dei.edrp.admissionsystem.GenerateAdmitCard;

import java.nio.file.Paths;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBeanNew;
import in.ac.dei.edrp.admissionsystem.Bean.EntranceTestPaper;

public class generateAdmitCardImpl extends SqlMapClientDaoSupport implements GenerateAdmitCardDao {

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBeanNew> getPrograms() {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getPrograms");
    }
    
    @Override
    public GenerateAdmitCardBeanNew getRetMonthYear() {
        return (GenerateAdmitCardBeanNew) getSqlMapClientTemplate().queryForObject("generateAdmitCard.getRetMonthYear");
    }
   

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBeanNew> getApplicantsEntrance(String programId) {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getApplicantsEntrance", programId);
    }

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBeanNew> getApplicantsDirect(String programId) {
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getApplicantsDirect", programId);
    }

    @SuppressWarnings("unchecked")
    public List<EntranceTestPaper> getEntranceTestPapers(String applicationNumber, String programId) {
    	GenerateAdmitCardBeanNew param = new GenerateAdmitCardBeanNew();
        param.setApplicationNumber(applicationNumber);
        param.setProgramId(programId);
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getETForAdmCard", param);
    }

    public void updateAdmitCardPath(String registrationNumber, String admitCardPath) {
    	GenerateAdmitCardBeanNew param = new GenerateAdmitCardBeanNew();
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

   // public void setAdmitCardPublished(String programId) {
   //     getSqlMapClientTemplate().update("generateAdmitCard.setAdmitCardPublished", programId);
  //  }

    // --- Get Session Year ---
    public GenerateAdmitCardBeanNew getSessionYear() {
        return (GenerateAdmitCardBeanNew) getSqlMapClientTemplate().queryForObject("generateAdmitCard.getsessiondate");
    }

    @SuppressWarnings("unchecked")
    public List<GenerateAdmitCardBeanNew> getOtherPrograms(String applicationNumber, String selectedProgramId) {
    	GenerateAdmitCardBeanNew param = new GenerateAdmitCardBeanNew();
        param.setApplicationNumber(applicationNumber);
        param.setProgramId(selectedProgramId); // ✅ maps to #programId#
        return getSqlMapClientTemplate().queryForList("generateAdmitCard.getOtherPrograms", param);
    }
}
