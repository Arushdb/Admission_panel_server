package in.ac.dei.edrp.admissionsystem.computation;

import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.ReportInfoGetter;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.transferBean;

public interface computationDao {
	
	public String runComputation(ReportInfoGetter input);
	public transferBean transferApp( transferBean input);
	public String runComputationforAll(ReportInfoGetter input);
	
	public String finalMeritListProcess(ReportInfoGetter input);
	public String finalMeritListProcessforAll(ReportInfoGetter input);
	public List<admissionBean> getCustomeGridData(admissionBean input);
	public admissionBean UpdateSCLMarks(admissionBean input);
	
	public admissionBean UpdateFinalStatus(admissionBean input);

}
