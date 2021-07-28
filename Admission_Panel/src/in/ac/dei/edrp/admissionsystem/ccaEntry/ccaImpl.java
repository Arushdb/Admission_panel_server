package in.ac.dei.edrp.admissionsystem.ccaEntry;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.statusBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ccaImpl extends SqlMapClientDaoSupport implements ccaDao {

	public List<cca_intBean> getCandidateInfo(cca_intBean input) 
	{
		List<cca_intBean> CandidateInfo = getSqlMapClientTemplate().queryForList("ccaint.candidateInfo",input);
		return CandidateInfo;
		
	}
	public List<cca_intBean> getCouncellingStatus(cca_intBean input) {
		
		List<cca_intBean> CandidateInfo = getSqlMapClientTemplate().queryForList("ccaint.getCouncenllingStatus",input);
		return CandidateInfo;
	}

	public List<cca_intBean> getMarksInfo(cca_intBean input) {
		
		List<cca_intBean> marksInfo = getSqlMapClientTemplate().queryForList("ccaint.MarksInfo",input);
		return marksInfo;
	}
	
	
	
	
	public List<String> doActionForBulk(cca_intBean input) {
		
		List<cca_intBean> ApplicationsList = new ArrayList<cca_intBean>();
		List<String> checkRecord=new ArrayList<String>();
		
		if (input.getFlag().equalsIgnoreCase("PW"))
		{
			ApplicationsList=getSqlMapClientTemplate().queryForList("ccaint.getApplicationsListForInterview");
		}
		else if (input.getFlag().equalsIgnoreCase("CA"))
		{
			ApplicationsList=getSqlMapClientTemplate().queryForList("ccaint.getApplicationsListForCCA");
		}
		
		for(int e=0;e<ApplicationsList.size();e++)
		{
			input.setApplication_number(ApplicationsList.get(e).getApplication_number());
			input.setMarks(ApplicationsList.get(e).getMarks());
			input.setCreator("EXCEL");
		
		List<cca_intBean> checkBinding=getSqlMapClientTemplate().queryForList("ccaint.getBindedApplication",input);
		List<cca_intBean> getsessionDate=getSqlMapClientTemplate().queryForList("ccaint.getSession");
		List<cca_intBean> checkUnBinding=new ArrayList<cca_intBean>();
		
		
		cca_intBean input1 = new cca_intBean();
		input1.setStart_date(getsessionDate.get(0).getStart_date());
		input1.setEnd_date(getsessionDate.get(0).getEnd_date());
		if (checkBinding.size()>0) // for binded applications 
		{
			input1.setCount(String.valueOf(checkBinding.size()));
			for (int j=0;j<checkBinding.size();j++)
			{
				
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setApplication_number(checkBinding.get(j).getApp_number());
				//input1.setInterview(input.getInterview());
				input1.setEntity_id(checkBinding.get(j).getEntity_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setMarks(input.getMarks());
				input1.setFlag(input.getFlag());
				input1.setCreator(input.getCreator());
				updateMethod2(input1);
			}
		}
		else
		{
		
			checkUnBinding=getSqlMapClientTemplate().queryForList("ccaint.getUnBindedApplication",input);	
			
		
			if (checkUnBinding.size()>0)
			{
				input1.setCount(String.valueOf(checkUnBinding.size()));
				for (int b=0 ; b<checkUnBinding.size();b++)
				{
					input1.setRegistration_number(checkUnBinding.get(b).getRegistration_number());
					input1.setApplication_number(checkUnBinding.get(b).getApp_number());
					input1.setMarks(input.getMarks());
					input1.setFlag(input.getFlag());
					input1.setCreator(input.getCreator());
					//input1.setInterview(input.getInterview());
					input1.setEntity_id(checkUnBinding.get(b).getEntity_id());
					input1.setProgram_id(checkUnBinding.get(b).getProgram_id());
					updateMethod2(input1);
				}
			}
		}
		
		input.setCount(input1.getCount());
		if (input.getFlag().equalsIgnoreCase("PW"))
		{
			getSqlMapClientTemplate().update("ccaint.updateMarksEntryPanelforInterview",input);
		}
		else if (input.getFlag().equalsIgnoreCase("CA"))
		{
			getSqlMapClientTemplate().update("ccaint.updateMarksEntryPanelforCCA",input);
		}
		
		
		
		//List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getUpdatedStudentRecord",input);
		if (checkUnBinding.size()==0 && checkBinding.size()==0)
		{
			
		}
		else
		{
			//checkRecord.add("update");
		}
		
	}
		if (input.getFlag().equalsIgnoreCase("PW"))
		{
			checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getprocessedRecordInterview",input);
		}
		else if (input.getFlag().equalsIgnoreCase("CA"))
		{
			checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getprocessedRecordCCA",input);
		}
		
		//List<cca_intBean> output=null;
		//output.add(input1.getCount());
		return checkRecord;
	}
	
	
	
	public List<String> doActionForBulk_GD(cca_intBean input) {
		
		List<cca_intBean> ApplicationsList = new ArrayList<cca_intBean>();
		List<String> checkRecord=new ArrayList<String>();
		
		if (input.getFlag().equalsIgnoreCase("GD"))
		{
			ApplicationsList=getSqlMapClientTemplate().queryForList("ccaint.getApplicationsListForGD");
		}
	
		
		for(int e=0;e<ApplicationsList.size();e++)
		{
			input.setApplication_number(ApplicationsList.get(e).getApplication_number());
			input.setMarks(ApplicationsList.get(e).getMarks());
			input.setCreator("EXCEL");
		
		List<cca_intBean> checkBinding=getSqlMapClientTemplate().queryForList("ccaint.getBindedApplication_GD",input);
		List<cca_intBean> getsessionDate=getSqlMapClientTemplate().queryForList("ccaint.getSession");
		List<cca_intBean> checkUnBinding=new ArrayList<cca_intBean>();
		
		
		cca_intBean input1 = new cca_intBean();
		input1.setStart_date(getsessionDate.get(0).getStart_date());
		input1.setEnd_date(getsessionDate.get(0).getEnd_date());
		if (checkBinding.size()>0) // for binded applications 
		{
			input1.setCount(String.valueOf(checkBinding.size()));
			for (int j=0;j<checkBinding.size();j++)
			{
				
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setApplication_number(checkBinding.get(j).getApp_number());
				//input1.setInterview(input.getInterview());
				input1.setEntity_id(checkBinding.get(j).getEntity_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setMarks(input.getMarks());
				input1.setFlag(input.getFlag());
				input1.setCreator(input.getCreator());
				updateMethod2(input1);
			}
		}
		else
		{
		
			checkUnBinding=getSqlMapClientTemplate().queryForList("ccaint.getUnBindedApplication_GD",input);	
			
		
			if (checkUnBinding.size()>0)
			{
				input1.setCount(String.valueOf(checkUnBinding.size()));
				for (int b=0 ; b<checkUnBinding.size();b++)
				{
					input1.setRegistration_number(checkUnBinding.get(b).getRegistration_number());
					input1.setApplication_number(checkUnBinding.get(b).getApp_number());
					input1.setMarks(input.getMarks());
					input1.setFlag(input.getFlag());
					input1.setCreator(input.getCreator());
					//input1.setInterview(input.getInterview());
					input1.setEntity_id(checkUnBinding.get(b).getEntity_id());
					input1.setProgram_id(checkUnBinding.get(b).getProgram_id());
					updateMethod2(input1);
				}
			}
		}
		
		input.setCount(input1.getCount());
		if (input.getFlag().equalsIgnoreCase("GD"))
		{
			getSqlMapClientTemplate().update("ccaint.updateMarksEntryPanelforGD",input);
		}
		
		
		
		//List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getUpdatedStudentRecord",input);
		if (checkUnBinding.size()==0 && checkBinding.size()==0)
		{
			
		}
		else
		{
			//checkRecord.add("update");
		}
		
	}
		if (input.getFlag().equalsIgnoreCase("GD"))
		{
			checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getprocessedRecordGD",input);
		}
		
		//List<cca_intBean> output=null;
		//output.add(input1.getCount());
		return checkRecord;
	}
	
	
	
	
	

	public List<String> doAction(cca_intBean input) {
		List<cca_intBean> checkBinding=getSqlMapClientTemplate().queryForList("ccaint.getBindedApplication",input);
		List<cca_intBean> getsessionDate=getSqlMapClientTemplate().queryForList("ccaint.getSession",input);
		List<cca_intBean> checkUnBinding=new ArrayList<cca_intBean>();
		List<String> checkRecord=new ArrayList<String>();
		
		System.out.println("getBindedApplication"+checkBinding.size());
		cca_intBean input1 = new cca_intBean();
		input1.setStart_date(getsessionDate.get(0).getStart_date());
		input1.setEnd_date(getsessionDate.get(0).getEnd_date());
		if (checkBinding.size()>0) // for binded applications 
		{
			input1.setCount(String.valueOf(checkBinding.size()));
			for (int j=0;j<checkBinding.size();j++)
			{
				
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setApplication_number(checkBinding.get(j).getApp_number());
				//input1.setInterview(input.getInterview());
				input1.setEntity_id(checkBinding.get(j).getEntity_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setMarks(input.getMarks());
				input1.setFlag(input.getFlag());
				input1.setCreator(input.getCreator());
				updateMethod(input1);
			}
		}
		else
		{
			System.out.println(input.getApplication_number());
			checkUnBinding=getSqlMapClientTemplate().queryForList("ccaint.getUnBindedApplication",input);	
			
			System.out.println("checkUnBinding"+checkUnBinding.size());
			if (checkUnBinding.size()>0)
			{
				input1.setCount(String.valueOf(checkUnBinding.size()));
				for (int b=0 ; b<checkUnBinding.size();b++)
				{
					input1.setRegistration_number(checkUnBinding.get(b).getRegistration_number());
					input1.setApplication_number(checkUnBinding.get(b).getApp_number());
					input1.setMarks(input.getMarks());
					input1.setFlag(input.getFlag());
					input1.setCreator(input.getCreator());
					//input1.setInterview(input.getInterview());
					input1.setEntity_id(checkUnBinding.get(b).getEntity_id());
					input1.setProgram_id(checkUnBinding.get(b).getProgram_id());
					updateMethod(input1);
				}
			}
		}
		
		//List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getUpdatedStudentRecord",input);
		if (checkUnBinding.size()==0 && checkBinding.size()==0)
		{
			
		}
		else
		{
			checkRecord.add("update");
		}
		
		//List<cca_intBean> output=null;
		//output.add(input1.getCount());
		return checkRecord;
	}
	private void updateMethod(cca_intBean input)
	{
		List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getrecordstoinsert",input);	
	  
		System.out.println("checkRecord-"+checkRecord.size()+" app "+input.getApplication_number()+"--"+input.getRegistration_number()+
				"marks"+input.getMarks()+"-"+input.getFlag()+"-"+input.getCreator()+"--"+input.getProgram_id());
		
	   if (checkRecord.size()==1 )
	   {
		  getSqlMapClientTemplate().update("ccaint.updateRecord",input);
	   }
	   else if(checkRecord.size()==0)
	   {
		  getSqlMapClientTemplate().insert("ccaint.insertRecord",input);
	   }
	}

	
	private void updateMethod2(cca_intBean input)
	{
		List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getrecordstoinsert",input);	
	  
		System.out.println("checkRecord-"+checkRecord.size()+" app "+input.getApplication_number()+"--"+input.getRegistration_number()+
				"marks"+input.getMarks()+"-"+input.getFlag()+"-"+input.getCreator()+"--"+input.getProgram_id());
		
	   if (checkRecord.size()==1 )
	   {
		  getSqlMapClientTemplate().update("ccaint.updateRecord",input);
		  getSqlMapClientTemplate().update("ccaint.updateRecord1",input);
	   }
	   else if(checkRecord.size()==0)
	   {
		  getSqlMapClientTemplate().insert("ccaint.insertRecord",input);
	   }
	}
	
	
	
	
	public List<cca_intBean> updatedStudentRecord(cca_intBean input) {

		List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getUpdatedStudentRecord",input);
		return checkRecord;
	}

	public List<cca_intBean> getStudentData(cca_intBean input) {
		
		List<cca_intBean> checkRecord=getSqlMapClientTemplate().queryForList("ccaint.getStudentRecordtoView",input);
		return checkRecord;
	}

	public List<String> EditRecord(cca_intBean input) {
		
		List<cca_intBean> checkBinding=getSqlMapClientTemplate().queryForList("ccaint.getBindedApplication",input);
		List<cca_intBean> getsessionDate=getSqlMapClientTemplate().queryForList("ccaint.getSession",input);
		List<cca_intBean> checkUnBinding=new ArrayList<cca_intBean>();
		List<String> checkRecord=new ArrayList<String>();
		
		System.out.println("getBindedApplication"+checkBinding.size());
		cca_intBean input1 = new cca_intBean();
		input1.setStart_date(getsessionDate.get(0).getStart_date());
		input1.setEnd_date(getsessionDate.get(0).getEnd_date());
		if (checkBinding.size()>0) // for binded applications 
		{
			input1.setCount(String.valueOf(checkBinding.size()));
			for (int j=0;j<checkBinding.size();j++)
			{
				
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setApplication_number(checkBinding.get(j).getApp_number());
				//input1.setInterview(input.getInterview());
				input1.setEntity_id(checkBinding.get(j).getEntity_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setMarks(input.getMarks());
				input1.setFlag(input.getFlag());
				input1.setCreator(input.getCreator());
				EditMethod(input1);
			}
		}
		else
		{
			System.out.println(input.getApplication_number());
			checkUnBinding=getSqlMapClientTemplate().queryForList("ccaint.getUnBindedApplication",input);	
			
			System.out.println("checkUnBinding"+checkUnBinding.size());
			if (checkUnBinding.size()>0)
			{
				input1.setCount(String.valueOf(checkUnBinding.size()));
				for (int b=0 ; b<checkUnBinding.size();b++)
				{
					input1.setRegistration_number(checkUnBinding.get(b).getRegistration_number());
					input1.setApplication_number(checkUnBinding.get(b).getApp_number());
					input1.setMarks(input.getMarks());
					input1.setFlag(input.getFlag());
					input1.setCreator(input.getCreator());
					//input1.setInterview(input.getInterview());
					input1.setEntity_id(checkUnBinding.get(b).getEntity_id());
					input1.setProgram_id(checkUnBinding.get(b).getProgram_id());
					EditMethod(input1);
				}
			}
		}
		if (checkUnBinding.size()==0 && checkBinding.size()==0)
		{
			
		}
		else
		{
			checkRecord.add("update");
		}
		
		return checkRecord;
	}
	
	
	private void EditMethod(cca_intBean input)
	{
		  getSqlMapClientTemplate().update("ccaint.EditRecord",input);
	}

	
	public List<cca_intBean> checkApplicationStatus(cca_intBean input) {
		List<cca_intBean> Info = getSqlMapClientTemplate().queryForList("ccaint.getApplicationStatus",input);
		return Info;
	}
	
	public List<cca_intBean> checkCallListStatus(cca_intBean input) {
		List<cca_intBean> Info = getSqlMapClientTemplate().queryForList("ccaint.getCallList",input);
		return Info;
	}

	public List<cca_intBean> checkEligibilityStatus(cca_intBean input) {
		List<cca_intBean> Info = getSqlMapClientTemplate().queryForList("ccaint.getCalledStatus",input);
		return Info;
	}

	
	public List<cca_intBean> InterviewStatus(cca_intBean input) {
		List<cca_intBean> Info = getSqlMapClientTemplate().queryForList("ccaint.getInterviewStatus",input);
		return Info;
	}

	
	public List<cca_intBean> checkfinalMeritListStatus(cca_intBean input) {
		List<cca_intBean> Info = getSqlMapClientTemplate().queryForList("ccaint.getMeritStatus",input);
		return Info;
	}

	public List<statusBean> uploadMarks(statusBean bean, ServletContext sc) {
		
		WritableWorkbook writableWorkbook;
		Workbook workbook;
		try
		{
			System.out.println("PID-"+bean.getProgram_id());
			workbook = Workbook.getWorkbook(new File(sc.getRealPath(File.separator)+"DOCS"+(File.separator)+"EnteranceTestMarks"+File.separator+bean.getProgram_id()+".xls")); // read from this file 
			//workbook = Workbook.getWorkbook(new File("D:/ADMISSION/ADM2019/ENTRANCE/MBA_GD/"+program_id+".xls")); // read from this file
			Sheet sheet = workbook.getSheet("Sheet1");
			
			for (int i=1 ; i<sheet.getRows(); i++)
			{
				if ((sheet.getCell(1, i).getContents() == "")|| (sheet.getCell(1, i).getContents() == null)) 
				{
					continue;
				}
				//System.out.println(i+1+"-"+sheet.getCell(1, i).getContents()); //application
				//System.out.println(i+2+"-"+sheet.getCell(6, i).getContents()); //cca
				//System.out.println(i+3+"-"+sheet.getCell(5, i).getContents()); //interview
				
				
				bean.setApplication_number(sheet.getCell(1, i).getContents());
				//bean.setProgram_id(program_id);
				bean.setExam_center("ALL");
		
				bean.setMarks(sheet.getCell(2, i).getContents());
				
				System.out.println(i+"-"+bean.getApplication_number()+"-"+bean.getMarks()+"-"+bean.getProgram_id());
				List<statusBean> check= new ArrayList<statusBean>();
				List<statusBean> check1= new ArrayList<statusBean>() ;
				check= getSqlMapClientTemplate().queryForList("admStatus.checkETMarks", bean);
				//System.out.println("YY"+check.get(0).getMarks());
				if (check.size()>0)
				{
						check1=getSqlMapClientTemplate().queryForList("admStatus.checkETMarksClone", bean);
						if (check1.size()>0)
						{
							System.out.println("Duplicate--------------------------------"+check1.get(0).getApplication_number()+"--marks--"+check1.get(0).getMarks());
							
						}
						else
						{
							getSqlMapClientTemplate().insert("admStatus.InsertETMarksClone",bean);
						}
					
				}
				else if (check.size()==0)
				{
					getSqlMapClientTemplate().insert("admStatus.InsertETMarks",bean);
					System.out.println("insert record -------------------------------------- "+bean.getApplication_number());
				}
				else
				{
					System.out.println("----------------------------------Error Error -------------------------------------------");
				}
				
			}
			getSqlMapClientTemplate().update("admStatus.updateETworngNumber",bean);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		List<statusBean> ETCOunt=getSqlMapClientTemplate().queryForList("admStatus.getETCount",bean);
		return ETCOunt;
	}

	public List<statusBean> updateETmarks(statusBean input) {
		
		List<statusBean> viewList = new ArrayList<statusBean>();
		if (input.getFlag().equalsIgnoreCase("UPDATE"))
		{
			getSqlMapClientTemplate().update("admStatus.updateETMarks001",input);
			getSqlMapClientTemplate().update("admStatus.updateETworngNumber",input);
			viewList=getSqlMapClientTemplate().queryForList("admStatus.viewETmarks",input);
		}
		else if (input.getFlag().equalsIgnoreCase("insert"))
		{
			getSqlMapClientTemplate().insert("admStatus.insertETMarks001",input);
			getSqlMapClientTemplate().update("admStatus.updateETworngNumber",input);
			viewList=getSqlMapClientTemplate().queryForList("admStatus.viewETmarks",input);
		}
		else if (input.getFlag().equalsIgnoreCase("viewETMarks"))
		{
		 viewList=getSqlMapClientTemplate().queryForList("admStatus.viewETmarks",input);
		
		}
		return viewList;
	}



}
