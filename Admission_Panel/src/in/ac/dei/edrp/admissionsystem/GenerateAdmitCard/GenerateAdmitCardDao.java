package in.ac.dei.edrp.admissionsystem.GenerateAdmitCard;

import java.util.List;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.EntranceTestPaper;

public interface GenerateAdmitCardDao {
    List<GenerateAdmitCardBean> getPrograms();
    List<GenerateAdmitCardBean> getApplicantsEntrance(String programId);
    List<GenerateAdmitCardBean> getOtherPrograms(String applicationNumber, String programId);


    List<GenerateAdmitCardBean> getApplicantsDirect(String programId);
    List<EntranceTestPaper> getEntranceTestPapers(String applicationNumber, String programId);
    void updateAdmitCardPath(String registrationNumber, String admitCardPath);
    
    void setAdmitCardAvailable(String registrationNumber);
    void setAdmitCardPublished(String programId);
 // Fetch current session year from university_master
    GenerateAdmitCardBean getSessionYear();
    
}
