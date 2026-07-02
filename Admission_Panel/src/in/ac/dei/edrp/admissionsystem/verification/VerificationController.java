package in.ac.dei.edrp.admissionsystem.verification;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
		
	
	//user and menu added by Pragya on 26 Jun 2025
	public ModelAndView validateiwlist(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray marksarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		String user = request.getParameter("user");
	    String menu = request.getParameter("menu");
		
		studentBean sbean = new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setUser_id(user);
		    sbean.setMenu(menu); 
		
		
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
	public ModelAndView getSignature(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray marksarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		String  user = request.getParameter("user");
		studentBean sbean =new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setUser_id(user);
		String rootPath = getServletContext().getRealPath("/");
		sbean.setTargetPath(rootPath);
		System.out.println("rootPath:"+rootPath);
				
	   	    BufferedImage img = vfyDao.VerifySignature(sbean);
	   	    
	   	 // Set the content type
	   	    response.setContentType("image/bmp");

	   	    // Write image to response output stream
	   	    OutputStream out = response.getOutputStream();
	   	 boolean success =ImageIO.write(img, "bmp", out);
	   	System.out.println("ImageIO success: " + success);
	   	    response.getOutputStream().flush();
	   	    out.close();
	   	 //JSONObject jsonObject = new JSONObject();
		   //	jsonObject.put("status","success");
		   	//marksarray.add(jsonObject);
	   	    
	  return null;
	    //return new ModelAndView("CreateCourse/hello", "message", marksarray.toString());       
			}

	public ModelAndView getApplicantPrograms(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray pgmarray = new JSONArray();
		
		String  application_number = request.getParameter("application_number");
		String  user = request.getParameter("user");
		studentBean sbean =new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setUser_id(user);
		
		
	   	    List <studentBean> ProgramList =vfyDao.getApplicantPrograms(sbean);
	
	   	    for  (studentBean pglist:ProgramList) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("programId", pglist.getProgramId());
	   	    	jsonObject.put("programName", pglist.getProgram_name());
	   	    	
	   	    	pgmarray.add(jsonObject);
	   	    	
	   	    }
	   	    
	  
	    return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());       
			}
	
	public ModelAndView getUserPrograms(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray pgmarray = new JSONArray();
		
		String  userId = request.getParameter("userId");
		String  component = request.getParameter("component");
		studentBean sbean =new studentBean();
		
		//sbean.setApplication_number(userId);
		sbean.setUser_id(userId);
		sbean.setComponentID(component);
		
		
	   	    List <studentBean> ProgramList =vfyDao.getUserPrograms(sbean);
	
	   	    for  (studentBean pglist:ProgramList) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("programId", pglist.getProgramId());
	   	    	jsonObject.put("programName", pglist.getProgram_name());
	   	    	
	   	    	pgmarray.add(jsonObject);
	   	    	
	   	    }
	   	    
	  
	    return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());       
			}

	
	public ModelAndView validateInterview(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		JSONArray pgmarray = new JSONArray();
		Gson gson= new Gson();
		
		
		
		String  programid = request.getParameter("programid");
		String  appno = request.getParameter("appno");
		String  comp = request.getParameter("comp");
		studentBean sbean =new studentBean();
		//sbean.setApplication_number(userId);
		sbean.setProgramId(programid);
		sbean.setApplication_number(appno);
		sbean.setComponentID(comp);
		String comonentdesc = comp.equalsIgnoreCase("PW")?"Interview":"CCA";
		
		int currentYear = LocalDate.now().getYear();

        String startDate = currentYear + "-07-01";
        sbean.setStartdate(startDate);
		
        // check attendance for Interview only for PW component
		
					
	   	    List <studentBean> attendanceList =vfyDao.getAttendance(sbean);
	   	    if(attendanceList.size()==0) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("status", false);
	   	    	jsonObject.put("message", "Attendance not marked");
	   	    	pgmarray.add(jsonObject);
	   	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString()); 
	   	    }else {
	   	    	if(!attendanceList.get(0).getVerificationStatusCode().equalsIgnoreCase("VERIFIED"))	
	   	    	{
	   	    		JSONObject jsonObject = new JSONObject();
	   	    		jsonObject.put("status", false);
		   	    	jsonObject.put("message", "Attendance is rejected");
		   	    	pgmarray.add(jsonObject);
		   	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());
	   	    	}
	   	    }
	   	    	
		
	   	    // check if component  marks are already entered
	   	 List <studentBean> iwmarks =vfyDao.checkComponentmarks(sbean);
	   	  if(iwmarks.size()>0) {
	    	JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("status", false);
   	    	jsonObject.put("message", "Marks are already entered");
   	    	pgmarray.add(jsonObject);
   	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());
	    }
	   	  
	   	  // check if interview level is defined
	   	List <studentBean> iwlevellist =vfyDao.getInterviewLevel(sbean);
	   	if (iwlevellist == null
	   	        || iwlevellist.isEmpty()
	   	        || iwlevellist.get(0).getCategory() == null
	   	        || iwlevellist.get(0).getCategory().trim().isEmpty()){
	   		    	JSONObject jsonObject = new JSONObject();
		    		jsonObject.put("status", false);
	   	    	jsonObject.put("message", "Please contact  EdRP,Interview level mising for program:"+programid );
	   	    	pgmarray.add(jsonObject);
	   	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());
	   		    }
	   	    
	   	  // check if applicant is called for interview
	   	List <studentBean> iwList =vfyDao.checkIWcalled(sbean);
	    if(iwList.size()==0) {
	    	JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("status", false);
   	    	jsonObject.put("message", "Candidate is not called for interview");
   	    	pgmarray.add(jsonObject);
   	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());
	    }else {
	    	JSONObject jsonObject = new JSONObject();
	    	
    		jsonObject.put("status", true);
	    	jsonObject.put("message", "Success");
	    	jsonObject.put("firstname", iwList.get(0).getFirst_name());
	    	
	    	pgmarray.add(jsonObject);
	    	return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());
	    	
	    }
   	    	
	   	 
	   	    
	  
	         
			}
	                    
	public ModelAndView getEnteredCandidates(HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Gson gson= new Gson();
		
		JSONArray pgmarray = new JSONArray();
		
		String  userId = request.getParameter("userId");
		String  component = request.getParameter("component");

		String  programid = request.getParameter("programId");
		
		
		studentBean sbean =new studentBean();
		
		//sbean.setApplication_number(userId);
		sbean.setUser_id(userId);
		sbean.setComponentID(component);
		sbean.setProgramId(programid);
		
		
	   	    List <studentBean> enteredCanidatesList =vfyDao.getEnteredCandidates(sbean);
	
	   	    for  (studentBean clist:enteredCanidatesList) {
	   	    	JSONObject jsonObject = new JSONObject();
	   	    	jsonObject.put("applicationNo", clist.getApplication_number());
	   	    	jsonObject.put("applicantName", clist.getFirst_name());
	   	    	jsonObject.put("score", clist.getScore());
	   	    	jsonObject.put("dateEntered", clist.getInsert_time());
	   	    	
	   	    	pgmarray.add(jsonObject);
	   	    	
	   	    }
	   	    
	  
	    return new ModelAndView("CreateCourse/hello", "message", pgmarray.toString());       
			}

	
	
}
