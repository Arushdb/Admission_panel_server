package in.ac.dei.edrp.admissionsystem.studentModule;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.ReportInfoGetter;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.transferBean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import com.google.gson.Gson;

public class studentController extends MultiActionController  {

private studentDao studentDao;
	
	public void setStudentDao(studentDao studentDao)
	{
		this.studentDao=studentDao;
	}
	
	
	public ModelAndView getCustomeData(HttpServletRequest request, HttpServletResponse responce)
	{
		System.out.println("hello");
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	    
	    System.out.println("ec-"+input1.getEmployee_code());
	    System.out.println("entity-"+input1.getEntity_id());
	    System.out.println("pid-"+input1.getProgram_id());
	    System.out.println("flag-"+input1.getFlag());
	    System.out.println("appli-"+input1.getApplication_number());
	    
	    
	   // System.out.println("end-"+input1.getEnd_date());
		
	    List<admissionBean> dataList =studentDao.getCustomeGridData(input1);
	    
	   System.out.println(dataList.size());
	   

	    
	    
	    if (dataList.size()>0)
	    {
	    	
	    	for(int i=0;i<dataList.size();i++)
	    	{
	    		JSONObject jsonObject = new JSONObject();
	    		
	    		jsonObject.put("val1", dataList.get(i).getVal1());
		    	jsonObject.put("val2", dataList.get(i).getVal2());
		    	jsonObject.put("val3", dataList.get(i).getVal3());
		    	jsonObject.put("val4", dataList.get(i).getVal4());
		    	jsonObject.put("val5", dataList.get(i).getVal5());
		    	jsonObject.put("val6", dataList.get(i).getVal6());
		    	jsonObject.put("val7", dataList.get(i).getVal7());
		    	jsonObject.put("val8", dataList.get(i).getVal8());
		    	jsonObject.put("val9", dataList.get(i).getVal9());
		    	jsonObject.put("val10", dataList.get(i).getVal10());
		    	jsonObject.put("val11", dataList.get(i).getVal11());
		    	jsonObject.put("val12", dataList.get(i).getVal12());
		    	jsonObject.put("val13", dataList.get(i).getVal13());
		    	jsonObject.put("val14", dataList.get(i).getVal14());
		    	//jsonObject.put("update_status", "OK");
		    	array1.add(jsonObject);
	    	}


	    }
	    else if (dataList.size()==0)
	    {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("val1", "NOTFOUND");
	    	jsonObject.put("val2","NOTFOUND");
	    	array1.add(jsonObject);
	    }
	    
	    
	    System.out.println(array1.toString());
	    
	    
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	
	public void generateSyllabusPdf(HttpServletRequest request, HttpServletResponse responce) throws IOException
	{
		
		responce.setHeader("Content-Disposition", "");
		
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
		System.out.println("w1");
	    input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class); // to set Client side data into Bean
	    
	    System.out.println("don-1-"+input1.getCourse_code());
	    
	    String path =studentDao.generateCourseDetailPdf(input1,this.getServletContext());
	    
	    JSONObject abc1 = new JSONObject();
	    contentofFile(path);
	    responce.getOutputStream().write(contentofFile(path));
		abc1.put("path", path);
			
		array1.add(abc1);
		
	    System.out.println("----------------"+array1.toString());
	    
		//return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
		
	private byte[] contentofFile(String path) throws IOException
	{
		 File file = new File(path);
		 byte[] bytesArray = new byte[(int) file.length()]; 
		 FileInputStream fis = new FileInputStream(file);
		 fis.read(bytesArray); //read file into bytes[]
		  fis.close();
		return bytesArray;
	}
	
	
}
