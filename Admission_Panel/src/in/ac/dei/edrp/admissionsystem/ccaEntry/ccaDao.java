package in.ac.dei.edrp.admissionsystem.ccaEntry;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.statusBean;

import java.util.List;

import javax.servlet.ServletContext;

public interface ccaDao {
public List<cca_intBean>getCandidateInfo(cca_intBean input);
public List<cca_intBean>getCouncellingStatus(cca_intBean input);
public List<cca_intBean>getMarksInfo(cca_intBean input);

public List<cca_intBean>checkApplicationStatus(cca_intBean input);
public List<cca_intBean>checkCallListStatus(cca_intBean input);
public List<cca_intBean>checkEligibilityStatus(cca_intBean input);
public List<cca_intBean>InterviewStatus(cca_intBean input);
public List<cca_intBean>checkfinalMeritListStatus(cca_intBean input);

public List<String>doAction(cca_intBean input);
public List<String>doActionForBulk(cca_intBean input);
public List<String>doActionForBulk_GD(cca_intBean input);

public List<String>EditRecord(cca_intBean input);
public List<cca_intBean>updatedStudentRecord(cca_intBean input);
public List<cca_intBean>getStudentData(cca_intBean input);
public List<statusBean>uploadMarks(statusBean input, ServletContext sc);
public List<statusBean>updateETmarks(statusBean input);

}
