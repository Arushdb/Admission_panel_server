package in.ac.dei.edrp.admissionsystem.verification;

import java.awt.image.BufferedImage;
import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.studentBean;

public interface VerifyDao {
	
	public List<studentBean>getAcademicMarks(studentBean sbean);
	public int updatestatus(studentBean sbean);
	public int validateIWlist(studentBean sbean);
	public List<studentBean> getUserProgramList(String user); //added by Jyoti on 19 Jun 2025
	public List<studentBean> getVfyProgramList(studentBean sbean); //added by Jyoti on 19 Jun 2025
	public List<studentBean> chkliststatus(studentBean sbean); //added by Jyoti on 19 Jun 2025
	public int updateliststatus(studentBean sbean); //added by Jyoti on 19 Jun 2025
	public BufferedImage VerifySignature(studentBean sbean);
	
	public List<studentBean> getApplicantPrograms(studentBean sbean);
	public List<studentBean> getUserPrograms(studentBean sbean);
	public List<studentBean> getAttendance(studentBean sbean);
	public List<studentBean> checkIWcalled(studentBean sbean);
	
	public List<studentBean> checkComponentmarks(studentBean sbean);
	public List<studentBean> getInterviewLevel(studentBean sbean);
}
