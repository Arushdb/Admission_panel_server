package in.ac.dei.edrp.admissionsystem.studentModule;

import java.util.List;

import javax.servlet.ServletContext;



import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.ReportInfoGetter;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.transferBean;

public interface studentDao {
	
	public List<admissionBean> getCustomeGridData(admissionBean input);
	public String generateCourseDetailPdf(admissionBean input,ServletContext sc);

}
