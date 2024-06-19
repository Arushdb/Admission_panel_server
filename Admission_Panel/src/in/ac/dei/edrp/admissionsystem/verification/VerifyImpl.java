package in.ac.dei.edrp.admissionsystem.verification;

import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.studentBean;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class VerifyImpl extends SqlMapClientDaoSupport implements VerifyDao {

	@Override
	public List<studentBean> getAcademicMarks(studentBean sbean) {
		
		List <studentBean> studentlist = null;
		
	 studentlist =getSqlMapClientTemplate().queryForList("verifystudent.getstudentmarks",sbean);
		// TODO Auto-generated method stub
		return studentlist;
	}

	@Override
	public int updatestatus(studentBean sbean) {
		// TODO Auto-generated method stub
		int count =getSqlMapClientTemplate().update("verifystudent.updatestatus",sbean);
		return count;
		
	}

	@Override
	public int validateIWlist(studentBean sbean) {
		List <studentBean> studentlist = null;
		// TODO Auto-generated method stub
		studentlist =getSqlMapClientTemplate().queryForList("verifystudent.validateIWlist",sbean);
		
		return studentlist.size();
	}
	



}
