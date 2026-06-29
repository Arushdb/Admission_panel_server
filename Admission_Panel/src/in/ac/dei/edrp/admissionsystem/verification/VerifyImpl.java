package in.ac.dei.edrp.admissionsystem.verification;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.imageio.ImageIO;

import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.studentBean;
import in.ac.dei.edrp.admissionsystem.security.ConfigUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class VerifyImpl extends SqlMapClientDaoSupport implements VerifyDao {

	@Override
	public List<studentBean> getAcademicMarks(studentBean sbean) {
		
		List <studentBean> studentlist = null;
		
	 studentlist =getSqlMapClientTemplate().queryForList("verifystudent.getstudentmarks",sbean);
		// TODO Auto-generated method stub
		return studentlist;
	}

	@Override
	public int updatestatus(studentBean sbean) {
		// TODO Auto-generated method stub
		int count =getSqlMapClientTemplate().update("verifystudent.updatestatus",sbean);
		return count;
		
	}

	@Override
	public int validateIWlist(studentBean sbean) {
		List <studentBean> studentlist = null;
		// TODO Auto-generated method stub
		studentlist =getSqlMapClientTemplate().queryForList("verifystudent.validateIWlist",sbean);
		
		return studentlist.size();
	}
	
	@Override  //added by Jyoti on 19 Jun 2025
	public List<studentBean> getUserProgramList(String user) {
		
		List <studentBean> studentlist = null;
		
		studentlist =getSqlMapClientTemplate().queryForList("verifystudent.getUserProgramList",user);
		
		return studentlist;
	}

	@Override  //added by Jyoti on 19 Jun 2025
	public List<studentBean> getVfyProgramList(studentBean sbean) {
		
		List <studentBean> studentlist = null;
		
		studentlist =getSqlMapClientTemplate().queryForList("verifystudent.getVfyProgramList",sbean);
		
		return studentlist;
	}

	//chkliststatus added by Jyoti on 19 Jun 2025
	@Override
	public List<studentBean> chkliststatus(studentBean sbean) {
		// TODO Auto-generated method stub
		List <studentBean> vlist = null;
		vlist = getSqlMapClientTemplate().queryForList("verifystudent.chkliststatus",sbean);
		return vlist;
		
	} 

	//updateliststatus added by Jyoti on 19 Jun 2025
	@Override
	public int updateliststatus(studentBean sbean) {
		// TODO Auto-generated method stub
		int count =getSqlMapClientTemplate().update("verifystudent.updateliststatus",sbean);
		return count;
		
	} 
	
	public BufferedImage VerifySignature(studentBean sbean) {
		List <studentBean> studentlist = null;
		String testid ="";
		String filename="";
		String appno="";
		BufferedImage newimage = null;
		String omrpath = ConfigUtil.get("omr.path");
		System.out.println("omrpath:"+omrpath);
		 //String outputDir = sbean.getTargetPath()+"signatureFolder";
		 //Properties p = ConfigReader.loadProperties(sbean.getTargetPath()+"config.properties");
		 //Properties p = ConfigReader.loadProperties("config.properties");
		 //System.out.println(p.getProperty("omr.path"));
		 

	        
	     
		
		
		int width =0;
		 int yOffset = 0;
		studentlist =getSqlMapClientTemplate().queryForList("verifystudent.getSignaturefile",sbean);
		
		int totalHeight = 800 * studentlist.size();
		///DEIAdmission/src/images/Scan_science_0003.bmp
        //if (studentlist.size()>0) {
          for(studentBean student:studentlist) {
        	  testid=student.getDocId();
        	  appno = student.getApplication_number();
        	  filename =student.getFile_name();
        	  //String inputPath =sbean.getTargetPath()+ "processedFolder"+File.separator+ testid+ File.separator+filename;
        	  String inputPath =omrpath+ File.separator+ testid+ File.separator+filename;
        	// Read the input BMP file
              try {
				BufferedImage originalImage = ImageIO.read(new File(inputPath));
				
				
				if (originalImage == null) {
	                System.err.println("Could not read image: " + inputPath);
	                continue;
	            }
			
					
		           // int height = originalImage.getHeight();
		            if (newimage == null) {
		                width = originalImage.getWidth();
		            newimage = new BufferedImage(width, totalHeight, originalImage.getType());	
		            
				}
		            
				
				for ( int y=0 ; y < 800; y++) {
	                for (int x=0 ; x <width; x++) {
	                    int rgb = originalImage.getRGB(x, y);
	                    
	                    newimage.setRGB(x, y+yOffset, rgb);
	                }
	            }
				 yOffset += 800; 
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  
        	  
          }
          
       // Create output directory if it doesn't exist
//          File dir = new File(outputDir);
//          if (!dir.exists()) {
//              dir.mkdirs();
//          }
//
//          // Write new image to output file
//          try {
//        	  String outputPath = outputDir+File.separator+ appno+".bmp";
//			ImageIO.write(newimage, "bmp", new File(outputPath));
//			   System.out.println("First 5 lines saved to: " + outputPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
       
          //return newImage;

                   
          return newimage ;
          //String inputPath =sbean.getTargetPath()+ "processedFolder"+File.separator+ testid+ File.separator+filename; // Replace with your file path
             
		
		
		
    }
	
	@Override
	public List<studentBean> getApplicantPrograms(studentBean sbean) {
		// TODO Auto-generated method stub
		List <studentBean> programList = null;
		// TODO Auto-generated method stub
		programList =getSqlMapClientTemplate().queryForList("verifystudent.getApplicantPrograms",sbean);
		
		return programList;
	}

	@Override
	public List<studentBean> getUserPrograms(studentBean sbean) {
		List <studentBean> programList = null;
		// TODO Auto-generated method stub
		programList =getSqlMapClientTemplate().queryForList("verifystudent.getUserPrograms",sbean);
		
		return programList;
	}

	@Override
	public List<studentBean> getAttendance(studentBean sbean) {
		List <studentBean> attendanceList = null;
		// TODO Auto-generated method stub
		attendanceList =getSqlMapClientTemplate().queryForList("verifystudent.getAttendance",sbean);
		
		return attendanceList;
	}

	@Override
	public List<studentBean> checkIWcalled(studentBean sbean) {
		// TODO Auto-generated method stub
		List <studentBean> iwcalledList = null;
		// TODO Auto-generated method stub
		iwcalledList =getSqlMapClientTemplate().queryForList("verifystudent.getIWcalled",sbean);
		
		return iwcalledList;
	}

	@Override
	public List<studentBean> checkComponentmarks(studentBean sbean) {
		List <studentBean> iwmarks = null;
		// TODO Auto-generated method stub
		String date = LocalDate.of(LocalDate.now().getYear(), 7, 1).toString();
		sbean.setStartdate(date);
		iwmarks =getSqlMapClientTemplate().queryForList("verifystudent.getComponentmarks",sbean);
		
		return iwmarks;
	}

	@Override
	public List<studentBean> getInterviewLevel(studentBean sbean) {
		// TODO Auto-generated method stub
		
		List<studentBean> iwlevellist=null;
		// TODO Auto-generated method stub
		iwlevellist =getSqlMapClientTemplate().queryForList("verifystudent.getInterviewLevel",sbean);
		
		return iwlevellist;
	}
	

}
