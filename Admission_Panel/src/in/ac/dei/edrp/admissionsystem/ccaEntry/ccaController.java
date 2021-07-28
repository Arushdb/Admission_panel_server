package in.ac.dei.edrp.admissionsystem.ccaEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.statusBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.CellType;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;


public class ccaController extends MultiActionController{

private ccaDao ccaDao;
	
	public void setCcaDao(ccaDao ccaDao)
	{
		this.ccaDao=ccaDao;
	}
	
	public ModelAndView viewData(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
	    List <cca_intBean> CandidateInfo =ccaDao.getCandidateInfo(input1);
	    List <cca_intBean> marksInfo =ccaDao.getMarksInfo(input1);	
	    System.out.println("CandidateInfo-"+CandidateInfo.size());
	    if(CandidateInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("application_number", CandidateInfo.get(0).getApplication_number());
	    	jsonObject.put("first_name", CandidateInfo.get(0).getCandidateName());
	    	jsonObject.put("father_first_name", CandidateInfo.get(0).getFatherName());
	    	jsonObject.put("gender", CandidateInfo.get(0).getGender());
	    	jsonObject.put("category", CandidateInfo.get(0).getCategory());
	    	
	    	if(marksInfo.size()>0)
	    	{
	    		jsonObject.put("marks_status", "P");
	    	}
	    	else
	    	{
	    		jsonObject.put("marks_status", "A");
	    	}
	    	array1.add(jsonObject);
	    }
	    //view record
	    // check marks are submitted or not
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString());       
			}
	
	
	
	public ModelAndView viewDataForArbitration(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
	    List <cca_intBean> CandidateInfo =ccaDao.getCandidateInfo(input1);
	    
	    
	    List <cca_intBean> checkApplicationStatus =ccaDao.checkApplicationStatus(input1);
	    
	    if (checkApplicationStatus.size()>0)
	    {
	    	List <cca_intBean> checkCallListStatus =ccaDao.checkCallListStatus(input1);
	    	if (checkCallListStatus.size()>0)
	    	{
	    		List <cca_intBean> checkEligibilityStatus =ccaDao.checkEligibilityStatus(input1);
	    		if (checkEligibilityStatus.size()>0 && checkEligibilityStatus.get(0).getCalled().equalsIgnoreCase("Y")) // and Y
	    		{
	    			 List <cca_intBean> InterviewStatus =ccaDao.InterviewStatus(input1);
	    			 
	    			 if (InterviewStatus.size()>0)
	    			 {
	    				 List <cca_intBean> checkfinalMeritListStatus =ccaDao.checkfinalMeritListStatus(input1);
	    				 
	    				 if (checkfinalMeritListStatus.size()>0)
	    				 {
	    					    input1.setReason_code("Eligible");
	    		    			input1.setSub_status("OK");
	    				 }
	    				 else
	    				 {
	    					    input1.setReason_code("Merit Not Processed");
	    		    			input1.setSub_status("InEligible");
	    				 }
	    			 }
	    			 else
	    			 {
	    				    input1.setReason_code("Absent In Interview");
	    	    			input1.setSub_status("InEligible");
	    			 }
	    		}
	    		else if (checkEligibilityStatus.size()>0 && checkEligibilityStatus.get(0).getCalled().equalsIgnoreCase("N") ) // and N
	    		{
	    			// reason code
	    			input1.setReason_code(checkEligibilityStatus.get(0).getReason_code());
	    			input1.setSub_status("InEligible");
	    		}
	    	}
	    	else
	    	{
	    		input1.setReason_code("Academic marks are not Submitted");
    			input1.setSub_status("InEligible");
	    	}
	    }
	    else
	    {
	    	input1.setReason_code("InComplete Application");
			input1.setSub_status("InEligible");
	    }
	    
	    
	    
	    
	   
	    
	    
	    
	    
	    List <cca_intBean> marksInfo =ccaDao.getMarksInfo(input1);	
	    System.out.println("CandidateInfo-"+CandidateInfo.size());
	    if(CandidateInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("application_number", CandidateInfo.get(0).getApplication_number());
	    	jsonObject.put("first_name", CandidateInfo.get(0).getCandidateName());
	    	jsonObject.put("father_first_name", CandidateInfo.get(0).getFatherName());
	    	jsonObject.put("gender", CandidateInfo.get(0).getGender());
	    	jsonObject.put("category", CandidateInfo.get(0).getCategory());
	    	
	    	jsonObject.put("reason_status", input1.getReason_code());
	    	jsonObject.put("eligibility_status", input1.getSub_status());
	    	array1.add(jsonObject);
	    }
	    //view record
	    // check marks are submitted or not
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString());       
			}
	
	
	
	
	public ModelAndView BulkMarksPosting(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		System.out.println("here01");
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
				
	    List <String> marksInfo =ccaDao.doActionForBulk(input1);
	    if(marksInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	//jsonObject.put("application_number", marksInfo.get(0).getRegistration_number());
	    	jsonObject.put("status", "OK");
	    	array1.add(jsonObject);
	    	
	    }
	    else
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("status", "NORECORD");
	    	array1.add(jsonObject);
	    }
	    
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString()); 
		
			}
	
	
	
	public ModelAndView BulkMarksPosting_GD(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		System.out.println("here01");
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
				
	    List <String> marksInfo =ccaDao.doActionForBulk_GD(input1);
	    if(marksInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	//jsonObject.put("application_number", marksInfo.get(0).getRegistration_number());
	    	jsonObject.put("status", "OK");
	    	array1.add(jsonObject);
	    	
	    }
	    else
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("status", "NORECORD");
	    	array1.add(jsonObject);
	    }
	    
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString()); 
		
			}
	
	    public ModelAndView viewDataForCouncelling(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
				Gson gson= new Gson();
				cca_intBean input1 = new cca_intBean();
				JSONArray array1 = new JSONArray();
				
			    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
			    
			    List <cca_intBean> CandidateInfo =ccaDao.getCandidateInfo(input1);
			    List <cca_intBean> coucenllingStatus =ccaDao.getCouncellingStatus(input1);
			    
			    if(CandidateInfo.size()>0)
			    {
			    	JSONObject jsonObject = new JSONObject();
			    	jsonObject.put("application_number", CandidateInfo.get(0).getApplication_number());
			    	jsonObject.put("first_name", CandidateInfo.get(0).getCandidateName());
			    	jsonObject.put("father_first_name", CandidateInfo.get(0).getFatherName());
			    	jsonObject.put("gender", CandidateInfo.get(0).getGender());
			    	jsonObject.put("category", CandidateInfo.get(0).getCategory());
			    	
			    	if (coucenllingStatus.size()>0)
			    	{
			    		jsonObject.put("eligibility_status", coucenllingStatus.get(0).getUpdate_status());
			    		jsonObject.put("meritTotal", coucenllingStatus.get(0).getMarks());
			    		jsonObject.put("registration_number", coucenllingStatus.get(0).getRegistration_number());
			    	}
			    	else
			    	{
			    		jsonObject.put("eligibility_status", "NON");
			    	}
			    	
			    	array1.add(jsonObject);
			    }
		
		        return new ModelAndView("CreateCourse/hello", "message", array1.toString());
			}
	
	
	public ModelAndView DoAction(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
				
	    List <String> marksInfo =ccaDao.doAction(input1);
	    if(marksInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	//jsonObject.put("application_number", marksInfo.get(0).getRegistration_number());
	    	jsonObject.put("update_status", "OK");
	    	array1.add(jsonObject);
	    	
	    }
	    else
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("update_status", "NORECORD");
	    	array1.add(jsonObject);
	    }
	    
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString()); 
		
			}
	
	public ModelAndView EditRecord(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
				//make the records editable
	           
	    List <String> marksInfo =ccaDao.EditRecord(input1);
	    if(marksInfo.size()>0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	//jsonObject.put("application_number", marksInfo.get(0).getRegistration_number());
	    	jsonObject.put("update_status", "OK");
	    	array1.add(jsonObject);
	    	
	    }
	    else
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("update_status", "NORECORD");
	    	array1.add(jsonObject);
	    }
	    
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
			}
	
	
	public ModelAndView viewRecords(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
	    List <cca_intBean> getStudentData =ccaDao.getStudentData(input1);
	    
	    if(getStudentData.size()>0)
	    {
	    
	    	for (int i=0;i<getStudentData.size();i++)
	    	{
	    		JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("application_number", getStudentData.get(i).getApplication_number());
		    	jsonObject.put("first_name", getStudentData.get(i).getCandidateName());
		    	jsonObject.put("category", getStudentData.get(i).getCategory());
		    	jsonObject.put("marks", getStudentData.get(i).getMarks());
		    	//jsonObject.put("update_status", "OK");
		    	array1.add(jsonObject);
	    	}
	    	
	    	
	    }
	    else
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("update_status", "NORECORD");
	    	array1.add(jsonObject);
	    }
				//view inserted marks
	           
	    return new ModelAndView("CreateCourse/hello", "message", array1.toString()); 
		
			}
	
	public ModelAndView generateExcelReport(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		cca_intBean input1 = new cca_intBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), cca_intBean.class);
				//make the records editable
	           
	    return null;
		
			}
	
	
	public ModelAndView UploadET(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		
		
		statusBean bean = new statusBean();
		bean = gson.fromJson(request.getParameter("courseObject"), statusBean.class);
		
		List <statusBean> getStudentData =ccaDao.uploadMarks(bean,this.getServletContext());
		JSONArray array1 = new JSONArray();
		
		if(getStudentData.size()>0)
	    {
	    
	    	
	    		JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("status", getStudentData.get(0).getCount());
		    	
		    	array1.add(jsonObject);
	    	
	    	
	    	
	    }
	
		
		  return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	public ModelAndView EditET(HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		
		Gson gson= new Gson();
		
		
		statusBean bean = new statusBean();
		bean = gson.fromJson(request.getParameter("courseObject"), statusBean.class);
		
		List <statusBean> getStudentData =ccaDao.updateETmarks(bean);
		JSONArray array1 = new JSONArray();
		
		if(getStudentData.size()>0)
	    {
	    
	    	
	    		//JSONObject jsonObject = new JSONObject();
	    		//jsonObject.put("status", getStudentData.get(0).getCount());
		    	
	    		for(int i=0;i<getStudentData.size();i++)
		    	{
		    		JSONObject jsonObject = new JSONObject();
		    		
		    		jsonObject.put("val1", getStudentData.get(i).getProgram_name());
			    	jsonObject.put("val2", getStudentData.get(i).getMarks());
			    	jsonObject.put("val3", getStudentData.get(i).getApplication_number());
			    	jsonObject.put("val4", getStudentData.get(i).getFlag());
			    	//jsonObject.put("update_status", "OK");
			    	array1.add(jsonObject);
		    	}
		    	//array1.add(jsonObject);
	    	
	    	
	    	
	    }
	
		
		  return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	
	
}
