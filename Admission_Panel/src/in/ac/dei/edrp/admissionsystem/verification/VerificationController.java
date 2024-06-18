package in.ac.dei.edrp.admissionsystem.verification;

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
		
	   	    List <studentBean> candidatemarks =vfyDao.getAcademicMarks(application_number);
	
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
		studentBean sbean = new studentBean();
		sbean.setApplication_number(application_number);
		sbean.setVerificationStatusCode(code);
		sbean.setVerificationStatusDesc(reason);
		
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

		
		
		
	

}
