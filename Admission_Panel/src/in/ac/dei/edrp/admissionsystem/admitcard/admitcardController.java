package in.ac.dei.edrp.admissionsystem.admitcard;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;




import com.google.gson.Gson;

public class admitcardController extends MultiActionController 
{
	private admitcardDao admitcardDao;
	
	public void setAdmitcardDao(admitcardDao admitcardDao)
	{
		this.admitcardDao=admitcardDao;
	}

	public ModelAndView generateAdmitCard(HttpServletRequest request, HttpServletResponse responce)
	{
		
		Gson gson= new Gson();
		GenerateAdmitCardBean input1 = new GenerateAdmitCardBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), GenerateAdmitCardBean.class);
	    
	    System.out.println("pid-"+input1.getProgram_id());
	    System.out.println("entity-"+input1.getEntity_id());
	    System.out.println("start-"+input1.getStart_date());
	    System.out.println("end-"+input1.getEnd_date());
		
	    String val =admitcardDao.generateAdmitCard(input1,this.getServletContext());
	    
	    //System.out.println(dataList.size());
	    JSONObject jsonObject = new JSONObject();
	jsonObject.put("status", "OK");
	jsonObject.put("flag", val);
	    
//	    if (dataList.size()>0)
//	    {
//
////				jsonObject.put("status", "OK");
////		    	jsonObject.put("user_id", dataList.get(0).getUser_id());
////		    	jsonObject.put("employee_code", dataList.get(0).getEmployee_code());
////		    	jsonObject.put("panel_authority", dataList.get(0).getPanel_authority());
//
//	    }
//	    else if (dataList.size()==0)
//	    {
//	    	jsonObject.put("status", "NOTFOUND");
//	    	jsonObject.put("user_id","NOTFOUND");
//	    }
	    
	    array1.add(jsonObject);
	    System.out.println(array1.toString());
	    
	    
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	
	
}
