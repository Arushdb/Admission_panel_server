package in.ac.dei.edrp.admissionsystem.GenerateAdmitCard;

import java.util.List;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBeanNew;
import in.ac.dei.edrp.admissionsystem.Bean.EntranceTestPaper;

public interface GenerateAdmitCardDao {
    List<GenerateAdmitCardBeanNew> getPrograms();
    List<GenerateAdmitCardBeanNew> getApplicantsEntrance(String programId);
    List<GenerateAdmitCardBeanNew> getOtherPrograms(String applicationNumber, String programId);


    List<GenerateAdmitCardBeanNew> getApplicantsDirect(String programId);
    List<EntranceTestPaper> getEntranceTestPapers(String applicationNumber, String programId);
    void updateAdmitCardPath(String registrationNumber, String admitCardPath);
    
    void setAdmitCardAvailable(String registrationNumber);
    void setAdmitCardPublished(String programId);
 // Fetch current session year from university_master
    GenerateAdmitCardBeanNew getSessionYear();
    GenerateAdmitCardBeanNew getRetMonthYear();
    
    
}
