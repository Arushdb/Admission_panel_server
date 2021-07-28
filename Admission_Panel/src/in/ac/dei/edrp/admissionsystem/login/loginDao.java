package in.ac.dei.edrp.admissionsystem.login;

import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;

import java.util.List;


public interface loginDao {
	
	public List<admissionBean> checkLogin(admissionBean input);
	public List<admissionBean> LoadEntityCombo(admissionBean input);//d
	public List<admissionBean> LoadProgramCombo(admissionBean input);//d
	public List<admissionBean> LoadSession(admissionBean input);//d
	
	public List<admissionBean> LoadSemesterCombo(admissionBean input);//d
	public List<admissionBean> LoadBranchCombo(admissionBean input);//d
}
