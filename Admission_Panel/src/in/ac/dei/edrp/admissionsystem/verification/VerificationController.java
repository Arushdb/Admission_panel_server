package in.ac.dei.edrp.admissionsystem.verification;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.studentBean;
import in.ac.dei.edrp.admissionsystem.ccaEntry.ccaDao;

public class VerificationController extends MultiActionController{
	
private VerifyDao vfyDao;

	
	public void setVfyDao(VerifyDao vfyDao) {
	this.vfyDao = vfyDao;
}

	
	
	@SuppressWarnings("unchecked")
	public ModelAndView getapplicantmarks(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray marksarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		String  user = request.getParameter("user");
		studentBean sbean =new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setUser_id(user);
		
		
	   	    List <studentBean> candidatemarks =vfyDao.getAcademicMarks(sbean);
	
	   	    for  (studentBean candidate:candidatemarks) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("application_number", candidate.getApplication_number());
	   	    	jsonObject.put("first_name", candidate.getFirst_name());
	   	    	jsonObject.put("marksPercentage", candidate.getMarksPercentage());
	   	    	jsonObject.put("marksObtained", candidate.getMarksObtained());
	   	    	jsonObject.put("totalMarks", candidate.getTotalMarks());
	   	    	jsonObject.put("score", candidate.getScore());
	   	    	jsonObject.put("componentID", candidate.getComponentID());
	   	    	jsonObject.put("verificationStatusCode", candidate.getVerificationStatusCode());
	   	    	jsonObject.put("verificationStatusDesc", candidate.getVerificationStatusDesc());
	   	    	marksarray.add(jsonObject);
	   	    	
	   	    }
	   	    
	  
	    return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
			}

	@SuppressWarnings("unchecked")
	public ModelAndView updatestatus(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray marksarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		String  reason = request.getParameter("verificationStatusDesc");
		String  code = request.getParameter("verificationStatusCode");
		String  user = request.getParameter("user");
		studentBean sbean = new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setVerificationStatusCode(code);
		sbean.setVerificationStatusDesc(reason);
		sbean.setUser_id(user);
		
	   	    int count =vfyDao.updatestatus(sbean);
	   	 JSONObject jsonObject = new JSONObject();
	   	jsonObject.put("count",count);
	   	marksarray.add(jsonObject);
	   	    	
	   	    return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
			}
		
	
	
	public ModelAndView validateiwlist(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray marksarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		
		studentBean sbean = new studentBean();
		sbean.setApplication_number(application_number);
		
		
	   	    int count =vfyDao.validateIWlist(sbean);
	   	 JSONObject jsonObject = new JSONObject();
	   	jsonObject.put("count",count);
	   	marksarray.add(jsonObject);
	   	    	
	   	    return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
			}

	//loadUserProgramList added by Jyoti on 19 Jun 2025
	public ModelAndView loadUserProgramCombo(HttpServletRequest request, HttpServletResponse response) 
	{
		String  user = request.getParameter("user");
		System.out.println("inputs: user=" + user);
		JSONArray prglistarray = new JSONArray();
		List <studentBean> vfyPrglist = vfyDao.getUserProgramList(user);
		System.out.println("list size=" + vfyPrglist.size());
		if (vfyPrglist.size() > 0 )
		{
			for  (studentBean std:vfyPrglist) {
				System.out.println(std.getProgramId() + "-" + std.getProgram_name());
			    	JSONObject jsonObject = new JSONObject();
			    	jsonObject.put("program_id", std.getProgramId() );
			    	jsonObject.put("program_name", std.getProgram_name() );
			    	prglistarray.add(jsonObject);    	
			}
		}
		else
		{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("program_id", "0");
			jsonObj.put("program_name", "No data found");
			prglistarray.add(jsonObj);
		}
		return new ModelAndView("CreateCourse/hello", "message", prglistarray.toString());
	}

	//loadVfyProgramList added by Jyoti on 19 Jun 2025
	public  ModelAndView loadVfyProgramList(HttpServletRequest request, HttpServletResponse response)
	{
		Gson gson= new Gson();
		JSONArray prglistarray = new JSONArray();
		String  programId = request.getParameter("program_id");
		String  listno = request.getParameter("list_num");
		String  user = request.getParameter("user");
		System.out.println("inputs: program_id=" + programId + " listno=" + listno + " user=" + user);
		studentBean sbean =new studentBean();
		sbean.setProgramId(programId);
		sbean.setUser_id(user);
		sbean.setListNum(listno);
	   	List <studentBean> vfyPrglist =vfyDao.getVfyProgramList(sbean);
	
	   	for  (studentBean std:vfyPrglist) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("vfy_status", std.getVerificationStatusCode());
	   	    	jsonObject.put("application_number", std.getApplication_number());
	   	    	jsonObject.put("first_name", std.getFirst_name());
	   	    	jsonObject.put("category", std.getCategory());
	   	    	jsonObject.put("gender", std.getGender());
	   	    	jsonObject.put("verifiedby", std.getEmail() );
	   	    	jsonObject.put("verifiedon", std.getInsert_time());
	   	    	prglistarray.add(jsonObject);    	
	   	}
	   	
	    return new ModelAndView("CreateCourse/hello", "message", prglistarray.toString());       
	
	}
	
		//	checklistStatus added by Jyoti on 21 Jun 2025
		@SuppressWarnings("unchecked")
		public ModelAndView chkListStatus(HttpServletRequest request, HttpServletResponse response)throws Exception
		{
			String  programId = request.getParameter("program_id");
			String  listno = request.getParameter("list_num");
			String  user = request.getParameter("user");
			System.out.println("check list status inputs: program_id=" + programId + " listno=" + listno + " user=" + user);
			studentBean sbean =new studentBean();
			sbean.setProgramId(programId);
			sbean.setUser_id(user);
			sbean.setListNum(listno);
			JSONArray marksarray = new JSONArray();
			
		   	//int count =vfyDao.chkliststatus(sbean);
			List <studentBean> vfySt =  vfyDao.chkliststatus(sbean);
			for  (studentBean vfy:vfySt) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("status", "VERIFIED");
	   	    	jsonObject.put("verifiedby", vfy.getEmail() );
	   	    	jsonObject.put("verifiedon", vfy.getInsert_time());
	   	    	marksarray.add(jsonObject);    	
	   	    	System.out.println(vfy.getEmail() + "-" + vfy.getInsert_time());
			}
	   	 
		   	if (vfySt.size() == 0) {
		   		JSONObject obj = new JSONObject();
		   		obj.put("status", "PENDING");
		   		obj.put("verifiedby", "");
		   		obj.put("verifiedon", "");
		   		marksarray.add(obj);
		   	}
		   	
		    return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
		}
	
	//setPrglistVfyStatus added by Jyoti on 21 Jun 2025
	@SuppressWarnings("unchecked")
	public ModelAndView setPrglistVfyStatus(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		String  programId = request.getParameter("program_id");
		String  listno = request.getParameter("list_num");
		String  user = request.getParameter("user");
		System.out.println("update list status inputs: program_id=" + programId + " listno=" + listno + " user=" + user);
		studentBean sbean =new studentBean();
		sbean.setProgramId(programId);
		sbean.setUser_id(user);
		sbean.setListNum(listno);
		sbean.setVerificationStatusCode("VFY"); // verified.
		JSONArray marksarray = new JSONArray();
	   	int count =vfyDao.updateliststatus(sbean);
	   	JSONObject jsonObject = new JSONObject();
	   	jsonObject.put("count",count);
	   	marksarray.add(jsonObject);
	   	    	
	    return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
	}
}
