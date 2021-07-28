package in.ac.dei.edrp.admissionsystem.login;

import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;





import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class loginController extends MultiActionController {

private loginDao loginDao;
	
	public void setLoginDao(loginDao loginDao)
	{
		this.loginDao=loginDao;
	}
	public ModelAndView checkLogin(HttpServletRequest request, HttpServletResponse responce)
	{
		
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
	    input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	    
	    System.out.println("don-1-"+input1.getUserName());
	    System.out.println("don-2-"+input1.getPassword());
		
	    List<admissionBean> dataList =loginDao.checkLogin(input1);
	    
	    System.out.println(dataList.size());
	    JSONObject jsonObject = new JSONObject();
	    if (dataList.size()>0)
	    {

				jsonObject.put("status", "OK");
		    	jsonObject.put("user_id", dataList.get(0).getUser_id());
		    	jsonObject.put("employee_code", dataList.get(0).getEmployee_code());
		    	jsonObject.put("panel_authority", dataList.get(0).getPanel_authority());

	    }
	    else if (dataList.size()==0)
	    {
	    	jsonObject.put("status", "NOTFOUND");
	    	jsonObject.put("user_id","NOTFOUND");
	    }
	    
	    array1.add(jsonObject);
	    System.out.println(array1.toString());
	    
	    
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
		
	}
	
	
	
	public ModelAndView loadEntity(HttpServletRequest request, HttpServletResponse responce)
	{
           System.out.println("hay baby---");
           Gson gson= new Gson();
           admissionBean input1 = new admissionBean();
       	JSONArray array1 = new JSONArray();
       	input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
       	List<admissionBean> dataList =loginDao.LoadEntityCombo(input1);
       	
       	for (int i=0; i<dataList.size();i++)
    	{
    		JSONObject abc1 = new JSONObject();
    	
    		abc1.put("entity_name", dataList.get(i).getEntity_name());
    		abc1.put("entity_id", dataList.get(i).getEntity_id());
    		
    		
    		array1.add(abc1);
    	}
    	
    	System.out.println(array1.toString());

    	return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
	
	
	
	public  ModelAndView loadProgram(HttpServletRequest request, HttpServletResponse responce)
	{
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
		 input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	
		    System.out.println(input1.getEntity_id());
		    List<admissionBean> dataList =loginDao.LoadProgramCombo(input1);
			for (int i=0; i<dataList.size();i++)
			{
				JSONObject abc1 = new JSONObject();
			
				abc1.put("program_id", dataList.get(i).getProgram_id());
				abc1.put("program_name", dataList.get(i).getProgram_name());
				abc1.put("entityId", dataList.get(i).getEntity_id());
				
				
				array1.add(abc1);
			}
			System.out.println(array1.toString());
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
	
	public  ModelAndView LoadSemester(HttpServletRequest request, HttpServletResponse responce)
	{
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
		 input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	
		    System.out.println(input1.getEntity_id());
		    List<admissionBean> dataList =loginDao.LoadSemesterCombo(input1);
			for (int i=0; i<dataList.size();i++)
			{
				JSONObject abc1 = new JSONObject();
				abc1.put("semester_code", dataList.get(i).getSemester_code());
				abc1.put("semester_name", dataList.get(i).getSemester_name());	
				array1.add(abc1);
			}
			System.out.println(array1.toString());
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
	
	
	public  ModelAndView LoadBranch(HttpServletRequest request, HttpServletResponse responce)
	{
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
		 input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	
		    System.out.println(input1.getEntity_id());
		    List<admissionBean> dataList =loginDao.LoadBranchCombo(input1);
			for (int i=0; i<dataList.size();i++)
			{
				JSONObject abc1 = new JSONObject();
				abc1.put("branch_id", dataList.get(i).getBranch_id());
				abc1.put("branch_name", dataList.get(i).getBranch_name());
				abc1.put("pck", dataList.get(i).getPck());
				array1.add(abc1);
			}
			System.out.println(array1.toString());
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
	
	
	
	public  ModelAndView getSessionDate(HttpServletRequest request, HttpServletResponse responce)
	{
		Gson gson= new Gson();
		admissionBean input1 = new admissionBean();
		JSONArray array1 = new JSONArray();
		
		 input1 = gson.fromJson(request.getParameter("courseObject"), admissionBean.class);
	
		    System.out.println(input1.getEntity_id());
		    List<admissionBean> dataList =loginDao.LoadSession(input1);
			for (int i=0; i<dataList.size();i++)
			{
				JSONObject abc1 = new JSONObject();
			
				abc1.put("sessionDate", dataList.get(i).getSessionDate());
				abc1.put("session_start_date", dataList.get(i).getSession_start_date());
				abc1.put("session_end_date", dataList.get(i).getSession_end_date());
				
				array1.add(abc1);
			}
			System.out.println(array1.toString());
		return new ModelAndView("CreateCourse/hello", "message", array1.toString());
	}
	
//	public ModelAndView barCode(HttpServletRequest request, HttpServletResponse responce)
//	 {
//		String myCodeText = "http://admission.dei.ac.in:8085/CMS/REPORTS/0001/2018-19/00010001/0001003/SM1_2018-07-01_2018-12-31/8/XX/00/8-Progress-Result%20Card-1801946.pdf";
//		String filePath = "C:/Users/dell/Desktop/adm2020/QRCode1.png";
//		int size = 250;
//		String fileType = "png";
//		File myFile = new File(filePath);
//		try {
//			
//			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//			
//			
//			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
//			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
// 
//			QRCodeWriter qrCodeWriter = new QRCodeWriter();
//			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
//					size, hintMap);
//			int CrunchifyWidth = byteMatrix.getWidth();
//			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
//					BufferedImage.TYPE_INT_RGB);
//			image.createGraphics();
// 
//			Graphics2D graphics = (Graphics2D) image.getGraphics();
//			graphics.setColor(Color.WHITE);
//			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
//			graphics.setColor(Color.BLACK);
// 
//			for (int i = 0; i < CrunchifyWidth; i++) {
//				for (int j = 0; j < CrunchifyWidth; j++) {
//					if (byteMatrix.get(i, j)) {
//						graphics.fillRect(i, j, 1, 1);
//					}
//				}
//			}
//			ImageIO.write(image, fileType, myFile);
//		} catch (WriterException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("\n\nYou have successfully created QR Code.");
//		
//		
//            return null;
//     }
   
}
