package in.ac.dei.edrp.admissionsystem.computation;



import java.io.File;
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

public class computationController extends MultiActionController  {

private computationDao computationDao;
	
	public void setComputationDao(computationDao computationDao)
	{
		this.computationDao=computationDao;
	}
	
	
	public ModelAndView runComputation(HttpServletRequest request, HttpServletResponse responce)
	{
		System.out.println("hello");
		Gson gson= new Gson();
		ReportInfoGetter input1 = new ReportInfoGetter();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), ReportInfoGetter.class);
	    
	    System.out.println("pid-"+input1.getProgram_id());
	    System.out.println("entity-"+input1.getEntity_id());
	    System.out.println("start-"+input1.getStart_date());
	    System.out.println("end-"+input1.getEnd_date());
	    
	   // System.out.println("end-"+input1.getEnd_date());
		
	    String val =computationDao.runComputation(input1);
	    
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
	
	
	public ModelAndView runComputationForAll(HttpServletRequest request, HttpServletResponse responce)
	{
		System.out.println("hello");
		Gson gson= new Gson();
		ReportInfoGetter input1 = new ReportInfoGetter();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), ReportInfoGetter.class);
	    
	    System.out.println("pid-"+input1.getProgram_id());
	    System.out.println("entity-"+input1.getEntity_id());
	    System.out.println("start-"+input1.getStart_date());
	    System.out.println("end-"+input1.getEnd_date());
	    
	   // System.out.println("end-"+input1.getEnd_date());
		
	    String val =computationDao.runComputationforAll(input1);
	    
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
	
	public ModelAndView meritListProcess(HttpServletRequest request, HttpServletResponse responce)
	{
		System.out.println("hello");
		Gson gson= new Gson();
		ReportInfoGetter input1 = new ReportInfoGetter();
		JSONArray array1 = new JSONArray();
		String val="";
	    input1 = gson.fromJson(request.getParameter("courseObject"), ReportInfoGetter.class);
	    
	    System.out.println("pid-"+input1.getProgram_id());
	    System.out.println("entity-"+input1.getEntity_id());
	    System.out.println("start-"+input1.getStart_date());
	    System.out.println("end-"+input1.getEnd_date());
	    
	   // System.out.println("end-"+input1.getEnd_date());
		if (input1.getFlag().equalsIgnoreCase("SINGLE"))
		{
			 val =computationDao.finalMeritListProcess(input1);
		}
		else if (input1.getFlag().equalsIgnoreCase("ALL"))
		{
			val =computationDao.finalMeritListProcessforAll(input1);
		}
	    
	     
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
	
	
	public static String getDocumentPath(HttpServletRequest request, ServletContext sc)
	{
		String serverPath=sc.getRealPath(File.separator)+"DOCS"+(File.separator)+"EnteranceTestMarks";
		File admitcardref = new File(serverPath);
		admitcardref.mkdirs();
		
		return serverPath;
		
	}
	public ModelAndView uploads(HttpServletRequest request, HttpServletResponse responce)
	{
	//	ReportInfoGetter input1 = new ReportInfoGetter();
//		Gson gson= new Gson();
//		   input1 = gson.fromJson(request.getParameter("courseObject"), ReportInfoGetter.class);
//		   System.out.println("PID:-"+request);
		   
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String filePath = getDocumentPath(request, this.getServletContext());
		
		filePath=filePath+(File.separator);
		 try
	        {
			 //List fileItems = upload.;
			 List fileItems = upload.parseRequest(request);
	        	Iterator i = fileItems.iterator();
	        	while(i.hasNext()) 
	        	{
	        		FileItem fi = (FileItem)i.next();
	        		if(!fi.isFormField())	
	        		{
	        			String recievedFileName = fi.getName().toLowerCase();
	        			String fileType = recievedFileName.substring(recievedFileName.lastIndexOf(".") + 1);
	        			File file = new File(filePath);
	        			double fileSize = fi.getSize()/1024;
	        			 String fileName = fi.getName();
	        			 
	        			 System.out.println(fileName);
	        			 String contentType = fi.getContentType();
				            boolean isInMemory = fi.isInMemory();
				            long sizeInBytes = fi.getSize();
				            // Write the file
				            if( fileName.lastIndexOf("\\") >= 0 ){
				               file = new File( filePath + 
				               fileName.substring( fileName.lastIndexOf("\\"))) ;
				            }else{
				               file = new File( filePath + 
				               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
				            }
				            fi.write( file ) ;
	        		}
	        	
	        		
	        	}
	        	
	        }
		 catch(Exception e)
		 {
			 
		 }
		return null;
		
	}
	
	
	public ModelAndView uploadsDHA(HttpServletRequest request, HttpServletResponse responce)
	{
//		ReportInfoGetter input1 = new ReportInfoGetter();
//     	Gson gson= new Gson();
//     	input1 = gson.fromJson(request.getParameter("courseObject"), ReportInfoGetter.class);
//		   System.out.println("PID:-"+input1.getProgram_id());
//		   //
//		   File file1 = new File(input1.getProgram_name());
//		   System.out.println("PIDN:-"+file1);
		   
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String filePath = getDocumentPath(request, this.getServletContext());
		
		filePath=filePath+(File.separator);
		 try
	        {
			 //List fileItems = upload.;
			 List fileItems = upload.parseRequest(request);
	        	Iterator i = fileItems.iterator();
	        	while(i.hasNext()) 
	        	{
	        		FileItem fi = (FileItem)i.next();
	        		if(!fi.isFormField())	
	        		{
	        			String recievedFileName = fi.getName().toLowerCase();
	        			String fileType = recievedFileName.substring(recievedFileName.lastIndexOf(".") + 1);
	        			File file = new File(filePath);
	        			double fileSize = fi.getSize()/1024;
	        			 String fileName = fi.getName();
	        			 //fileName="DHA01.pdf";
	        			 System.out.println(fileName);
	        			 String contentType = fi.getContentType();
				            boolean isInMemory = fi.isInMemory();
				            long sizeInBytes = fi.getSize();
				            // Write the file
				            if( fileName.lastIndexOf("\\") >= 0 ){
				               file = new File( filePath + 
				               fileName.substring( fileName.lastIndexOf("\\"))) ;
				            }else{
				               file = new File( filePath + 
				               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
				            }
				            fi.write( file ) ;
	        		}
	        	
	        		
	        	}
	        	
	        }
		 catch(Exception e)
		 {
			 
		 }
		return null;
		
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
		
	    List<admissionBean> dataList =computationDao.getCustomeGridData(input1);
	    
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
	
	
	
	public ModelAndView UpdateSCLcomponent(HttpServletRequest request, HttpServletResponse responce)
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
		
	    
	   admissionBean dataList =computationDao.UpdateSCLMarks(input1);
	    
	   System.out.println("here--");
	   System.out.println("end-"+dataList.getFlag());
	   
	   System.out.println("aa--");
	   
	   JSONObject jsonObject = new JSONObject();
       jsonObject.put("val1", dataList.getFlag());
       array1.add(jsonObject);
	      
	    System.out.println(array1.toString());
	    
	    
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	public ModelAndView UpdateFinalCandidate(HttpServletRequest request, HttpServletResponse responce)
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
		
	    
	   admissionBean dataList =computationDao.UpdateFinalStatus(input1);
	    
	   
	   JSONObject jsonObject = new JSONObject();
       jsonObject.put("val1", dataList.getFlag());
       array1.add(jsonObject);
	      
	    System.out.println(array1.toString());
	    
	    
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	
	
	
	
	public ModelAndView TransferApplication(HttpServletRequest request, HttpServletResponse responce)
	{
		System.out.println("hello");
		Gson gson= new Gson();
		transferBean transfer = new transferBean();
		transferBean transfer1 = new transferBean();
		JSONArray array1 = new JSONArray();
		
		transfer = gson.fromJson(request.getParameter("courseObject"), transferBean.class);
	    
	    
	  
		System.out.println("APP-"+transfer.getAppnumber());
		transfer.setTransferapp("5"+transfer.getAppnumber().substring(1));
	    System.out.println("NEW_APP-"+transfer.getTransferapp());
	    System.out.println("FROM-"+transfer.getFromprogram());
	    System.out.println("TO1-"+transfer.getToprogram());
	    System.out.println("TO2-"+transfer.getToprogram2());
	    System.out.println("TO3-"+transfer.getToprogram3());
	    System.out.println("transferFlag-"+transfer.getTransferFlag());
	   
	    transfer.setActualApplication(transfer.getAppnumber());
	    transfer.setPaperOptionFlag1("D");
	   // System.out.println("end-"+input1.getEnd_date());
		
	    transfer1 =computationDao.transferApp(transfer);
	    
	    //System.out.println(dataList.size());
	    JSONObject jsonObject = new JSONObject();
	    System.out.println(transfer1.getAppnumber());
	    System.out.println(transfer1.getStatus());
	    
	    jsonObject.put("transferApp", transfer1.getAppnumber());
	    jsonObject.put("status", transfer1.getStatus());
	    jsonObject.put("pass", transfer1.getPassword());
	   // jsonObject.put("flag", val);
	    
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
