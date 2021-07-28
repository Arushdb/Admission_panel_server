package in.ac.dei.edrp.admissionsystem.admitcard;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.studentBean;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class admitcardImpl extends SqlMapClientDaoSupport implements admitcardDao {

	// Log4JInitServlet logObj = new Log4JInitServlet();
	// ReportingFunction reportfunction = new ReportingFunction();
	
	 
	 
	public String generateAdmitCard(GenerateAdmitCardBean obj,ServletContext sc)
	{
		 String mypath=sc.getRealPath("images");
		 System.out.println(sc.getRealPath("images"));
		String 	AdmitCardPath ;
		String 	ApplicantAdmitCardPath ;
		String  AdmitCardfolder = "AdmitCards";
		
		List<GenerateAdmitCardBean> programList = new ArrayList<GenerateAdmitCardBean>();
		
		java.util.List <GenerateAdmitCardBean> applicants  =  null;
		int studentcount = 0;
		  try 
		  {
              String uinid="0001";
              
              programList =  getSqlMapClientTemplate().queryForList("Admitcard.getprogramListShiftWise"); // @Q-0
			  
              for (int PT=0; PT<programList.size(); PT++)
			  {
            	  obj.setProgram_id(programList.get(PT).getProgram_id());
    			  obj.setEntity_id(programList.get(PT).getEntity_id());
    			  obj.setProgram_name(programList.get(PT).getProgram_name());
			  
              
			  String programid=obj.getProgram_id();
			  String entity=obj.getEntity_id();
			  System.out.println("obj-"+programid+"-"+entity);
			  
//			  obj.setInstructions("hello");
//			  obj.setHindiInstructons("hello");
//			  
//			  String[] instructions =  obj.getInstructions().split(":");
//			  String[] instructionsHindi =  obj.getHindiInstructons().split(":");
			  
			  String logopath =mypath +
		      File.separator + "logo.jpg" ;	 

//			  String barCode =mypath +
//		      "barCodes" + File.separator + "QRCode1.png" ;	
//			  
//			  String barCode1 =mypath +
//		      "barCodes" + File.separator + "QRCode.png" ;
			  
			  
			  obj.setUniversity_id("0001");

		      //String[] udate = reportfunction.getSessionDate(obj.getUniversity_id());
			  //obj.setStart_date(udate[0]);
	         // obj.setEnd_date(udate[1]); 
			  //obj.setStart_date("2019-07-01");
			 // obj.setEnd_date("2020-06-30");
			  
			  obj.setStart_date(programList.get(PT).getStart_date());
	          obj.setEnd_date(programList.get(PT).getEnd_date());
	          
	          String startdate=obj.getStart_date();
	          String enddate=obj.getEnd_date();
	          String session = obj.getStart_date().substring(0,4)+ '-' + obj.getEnd_date().substring(0,4);
	          
	          System.out.println("session-"+session);
	          //////////
	         // compeligibiltity(uinid,programid,entity,startdate,enddate);
	          ////////////
	          
	          applicants =getSqlMapClientTemplate().queryForList("Admitcard.getapplcants",obj);
	          System.out.println("applicants-"+applicants.size());
	          
	          for(int i = 0; i < applicants.size(); i++)
	  		{
	  			GenerateAdmitCardBean admCardBean = applicants.get(i);
	  			List<admissionBean> entranceTests = getSqlMapClientTemplate().queryForList("Admitcard.getETForAdmCard",admCardBean);
	  			
	  			List<admissionBean> programs = getSqlMapClientTemplate().queryForList("Admitcard.programWithCommonShift",admCardBean);//Added by Arjun on 13-06-2019 
	  			applicants.get(i).setPrograms(programs);
	  			GenerateAdmitCardBean applicantVanue = (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("Admitcard.getApplicantVanue",admCardBean);
	  			applicants.get(i).setVenue(applicantVanue.getVenue());
	  			applicants.get(i).setEntranceTests(entranceTests);
	  		}
	          if(applicants.size()>0){
	        	  
	        	  String homeDir = System.getProperty("user.home"); //server location to admitcards

	     		 AdmitCardPath = homeDir +File.separator+ AdmitCardfolder+
	     				File.separator+obj.getProgram_id();

	     		 File admitcardref = new File(AdmitCardPath);
	     		 admitcardref.mkdirs();
	     		 
	     		Image blankPhoto = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(mypath+File.separator+"BLANK.jpg"), null); 
	    		Image blankSignature = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(mypath+File.separator+"BLANK.jpg"), null);
	    		blankPhoto.scaleAbsolute(100, 125);
	    		blankPhoto.setBorder(Rectangle.BOX);
	    		blankPhoto.setBorderColor(Color.BLUE);
	    		blankPhoto.setBorderWidth(3.0f);
	    		blankPhoto.setAbsolutePosition(400f, 597f);
	    		blankSignature.scaleAbsolute(120, 20);
	    		blankSignature.setDpi(300, 300);
	    		blankSignature.setAbsolutePosition(380f, 560f);
	    		
	    		Image logo = null;
	    		
	    		for (GenerateAdmitCardBean applicationObj:applicants){
	    			
	    			int noOfPapers = applicationObj.getEntranceTests().size();
	        		Document document = new Document(PageSize.A4);
	        		studentcount = studentcount+1;

	        		applicationObj.setAdmitCardPath(AdmitCardPath);
	        		applicationObj.setStart_date(obj.getStart_date());


		            ApplicantAdmitCardPath = AdmitCardPath+File.separator	+
		            		applicationObj.getTestnumber()+ ".pdf";

		            System.out.println("Admit Card Path:"+ApplicantAdmitCardPath);


					PdfWriter.getInstance(document,
		            new FileOutputStream(ApplicantAdmitCardPath));


		            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8,
		                    Font.NORMAL, new Color(0, 0, 0));
		          
		            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 8,
		                    Font.BOLD, new Color(0, 0, 0));
		            
		            BaseFont engInsFont = BaseFont.createFont(sc.getRealPath("Fonts")+File.separator+"GOTHIC.TTF",BaseFont.WINANSI,BaseFont.EMBEDDED );
		            BaseFont bf = BaseFont.createFont(sc.getRealPath("Fonts")+File.separator+"K010.TTF",BaseFont.WINANSI,BaseFont.EMBEDDED );
		           // Font font = new Font(bf, 9);
		            
///////////////////first section/////////////////////////////
		            HeaderFooter header = new HeaderFooter(new Phrase(
	                        "DAYALBAGH EDUCATIONAL INSTITUTE,DAYALBAGH, AGRA ",
	                        FontFactory.getFont(FontFactory.HELVETICA, 10,
	                            Font.BOLD, new Color(0, 0, 0))), false);
		            
		            header.setAlignment(Element.ALIGN_CENTER);
		            header.setBorderWidth(0);
		            document.setHeader(header);
		            
		            GenerateAdmitCardBean month = (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("Admitcard.getAdmitCardMonth"); //Added by Atul Dayal
	                GenerateAdmitCardBean IntFlag = (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("Admitcard.getAdmitCardInterviewFlag"); //Added by Atul Dayal
	                GenerateAdmitCardBean personalinterview = (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("Admitcard.getInterviewDateAndTime",obj); //Added by Atul Dayal
		            
		            
	                Chunk line = new Chunk("ADMIT CARD : "+session );
		            
	 	           if(obj.getProgram_id().equalsIgnoreCase("0001179")||obj.getProgram_id().equalsIgnoreCase("0001232") ||obj.getProgram_id().equalsIgnoreCase("0001235"))
	 	           {
	 	            line = new Chunk("ADMIT CARD : "+"RET,"+month.getAdmitcard_month()+"-"+obj.getStart_date().substring(0,4));//Dharna //updated by Atul Dayal 22-12-2014
	 	           }
	 	           else if (obj.getProgram_id().equalsIgnoreCase("0001099"))
	 	           {
	 	            	line = new Chunk("ADMIT CARD : "+"M.Phil."+" "+"RET,"+month.getAdmitcard_month()+"-"+obj.getStart_date().substring(0,4)); //added by Atul Dayal 22-12-2014
	 	           }
	 	            
	 	            
	 	            line.setUnderline(0.1f, -2f);
	 	            Paragraph para = new Paragraph ();
	 	            para.add(line);
	 	            para.setAlignment(Element.ALIGN_CENTER);
	 	            
	 	           try {
		                 logo= Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(logopath), null);

		                 logo.scaleAbsolute(100, 100);
		                 logo.setAbsolutePosition(40f, 720f);


		              } 
		            catch (Exception ee) {
		                System.out.println("image exception" + ee.getStackTrace());
		                System.out.println("image exception"+ee);
		               // logObj.logger.error("Exception in logo for Registration Number :"+applicationObj.getRegistration_number() + ee);
		                throw new Exception(ee);
		                
		            }
///////////////////first section///////////////////////////////////////////////////////////
		            
///////////////////Second section///////////////////////////////////////////////////////////		            
		            
		            PdfPTable  classdetail = new PdfPTable(new float[] {1f,2f});
		            classdetail.setSpacingBefore(100);
		            if(obj.getProgram_id().equalsIgnoreCase("0001179")||obj.getProgram_id().equalsIgnoreCase("0001232")||obj.getProgram_id().equalsIgnoreCase("0001235")){
		            	addCell(headerFont, classdetail, "Program");
		            }
		            else
		            {
		            	addCell(headerFont, classdetail, "Classe");
		            }
		            
		            
		               addCell(headerFont, classdetail, obj.getProgram_name()); 
		            

		             addCell(dataFont, classdetail, "Name");
		             addCell(dataFont, classdetail, applicationObj.getName());


		               if (!(applicationObj.getFathername().equalsIgnoreCase("")))
		             {
		                 addCell(dataFont, classdetail, "Father Name");
		                 addCell(dataFont, classdetail, applicationObj.getFathername());
		             }
		            else
		             {
		            	 addCell(dataFont, classdetail, ".");
		                 addCell(dataFont, classdetail, ".");
		             }
		            
		            
///////////////////Second section end///////////////////////////////////////////////////////////		            
		            
	
///////////////////Third section Start///////////////////////////////////////////////////////////
			            PdfPTable  venuedetail = new PdfPTable(new float[] {1,2});
			            
			            venuedetail.setSpacingBefore(80);
			           
		                addCellwithborder(dataFont, venuedetail, "Application/Roll  Number");
		                addCellwithborder(dataFont, venuedetail, applicationObj.getApplication_number());

		                addCellwithborder(dataFont, venuedetail, "Roll Number");
			            addCellwithborder(dataFont, venuedetail, applicationObj.getProgramCode()+"-"+applicationObj.getTestnumber()+"-"+obj.getStart_date().substring(2, 4));

			            
			           
			            GenerateAdmitCardBean papoption = (GenerateAdmitCardBean) getSqlMapClientTemplate().queryForObject("Admitcard.getpaperoptionforchoice",applicationObj);   
			            
			            if (!(papoption.getPaper_code().equalsIgnoreCase("0")))
			            {
			            	addCellwithborder(dataFont, venuedetail, "Venue");
			            	//addCellwithborder(dataFont, venuedetail, "Entrance-Coordinator");
				            addCellwithborder(dataFont, venuedetail, applicationObj.getVenue());
				            System.out.println("Venue this"+applicationObj.getVenue());
			            }
			            
			            else
			            {
			            	addCellwithborder(dataFont, venuedetail, "Venue");
				            addCellwithborder(dataFont, venuedetail, "DayalBagh Deemed University, Dayalbagh Agra"); // can be entered for b.tech
			            }
			            

			            if(noOfPapers>0)
			            {
			            	addCellwithborder(dataFont, venuedetail, "Written Test on");
				            addCellwithborder(dataFont, venuedetail, applicationObj.getTestdate());

				            addCellwithborder(dataFont, venuedetail, "Written Test Reporting Time");
				            addCellwithborder(dataFont, venuedetail, applicationObj.getTesttime());
			            }
			            

			            addCellwithborder(dataFont, venuedetail, "Personal Interview On");
			  
			            if (IntFlag.getInterview_Flag().equalsIgnoreCase("Y")) //added By atul Dayal
			            {
			            	addCellwithborder(dataFont, venuedetail, personalinterview.getInterviewdate() ); //added By atul Dayal
			            }
			            else //added By atul Dayal
			            {
			            	addCellwithborder(dataFont, venuedetail, "AS NOTIFIED ON THE DEI WEBSITE- www.dei.ac.in" ); 
			            }
			            
			            
			            
			            addCellwithborder(dataFont, venuedetail, "Interview Time");
			          
			            if(IntFlag.getInterview_Flag().equalsIgnoreCase("Y")) //added By atul Dayal
			            {
			            	addCellwithborder(dataFont, venuedetail, personalinterview.getInterviewtime()); //added By atul Dayal
			            }
			            else
			            {
			            	addCellwithborder(dataFont, venuedetail, "AS NOTIFIED ON THE DEI WEBSITE- www.dei.ac.in");//Added By Arjun on 20-06-2014
			            }
			            
			            
			            
			            if(noOfPapers>0)
			            {
			            	if(obj.getProgram_id().equalsIgnoreCase("0001179")||obj.getProgram_id().equalsIgnoreCase("0001232")||obj.getProgram_id().equalsIgnoreCase("0001235")){
			            		addCellwithborder(dataFont, venuedetail, "RET: Subject / Discipline");//Dharna
			            	}
						    else if (obj.getProgram_id().equalsIgnoreCase("0001099"))
			            	{
			            		addCellwithborder(dataFont, venuedetail, "M.Phil: Subject / Discipline");//added by Atul Dayal 22-12-2014
			            	}
			            	else
			            	{
			            	addCellwithborder(dataFont, venuedetail, "Entrance Test Options");
			            	}
			            	
			            	PdfPTable paperTable = new PdfPTable(2);
							paperTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							
				            for(int i = 0; i < noOfPapers; i++)
				            {
				            	if(((i+1) == noOfPapers) && noOfPapers%2!=0)
				            	{
				            		paperTable.addCell(new Phrase((i+1)+" . "+applicationObj.getEntranceTests().get(i).getPaperName().concat(" ( "+applicationObj.getEntranceTests().get(i).getPaperCode()+" )"), dataFont));
				            		paperTable.addCell(new Phrase("", dataFont));
				            	}
				            	else
				            	{
				            		paperTable.addCell(new Phrase((i+1)+" . "+applicationObj.getEntranceTests().get(i).getPaperName().concat(" ( "+applicationObj.getEntranceTests().get(i).getPaperCode()+" )"), dataFont));
				            	}
				            	
				            }
				            venuedetail.addCell(paperTable);
			            }   
///////////////////Third section End///////////////////////////////////////////////////////////	
			            
///////////////////4th section start///////////////////////////////////////////////////////////	
	
			            PdfPTable programsTbl = new PdfPTable(new float[]{2.0f,8.0f});
			            programsTbl.setWidthPercentage(100);
			            programsTbl.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            programsTbl.getDefaultCell().setPadding(2);
			            programsTbl.setSpacingBefore(10);
			            
			            PdfPCell msgCell = new PdfPCell(new Phrase("You need to appear in any one entrance exam for the program(s) below.", headerFont));
			            msgCell.setBorder(Rectangle.NO_BORDER);
			            msgCell.setPadding(2);
			            msgCell.setColspan(2);
			            msgCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						
			            programsTbl.addCell(msgCell);
			            programsTbl.addCell(new Phrase("Roll Number", headerFont));
		            	programsTbl.addCell(new Phrase("Program Name", headerFont));
		            	
			            for(int pCount = 0; pCount <  applicationObj.getPrograms().size(); pCount++)
			            {
			            	admissionBean program = applicationObj.getPrograms().get(pCount);
			         
			            	programsTbl.addCell(new Phrase(program.getProgramCode()+"-"+applicationObj.getApplication_number()+"-"+program.getParam1(), dataFont));
			            	programsTbl.addCell(new Phrase((pCount+1)+". "+program.getProgramName(), dataFont));
			            }
			            
			            int leftOutOf5 = 5-applicationObj.getPrograms().size();
			            for(int lCount = 0; lCount<leftOutOf5; lCount++)
			            {
			            	programsTbl.addCell(new Phrase("", dataFont));
			            	programsTbl.addCell(new Phrase("", dataFont));
			            }
///////////////////4th section end///////////////////////////////////////////////////////////

///////////////////5th section start///////////////////////////////////////////////////////////
			            
			            Paragraph noticeInfo = new Paragraph();//Added by Arjun on 13-06-2019
			            noticeInfo.setSpacingBefore(10);
			            Phrase noticephrase = new Phrase();
			            noticephrase.setFont(headerFont);
			            noticephrase.add(new Chunk("Notice :"));
			            
			            noticeInfo.add(noticephrase);
			            noticeInfo.setFont(dataFont);//Added by Arjun on 13-06-2019
			            noticeInfo.add(new Phrase("\n"));
			            noticeInfo.add(new Phrase("FOR ALL ADMISSION RELATED QUERIES/ANNOUNCEMENTS, KINDLY SEE DEI WEBSITE (https://www.dei.ac.in/dei/admission) REGULARLY."));            
///////////////////5th section end///////////////////////////////////////////////////////////	

///////////////////6th section start///////////////////////////////////////////////////////////	
			            Paragraph addressinfo = new Paragraph();     
			            Phrase addressphrase = new Phrase();
			            addressphrase.setFont(headerFont);
			            addressphrase.add(new Chunk("Address :"));
			            
                        addressinfo.setFont(dataFont);  
			            addressinfo.add(addressphrase);
			            addressinfo.add(new Phrase("\n"));
			            addressinfo.add(new Phrase(applicationObj.getAddress()));
			            addressinfo.add(new Phrase("\n"));
			            addressinfo.add(new Phrase(applicationObj.getCity()));
			            addressinfo.add(new Phrase("\n"));
			            addressinfo.add(new Phrase(applicationObj.getState()));
			            addressinfo.add(new Chunk("-"));
			            if(applicationObj.getPincode()==null || applicationObj.getPincode().trim().equals("") || applicationObj.getPincode().trim().equals("999999"))
			            {
			            	addressinfo.add(new Chunk(""));
			            }
			            else
			            {
			            	 addressinfo.add(new Chunk(applicationObj.getPincode()));
			            }

///////////////////6th section end///////////////////////////////////////////////////////////	

///////////////////7th section start///////////////////////////////////////////////////////////
			            PdfPTable disclaimerTable = new PdfPTable(1);
		            	disclaimerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            	disclaimerTable.setSpacingBefore(5);
		            	disclaimerTable.setSpacingAfter(5);
		            	
		            	
		            	
		            	//Dharna
		            	if(obj.getProgram_id().equalsIgnoreCase("0001179")||obj.getProgram_id().equalsIgnoreCase("0001232")||obj.getProgram_id().equalsIgnoreCase("0001235")){
		            		disclaimerTable.addCell(new Phrase("1. The admit card is not a proof of eligibility for admission. Please refer to the prospectus of the institute and latest notifications on the Institute website to ensure that the conditions for eligibility are satisfied.", dataFont));
		            		disclaimerTable.addCell(new Phrase("2. It is the responsibility of the candidate to ensure that they meet the minimum criteria for their respective category before appearing for written test and/or interview.", dataFont));
		            		disclaimerTable.addCell(new Phrase("3. If it is detected at any stage that the candidate does not meet the eligibility criterion for the category and program they have applied for, their application is liable to be rejected at any stage.", dataFont));
		            	}
		            	else
		            	{
		            		disclaimerTable.addCell(new Phrase("1. The admit card is not a proof of eligibility for admission. Please refer to the prospectus of the Institute to ensure that the conditions for eligibility are satisfied.", dataFont));
		            		disclaimerTable.addCell(new Phrase("2. It is the responsibility of the candidate to ensure that they meet the minimum criteria for their respective category before appearing for written test and/or interview.", dataFont));
		            		disclaimerTable.addCell(new Phrase("3. If it is detected at any stage that the candidate does not meet the eligibility for the category and program they have applied for, their application is liable to be rejected.", dataFont));
		            	}
		            	    disclaimerTable.addCell(new Phrase("4. Candidates who do not belong to the General Category will be required to produce documents in original to prove their eligibility for the category they have declared at the time of the interview, if they are shortlisted for interview.", dataFont));
		            
			        
///////////////////7th section end///////////////////////////////////////////////////////////	
		       
		           document.open();
		           
		           //////////////////////////////////Qr Code///////////////////////////////////////////
		           
		           /*
		            Image QrCode = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(barCode), null);
		            QrCode.scaleAbsolute(80, 80);
		            QrCode.setAbsolutePosition(450f, 740f);
		            
		            
		            Image QrCode1 = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(barCode1), null);
		            QrCode1.scaleAbsolute(60, 60);
		            QrCode1.setAbsolutePosition(450f, 230f);
		            */
		            /////////////////////////////////////////////////////////////////////////////////////////
		            
		            
		            //////////////////////////////////////////////photo signature//////////////////////////////////////////////
		            if(applicationObj.getApplicationType().trim().equalsIgnoreCase("E") ||applicationObj.getApplicationType().trim().equalsIgnoreCase("P"))
		            {
		            	try {
			            	
		            		Image studentPhoto = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(applicationObj.getDoc_path()+File.separator	+ "photo.jpg"), null);
		            		studentPhoto.scaleAbsolute(100, 125);
		            		studentPhoto.setBorder(Rectangle.BOX);
		            		studentPhoto.setBorderColor(Color.BLUE);
		            		studentPhoto.setBorderWidth(3.0f);
		            		studentPhoto.setAbsolutePosition(400f, 597f);
		            		document.add(studentPhoto);
			            	

			            } catch (Exception ee)
			            {
			                System.out.println("image exception" + ee.getStackTrace());
			                System.out.println("image exception"+ee);
			               // logObj.logger.error("Exception in Student Photo for Registration Number :"+applicationObj.getRegistration_number() + ee);
			              
			                document.add(blankPhoto);  //added by atul dayal
			               
			            }

			            try 
			            {
			            	System.out.println(applicationObj.getDoc_path()+"doc path from entitystudent");
			            	Image studentSignature= Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(applicationObj.getDoc_path()+File.separator	+ "signature.jpg"), null);
			            	studentSignature.scaleAbsolute(120, 20);
			                studentSignature.setDpi(300, 300);
			                studentSignature.setAbsolutePosition(380f, 560f);
			                document.add(studentSignature);

			            } catch (Exception ee) 
			            {
			                System.out.println("image exception" + ee.getStackTrace());
			                System.out.println("image exception"+ee);
			               // logObj.logger.error("Exception in Student Signature for Registration Number :"+applicationObj.getRegistration_number() + ee);
			                document.add(blankPhoto);  //added by atul dayal
			                
			            }

		            }
	            	else
	            	{
	            		document.add(blankPhoto);
	            		
	            	}
		            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		            
		            document.add(para); //1
		            document.add(logo);//1
		            
		            document.add(classdetail); //2
		            document.add(venuedetail);//3
		            document.add(programsTbl);//4
		            document.add(noticeInfo); //5
		            document.add(addressinfo);//6
		            
		            document.add(new Paragraph("\n\nDisclaimer".toUpperCase(), headerFont)); //7
		            document.add(disclaimerTable); //7
		            
		            //document.add(QrCode);
		           // document.add(QrCode1);
		            document.close();
		            
		            String targetPath = applicationObj.getAdmitCardPath().replace(System.getProperty("user.home")+File.separator,"");
		            studentBean studentInfoForAppPDF = new studentBean();
		            studentInfoForAppPDF.setForm_number(applicationObj.getApplication_number());
		            studentInfoForAppPDF.setTargetPath(targetPath);
		            studentInfoForAppPDF.setFile_name(applicationObj.getTestnumber()+".pdf");
		            studentInfoForAppPDF.setDocId(applicationObj.getRegistration_number());
		            studentInfoForAppPDF.setSourcePath(applicationObj.getAdmitCardPath());
		            
		            getSqlMapClientTemplate().update("Admitcard.updateadmitcardpath", applicationObj);
		           getSqlMapClientTemplate().update("Admitcard.setadmitcardavailable", applicationObj);
		            getSqlMapClientTemplate().insert("Admitcard.insertFTPDocumentDetails", studentInfoForAppPDF);
		            	
////////////////////////////////////////////////////////////////////////////////////////	            
	    		}
	          }
	          else{
	          	//return ("No Record found for processing");
	          }
	          
		  } //for loop end here
		} //try end here 
		  catch(Exception e)
		  {
			  System.out.println(e);
		  }
		
		  return("Total Students ="+ applicants.size()+"; Admit Card Generated ="+studentcount);
	}

	private void addCell(Font cellFont, PdfPTable table,
	        String value)
	{
		PdfPCell c1 = new PdfPCell(new Phrase(value, cellFont));
        c1.setBorder(0);

        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);
	}
	
	private void addCellwithborder(Font cellFont, PdfPTable table,
	        String value)
	{
		PdfPCell c1 = new PdfPCell(new Phrase(value, cellFont));
	       // c1.setBorder(2);
	        c1.rectangle(2f, 2f);
	        c1.setFixedHeight(20f);
	        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        c1.setVerticalAlignment(Element.ALIGN_TOP);
	        table.addCell(c1);
	}
	
}
