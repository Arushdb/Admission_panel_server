package in.ac.dei.edrp.admissionsystem.login;

import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;



public class loginImpl extends SqlMapClientDaoSupport implements  loginDao{

	public List<admissionBean> checkLogin(admissionBean input) {
		
		System.out.println("Server Side working");
		List<admissionBean>  getuserData = new ArrayList<admissionBean>();
		 getuserData =getSqlMapClientTemplate().queryForList("login.getuserData",input);
		 if (getuserData.size()==0)
		 {
			 getuserData =getSqlMapClientTemplate().queryForList("login.getuserDatafromedit_panel_authority",input);
		 }
		
		return getuserData;
	}

	public List<admissionBean> LoadEntityCombo(admissionBean input) {
		
		List<admissionBean> getentity = new ArrayList<admissionBean>();
		if (input.getFlag().equalsIgnoreCase("TimeTable"))
		{
			getentity =getSqlMapClientTemplate().queryForList("login.getEntityDataTimeTable",input);
		}
		else
		{
			getentity =getSqlMapClientTemplate().queryForList("login.getEntityData",input);
		}
		
		
		return getentity;
	}

	public List<admissionBean> LoadProgramCombo(admissionBean input) {
		
		List<admissionBean> getprogram = new ArrayList<admissionBean>();
		
		
		if (input.getFlag().equalsIgnoreCase("PE"))
		{
			getprogram =getSqlMapClientTemplate().queryForList("login.getProgramDataPE",input);
		}
		else if(input.getFlag().equalsIgnoreCase("ARB"))
		{
			getprogram =getSqlMapClientTemplate().queryForList("login.getProgramDataArbitration",input);
		}
		else if(input.getFlag().equalsIgnoreCase("TimeTable"))
		{
			getprogram =getSqlMapClientTemplate().queryForList("login.getProgramDataForTimeTable",input);
		}
			
		else
		{
			getprogram =getSqlMapClientTemplate().queryForList("login.getProgramData",input);
		}
		
		return getprogram;
	}

	public List<admissionBean> LoadSession(admissionBean input) {
		List<admissionBean> getSession =getSqlMapClientTemplate().queryForList("login.getSessionDate",input);
		System.out.println("session-"+getSession.size());
		return getSession;
	}

	public List<admissionBean> LoadSemesterCombo(admissionBean input) {
	
		List<admissionBean> sem =getSqlMapClientTemplate().queryForList("login.getsemesterData",input);
		return sem;
	}

	public List<admissionBean> LoadBranchCombo(admissionBean input) {
		
		List<admissionBean> branch =getSqlMapClientTemplate().queryForList("login.getBranchDataFromCMS",input);
		return branch;
	}

}
