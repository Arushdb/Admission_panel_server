package in.ac.dei.edrp.admissionsystem.verification;

import java.util.List;


import in.ac.dei.edrp.admissionsystem.Bean.studentBean;

public interface VerifyDao {
	
	public List<studentBean>getAcademicMarks(studentBean sbean);
	public int updatestatus(studentBean sbean);
	public int validateIWlist(studentBean sbean);

}
