package in.ac.dei.edrp.admissionsystem.computation;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.ac.dei.edrp.admissionsystem.Bean.BubbleFormBean;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.ReportInfoGetter;
import in.ac.dei.edrp.admissionsystem.Bean.TimeTableBean;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.transferBean;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class computationImpl extends SqlMapClientDaoSupport implements computationDao {

	TransactionTemplate transactionTemplate =null;
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public String runComputation(ReportInfoGetter input) {
		
		 System.out.println("Hello");
		 
		 String eligibility = "Eligible";
		 int start = 1 ;
		 Integer totalstudents = 0;
		 ReportInfoGetter entityInfo = new ReportInfoGetter();  //Bean
		 ReportInfoGetter lastentity = new  ReportInfoGetter(); //BEAN
		 List<ReportInfoGetter> li = null;
		 
		 
		 //computation starts!
		 entityInfo.setProgram_id(input.getProgram_id());
		 entityInfo.setEntity_id(input.getEntity_id());
		 entityInfo.setStart_date(input.getStart_date());
		 entityInfo.setEnd_date(input.getEnd_date());
		 entityInfo.setSubject_code("%XX");
		 entityInfo.setType("A");
		 
		 try 
		 {
			 getSqlMapClientTemplate().update("computation.updateTestNumberBeforeStart", entityInfo);
			 
			li = getSqlMapClientTemplate().queryForList("computation.getapplicants",entityInfo);
			
			if(li.size()>0)
			{
							//////////////////////////////////
							start = 1;
							String registrationNumber = null;
							String message = "You are eligible";
							  double totalscore = 0;
							  double score_without_sp_wt = 0;
							  double finaltotalscore = 0;
							  double finalscore_without_sp_wt = 0;
							  double finaltotalscoreG1 = 0;
							  double finalscore_without_sp_wtG1 = 0;
							  double finaltotalscoreG2 = 0;
							  double finalscore_without_sp_wtG2 = 0;
							///////////////////////////////////
				
				for (ReportInfoGetter entity : li)
				{
					registrationNumber = entity.getRegistration_number();
					String Phyhandicaped = entity.getFlag();
					
				     totalscore = 0;
	                 score_without_sp_wt = 0;
	                 
					if (start==1 )
					 {
						lastentity.setRegistration_number(entity.getRegistration_number()) ;
						lastentity.setComponent_group(entity.getComponent_group());
	                	lastentity.setCos_value(entity.getCos_value());
	                	lastentity.setGender(entity.getGender());
	                	
	                	message = "you are eligible";
	                    eligibility = "Eligible";
	               
						start = start+1;
						
						System.out.println("A"+lastentity.getRegistration_number());
					 }
					if (!(lastentity.getRegistration_number().equalsIgnoreCase(entity.getRegistration_number())))
					{
						entityInfo.setRegistration_number(lastentity.getRegistration_number());
						entityInfo.setCos_value(lastentity.getCos_value());
	                	entityInfo.setGender(lastentity.getGender());
	                	
						// check gender eligibilty and component Eligibility method here 
	                	
	                	/* not required
	                	 ReportInfoGetter getGenderEliibility = (ReportInfoGetter)client.queryForObject("GenderEliibility",entityInfo); //Q5
	                	if ((getGenderEliibility.getStatus().equalsIgnoreCase("InEligible"))&&(getGenderEliibility.getReason_code().equalsIgnoreCase("GenderEligible")))
	                	{
	                		entityInfo.setMessage("InEligible");
	                         entityInfo.setReason_code("GenderEligible");
	                	}
	                	
	                	else
	                	{
	                	}
	                	*/
	                	
	                	
	                		 ReportInfoGetter compEligibility;
							try {
								compEligibility = getcomponenteligibility1(entityInfo);
								entityInfo.setMessage(compEligibility.getMessage());
			                    entityInfo.setReason_code(compEligibility.getReason_code());
							} catch (Exception e) {
								
								e.printStackTrace();
							}   //METHOD-4                          
	                       
	                	
	                         entityInfo.setCalled("N"); 
	                         Integer count=(Integer)getSqlMapClientTemplate().queryForObject("computation.checkDuplicateForComputeMarks", entityInfo);
	                         if (count>0)
								{
	                        	System.out.println("SUM COMP:"+entityInfo.getSum_computed_marks());
	                        	
	                        	getSqlMapClientTemplate().update("computation.updateTestNumber", entityInfo);
	                        	
	                        	
			                            List<ReportInfoGetter> FMC_CompInsert = getSqlMapClientTemplate().queryForList("computation.checkFMCInsert", entityInfo); //Q8
			                            if (FMC_CompInsert.size()==0)
			                            {
			                            	ReportInfoGetter RollNumber =(ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.checkFMCInsert2", entityInfo); //Q9
			                            	entityInfo.setNumber(RollNumber.getNumber());
			                            	getSqlMapClientTemplate().insert("computation.insertcheckFMCInsert", entityInfo); //Q10
			                            }
	                            
	                        	
	                        	
								}
	                         
						System.out.println("B_eligibility logic"+lastentity.getRegistration_number());
						
						
						
						totalstudents++;
						finalscore_without_sp_wt = 0;
	            		finaltotalscore = 0 ;
	            		finaltotalscoreG1 = 0;
	            		finaltotalscoreG2 = 0 ;
	            		finalscore_without_sp_wtG1 = 0 ;
	            		finalscore_without_sp_wtG2 = 0 ;
						lastentity.setRegistration_number(entity.getRegistration_number());
	            		lastentity.setCos_value(entity.getCos_value());
	            		lastentity.setComponent_group(entity.getComponent_group());
	            		lastentity.setGender(entity.getGender());
					}
					
					
					
					entityInfo.setRegistration_number(entity.getRegistration_number());
					System.out.println("Computation Logic"+entityInfo.getRegistration_number()+"-"+entity.getComponent_id());
					
					 double boardPercentage = 0.0;
					 
					      /////////////// no effect of board normalization////////////////
						 if((entity.getBoard_flag().equalsIgnoreCase("Y"))  &&  (entity.getNormalization_factor()>0) )
						 {
		     				boardPercentage = entity.getMarks_percentage()* (entity.getNormalization_factor());	
		     			 }
						 else
						 {
		     				boardPercentage = entity.getMarks_percentage();
		     			 }
	        		
						 
						 
						 if (entity.getWeightage_flag().equalsIgnoreCase("Y"))
						 {
							double val11=0;
	             			val11 =  (boardPercentage*entity.getComponent_Weightage())/100 ;
	             			double d =val11 ;   
	             			long l = (int)Math.round(d * 1000); 
	             			totalscore = l / 1000.00; 
	             			System.out.println("TS:"+totalscore);
	             			score_without_sp_wt = totalscore ;
	             			
	             			
	             			
	             			
	             			
	             			// for dei student and staff weightage not used now //////////////////////////////////////
	             			if ((entity.getSpecial_weightage_flag().equalsIgnoreCase("Y"))&& 
	             					((entity.getStaffward().equalsIgnoreCase("Y"))||
	             							(entity.getDeistudent().equalsIgnoreCase("Y"))))
	             			
	             			      {							
	            					totalscore = totalscore+(totalscore*entity.getWeightage_percentage()/100);
	            			      }
	             			////////////////////////////////////////////////////////////////////////
	             		
						 }
						 else
						 {
	             			totalscore = 0;
	             			score_without_sp_wt = 0;
	             		 }
						 
							entityInfo.setRegistration_number(entity.getRegistration_number());
	                		entityInfo.setComputed_Marks(totalscore);
	                		entityInfo.setActual_computed_Marks(score_without_sp_wt);
	                		entityInfo.setComponent_id(entity.getComponent_id());
						 
							ReportInfoGetter roundoffmarks = (ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.getComputed_marks_roundoff",entityInfo); // Q11
	                 		
							entityInfo.setComputed_Marks_roundoff(roundoffmarks.getComputed_Marks_roundoff());                 
	                		entityInfo.setActual_computed_marks_roundoff(roundoffmarks.getActual_computed_marks_roundoff());   
							System.out.println("roundoffmarks"+roundoffmarks.getComputed_Marks_roundoff());
							getSqlMapClientTemplate().update("computation.updateCallList", entityInfo);	//Q12
	      
					
				}
				
				   // check gender eligibilty and component Eligibility method here 
				   System.out.println("C_eligibility logic"+lastentity.getRegistration_number());
				   entityInfo.setRegistration_number(lastentity.getRegistration_number());
	           	   entityInfo.setCos_value(lastentity.getCos_value());
	           	   entityInfo.setGender(lastentity.getGender());
	           	   
	           	 ReportInfoGetter compEligibility;
				try {
					compEligibility = getcomponenteligibility1(entityInfo);
					entityInfo.setMessage(compEligibility.getMessage());
		            entityInfo.setReason_code(compEligibility.getReason_code());
				} catch (Exception e) {
					
					e.printStackTrace();
				}   //METHOD-4                          
	            
	    	
	             entityInfo.setCalled("N"); 
	             Integer count=(Integer)getSqlMapClientTemplate().queryForObject("computation.checkDuplicateForComputeMarks", entityInfo);
	             if (count>0)
					{
	            	 getSqlMapClientTemplate().update("computation.updateTestNumber", entityInfo); 
	            	// System.out.println("SUM COMP:"+entityInfo.getSum_computed_marks());
	            
					}
	           	   
	           	   
	           	// do some action // gender eligibility , component eligibility, insert FMC
	           	   
	           	totalstudents++;
			}
			else
			{
				System.out.println("Already Computed !!");
			}
			System.out.println(li.size());
			
			getSqlMapClientTemplate().update("computation.updatefinalscore", entityInfo); 
			System.out.println("updatefinalscore");
			
			getSqlMapClientTemplate().update("computation.updatefinalscoreroundoff", entityInfo);  //Q17
	        System.out.println("updatefinalscoreroundoff");
			  
	        getSqlMapClientTemplate().update("computation.updatefinalscore_AS", entityInfo);  //Q18 // program_component group wise computed marks total from (SCL) into academic_score coloum in (STN)
	        System.out.println("updatefinalscore_AS");        //added by atul dayal 06-07-2016
			  
	        getSqlMapClientTemplate().update("computation.updatebestscore", entityInfo);  //Q19 best between  between sum_computed_marks_old  and  sum_computed_marks_roundoff
	        System.out.println("updatebestscore"); 
	        
	        getSqlMapClientTemplate().update("computation.updateET_AS_score", entityInfo);    //Q20  add sum_computed_score amd entrance score and uptate it into (STN) best_score column
	        System.out.println("updateET_AS_score");
			
			
	        getSqlMapClientTemplate().update("computation.updatestudentbycutoffnumber", entityInfo);
		 } 
		 
		 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		  }
		 
		 magicmethod(entityInfo.getProgram_id());  //METHOD-7
	     EnteranceTestEligibility(entityInfo.getProgram_id()); //METHOD-8
	     return totalstudents.toString();
	}
	
	
private ReportInfoGetter  getcomponenteligibility1(ReportInfoGetter entity) throws Exception {
	 	
	 	ReportInfoGetter obj = new ReportInfoGetter();
	 	obj.setStatus("Y");
	 	obj.setMessage("Eligible");
	 	obj.setReason_code("You are Eligible");
	 	
	 	String   phyhndicap  = (String) getSqlMapClientTemplate().queryForObject("computation.getPhyHnddetail");
	 	//String phyhndicap="5";
	 	Double phyhndrlx = Double.parseDouble(phyhndicap)  ;
	 	List<ReportInfoGetter> compulasryEligibility = null;
	 	obj.setStatus("Y");
	 	List<ReportInfoGetter> optionalEligibility = null;
	 	Double ceeligibility;
	 	
	 		
	 	/////age Eligibility/////////////////////////////////////
	 	
	 	/* by passed because it is checked at the time of applying 
	 	List<ReportInfoGetter> ageeligiblity =  client.queryForList("computation.getAgeEligibility",entity);
	 		if (ageeligiblity.size()>0){
	 		String	eligibility = null;System.out.println( "Computing Marks For : "+ entity.getRegistration_number()+ " : "+entity.getGender());	
	 		if(entity.getGender().trim().equalsIgnoreCase("M"))
	 		{
	 			eligibility = getAgeEligibility(entity,ageeligiblity, entity.getRegistration_number());
	 		}
	 		else
	 		{
	 			eligibility = "Eligible";
	 		}
                                
	        if (eligibility.equalsIgnoreCase("InEligible")) {
	             obj.setReason_code("Overage");
	             obj.setMessage("InEligible");
	             return obj;
	             
	         }
	 		}
	 		*/
	 	    /////age Eligibility End/////////////////////////////////////
	 		 
	 		
	 	
	 	
	 		///////////get aggregate eligibility ///////////////////////////////
	 		List<ReportInfoGetter> agregateeligiblity =  getSqlMapClientTemplate().queryForList("computation.getStudentPercentage",entity);
	 		if (agregateeligiblity.size()>0)
	 		{
	 			String	eligibility = null;
	 	
	 			
	 			if(Double.parseDouble(agregateeligiblity.get(0).getAgregate_percentage())>= Double.parseDouble(agregateeligiblity.get(0).getMinimum_agregate()))
	 					{
	 				         obj.setReason_code("You are Eligible");
	                         obj.setMessage("Eligible");
	 				
	 					} else
	 					{
	 					     obj.setReason_code("aggregate percentage less than "+agregateeligiblity.get(0).getMinimum_agregate() );
	 					     obj.setMessage("InEligible");
	 					     return obj;
	 					}
	 		     }
	 		System.out.println("AgregateEligiblity"+entity.getRegistration_number());
	 												
	 	      /////////////aggregate eligibility End ///////////////////////////////
	 			
	 			
	 		
	 		    ///check eligibilty for physical handicaped/////////////////////////////
	     
			 		compulasryEligibility = getSqlMapClientTemplate().queryForList("computation.get_compulsary_eligibility",entity);
					optionalEligibility = getSqlMapClientTemplate().queryForList("computation.get_Optional_eligibility",entity);
					
					System.out.println(optionalEligibility.size());
			 		
			
			 	//check compulsary_eligibility ////////////////////////////////
	 		if(compulasryEligibility.size()>0)
	 		{
	 			
	 		
			 		for (ReportInfoGetter ce:compulasryEligibility)
			 		{
			 			
			 			ceeligibility = ce.getCut_off_number();
			 			
			 			// relaxation for physically handicaped
			 			if(ce.getFlag().equalsIgnoreCase("Y")&&
			 			((entity.getCos_value().substring(0,2).equalsIgnoreCase("GN"))||
			 					(entity.getCos_value().substring(0,2).equalsIgnoreCase("BC"))||
			 					(entity.getCos_value().substring(0,2).equalsIgnoreCase("SC"))||
			 					(entity.getCos_value().substring(0,2).equalsIgnoreCase("ST"))))
			 			 
			 			 {
			         		 ceeligibility = ce.getCut_off_number()-phyhndrlx ;
			 		     }
			 			
			     	if (ce.getMarks_percentage()>=ceeligibility)
			     	{
			     		obj.setStatus("Y") ;
			     		obj.setMessage("Eligible");
			     		obj.setReason_code("You are Eligible");
			     	}
			     	else
			     	{
			     		obj.setStatus("N") ;
			     		obj.setMessage("InEligible");
			     		obj.setReason_code("OutInCompulsaryEligibilty");
			     		obj.setReason_code("COM:"+getComponentDescription(ce.getComponent_id(), ce.getRegistration_number(), entity) +" < " +
			                  getComponetEligibleMarks(entity.getComponent_id(),ce.getRegistration_number(), entity));
			     		return obj;
			     		
			     	}
			     }
			 		System.out.println("Compulsary_Eligibility");
	 		}
	 		
	 		//check compulsary_eligibility End////////////////////////////////
	 		
	 		
	 		//check Optional_eligibility  ////////////////////////////////
	 		if(optionalEligibility.size()>0)
	 		{
	 			System.out.println("hello");
	 			for (ReportInfoGetter ce:optionalEligibility)
	 			{
				 	ceeligibility = ce.getCut_off_number();
				 	System.out.println("-"+entity.getCos_value());
				 // relaxation for physically handicaped
				 	if(ce.getFlag().equalsIgnoreCase("Y")&&
	                  ((entity.getCos_value().substring(0,2).equalsIgnoreCase("GN"))||
				             				(entity.getCos_value().substring(0,2).equalsIgnoreCase("BC"))||
						 					(entity.getCos_value().substring(0,2).equalsIgnoreCase("SC"))||
						 					(entity.getCos_value().substring(0,2).equalsIgnoreCase("ST"))))
				 	   
				 	        {
				              ceeligibility = ce.getCut_off_number()-phyhndrlx ;
				     		}
				 			if (ce.getMarks_percentage()>=ceeligibility)
				 			{
					     		obj.setStatus("Y") ;
					     		obj.setMessage("Eligible");
					     		obj.setReason_code("You are Eligible");
					     		return obj;
				     	    }
				 			else
				     	    {
					     		obj.setStatus("N");
					     		obj.setMessage("InEligible");
					     		obj.setReason_code("OutInOptionalEligibilty");
					     		obj.setReason_code("OPT:"+getComponentDescription(ce.getComponent_id(), ce.getRegistration_number(), entity) +" < " +
					                    getComponetEligibleMarks(entity.getComponent_id(),ce.getRegistration_number(), entity));
				     		 }
	          }
	 			
	 			if (obj.getStatus().equalsIgnoreCase("N"))
	 			{
	 				return obj;
	 			}
	 			
	 			System.out.println("Optional_Eligibility");
		
	 }
	 		
	 		//check Optional_eligibility END ///////////////////////
	 		
	 		return obj;
	 }
	
 
 public String getComponentDescription(String component_id, String regnum, ReportInfoGetter entityInfo)
 {
     String description = null;
     try
     {
         ReportInfoGetter desc = entityInfo;
         desc.setComponent_id(component_id);
         List li;
			li = getSqlMapClientTemplate().queryForList("computation.getComponentDescription", desc);
      
         Iterator iterator = li.iterator();
         while( iterator.hasNext())
         {
             ReportInfoGetter descr = (ReportInfoGetter)iterator.next();
             description = descr.getComponent_description();
         }

     }
     catch(Exception e)
     {
         //logObj.logger.info((new StringBuilder("Error while getting Component Descrition for : ")).append(component_id).append(", registration").append(regnum).toString());
         //logObj.logger.info((new StringBuilder("Exception is:  ")).append(e.getMessage()).toString());
     }
     return description;
 }
 
 
 public double getComponetEligibleMarks(String component_id, String registration_number, ReportInfoGetter entityInfo)
 {
     double marks = 0.0D;
     try
     {
         ReportInfoGetter comp_marks = entityInfo;
         comp_marks.setComponent_id(component_id);
         comp_marks.setCategory(entityInfo.getCos_value().substring(0,2));
         //comp_marks.setCategory(getCategoryId(registration_number, entityInfo));
         List li = getSqlMapClientTemplate().queryForList("computation.getEligible", comp_marks);
         Iterator iterator = li.iterator();
         while( iterator.hasNext())
         {
             ReportInfoGetter el_marks = (ReportInfoGetter)iterator.next();
             marks = el_marks.getComponent_eligiblity();
         }

     }
     catch(Exception e)
     {
         //logObj.logger.info((new StringBuilder("Error while getting eligible for component marks for ")).append(registration_number).toString());
     }
     return marks;
 }
 
 //METHOD-7
 private void magicmethod( String program_id)
 {
 	ReportInfoGetter enInfo =new ReportInfoGetter();
 	enInfo.setProgram_id(program_id);
 	try 
 	{
 	 ReportInfoGetter cutoffupdate = (ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.getcalledapplicants",enInfo);
      
 	 System.out.println("cutoffupdate.getNumber()"+cutoffupdate.getNumber());
      if (cutoffupdate.getNumber().equalsIgnoreCase("4"))
      {
    	         System.out.println("Inside :updateCalledApplicants_cutoff_based_on_Category"+enInfo.getProgram_id() );
    	         try 
    	            {
    	             getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_based_on_Category", enInfo);
    	             System.out.println("updateCalledApplicants_cutoff_based_on_Category");
    	            }
    	          catch (Exception e)
    	            {
    		         System.out.println("Error"+e);
    	            }
      }
      
      else if (cutoffupdate.getNumber().equalsIgnoreCase("8"))
      {
    	  
   	  System.out.println("Inside :updateCalledApplicants_cutoff_based_on_Category_Gender"+enInfo.getProgram_id());
  	  
   	
   	          try 
    	           {  
    		  
    	            getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_based_on_Category_Gender",enInfo);
    	            System.out.println("updateCalledApplicants_cutoff_based_on_Category_Gender");
    	          }
    	          catch(Exception e)
    	          {
    	            System.out.println("error"+e ); 
    	          }
      }
       else
             {
    	           System.out.println("no record in program_cut_off for program_id-"+enInfo.getProgram_id());
             }
 	}
 	catch (Exception e)
 	{
 		 System.out.println("error"+e ); 
 	}
 }
 
 //METHOD-8
 private void EnteranceTestEligibility(String Program_id)
 {
 	ReportInfoGetter enInfo =new ReportInfoGetter();
 	   try 
	           {  
 		   ReportInfoGetter getCatCutOff = (ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.getCatCutOff",enInfo);
 		   
 		   
 		   enInfo.setProgram_id(Program_id);
 		   enInfo.setBC(getCatCutOff.getBC());
 		   enInfo.setGN(getCatCutOff.getGN());
 		   enInfo.setSC(getCatCutOff.getSC());
 		   enInfo.setST(getCatCutOff.getST());
 		   
	            getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_ET_CAT_BC",enInfo);
	            getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_ET_CAT_GN",enInfo);
	            getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_ET_CAT_SC",enInfo);
	            getSqlMapClientTemplate().update("computation.updateCalledApplicants_cutoff_ET_CAT_ST",enInfo);
				
				
	             getSqlMapClientTemplate().update("computation.updateCalledApplicants_BlankET",enInfo); //added by atul dayal 06-07-2016
	             getSqlMapClientTemplate().update("computation.updateCalledApplicants_AbsentInET",enInfo); //added by atul dayal 09-08-2020
	             
	             System.out.println("updateCalledApplicants_cutoff_based_on_Category_Gender");
				
				
	            System.out.println("updateCalledApplicants_cutoff_based_on_Category_Gender");
	          }
	          catch(Exception e)
	          {
	            System.out.println("error"+e ); 
	          }
	         
 }

 
 
 
 
 
 public String finalMeritListProcess(ReportInfoGetter input)
 {
	 ReportInfoGetter reportBeen=new ReportInfoGetter();
	 
	    String message = "";
	    reportBeen.setUniversity_id("0001");
		reportBeen.setEntity_id(input.getEntity_id());
		reportBeen.setProgram_id(input.getProgram_id());
		reportBeen.setUser_id("");
		reportBeen.setUniversity_start_date(input.getStart_date());
		reportBeen.setUniversity_end_date(input.getEnd_date());

		
		try
		{
			getSqlMapClientTemplate().update("computation.deletestudentfinalmeritlist",reportBeen);
			
			List<ReportInfoGetter>regList=getSqlMapClientTemplate().queryForList("computation.getRegisteredStudent", reportBeen);
			System.out.println("regList:"+regList.size());
			//client.update("computation.updateStudentFinalMarksforMerit",reportBeen);
			
			double totalscore =0;
			
			if(regList.size()>0)
			{
				int totalStudents=regList.size();
				int recordProcessed=0;
					for(int i=0;i<regList.size();i++)
					{
						totalscore = 0;
						reportBeen.setRegistration_number(regList.get(i).getRegistration_number());	
						//List<ReportInfoGetter>compList=client.queryForList("computation.getComponentDetail", reportBeen);
						
						List<ReportInfoGetter>Academicscore=getSqlMapClientTemplate().queryForList("computation.getAcademicScoreAll", reportBeen);
						List<ReportInfoGetter> NonAcademicscore=getSqlMapClientTemplate().queryForList("computation.getNonAcademicScore", reportBeen);
						
						if(Academicscore.size()>0)
						{
							totalscore= Academicscore.get(0).getTotalscore();
						}
						if(NonAcademicscore.size()>0)
						{
							totalscore=totalscore+ NonAcademicscore.get(0).getTotalscore();
						}
						
						double totalComponentMarks=0.0;
						double academicScore=0.0;
						boolean flag=false;
						reportBeen.setTest_number(regList.get(i).getTest_number());
						reportBeen.setCos_value(regList.get(i).getCos_value());							
						reportBeen.setTotal_marks(totalscore);
						getSqlMapClientTemplate().insert("computation.insertIntoFinalMeritList", reportBeen);
						recordProcessed++;
						
						
						
					}
					message="success-"+String.valueOf(totalStudents)+"-"+String.valueOf(recordProcessed)+"-"+String.valueOf((totalStudents-recordProcessed));
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return message;
	 
 }


 
 public String finalMeritListProcessforAll(ReportInfoGetter input)
 {
	 ReportInfoGetter reportBeen=new ReportInfoGetter();
	 
	 String message = "";

	 
		try
		{
			List<ReportInfoGetter> programList =getSqlMapClientTemplate().queryForList("computation.getProgramList");
			 
			for (int l1=0;l1<programList.size();l1++)
			{
				
			
			    reportBeen.setUniversity_id("0001");
				reportBeen.setEntity_id(programList.get(l1).getEntity_id());
				reportBeen.setProgram_id(programList.get(l1).getProgram_id());
				reportBeen.setUser_id("");
				reportBeen.setUniversity_start_date(programList.get(l1).getStart_date());
				reportBeen.setUniversity_end_date(programList.get(l1).getEnd_date());
				
			getSqlMapClientTemplate().update("computation.deletestudentfinalmeritlist",reportBeen);
			
			List<ReportInfoGetter>regList=getSqlMapClientTemplate().queryForList("computation.getRegisteredStudent", reportBeen);
			System.out.println("regList:"+regList.size());
			//client.update("computation.updateStudentFinalMarksforMerit",reportBeen);
			
			double totalscore =0;
			
			if(regList.size()>0)
			{
				int totalStudents=regList.size();
				int recordProcessed=0;
					for(int i=0;i<regList.size();i++)
					{
						totalscore = 0;
						reportBeen.setRegistration_number(regList.get(i).getRegistration_number());	
						//List<ReportInfoGetter>compList=client.queryForList("computation.getComponentDetail", reportBeen);
						
						List<ReportInfoGetter>Academicscore=getSqlMapClientTemplate().queryForList("computation.getAcademicScoreAll", reportBeen);
						List<ReportInfoGetter> NonAcademicscore=getSqlMapClientTemplate().queryForList("computation.getNonAcademicScore", reportBeen);
						
						if(Academicscore.size()>0)
						{
							totalscore= Academicscore.get(0).getTotalscore();
						}
						if(NonAcademicscore.size()>0)
						{
							totalscore=totalscore+ NonAcademicscore.get(0).getTotalscore();
						}
						
						double totalComponentMarks=0.0;
						double academicScore=0.0;
						boolean flag=false;
						reportBeen.setTest_number(regList.get(i).getTest_number());
						reportBeen.setCos_value(regList.get(i).getCos_value());							
						reportBeen.setTotal_marks(totalscore);
						getSqlMapClientTemplate().insert("computation.insertIntoFinalMeritList", reportBeen);
						recordProcessed++;
						
						List<ReportInfoGetter> list1 = new ArrayList<ReportInfoGetter>();
						
						list1=getSqlMapClientTemplate().queryForList("computation.getCouncellingData", reportBeen);
						
						
						if (list1.size()>0)
						{
						    if (list1.get(0).getFlag().equalsIgnoreCase("UPD"))
							{
						    	
						    	getSqlMapClientTemplate().update("computation.UpdateCouncellingData", reportBeen);
						    	getSqlMapClientTemplate().update("computation.UpdateCouncellingDataINSCL", reportBeen);
							}
						}
						else
						{
							getSqlMapClientTemplate().insert("computation.InsertCouncellingData", reportBeen);
						}
						
						
						
					}
					message="success-"+String.valueOf(totalStudents)+"-"+String.valueOf(recordProcessed)+"-"+String.valueOf((totalStudents-recordProcessed));
			}
			
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
			message=e.toString();
		}
		return message;
	 
 }
 
 
public List<admissionBean> getCustomeGridData(admissionBean input) 

{
	List<admissionBean> getData = new ArrayList<admissionBean>();
	if (input.getFlag().equalsIgnoreCase("A"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getprogram_components", input); 
	}
	else if (input.getFlag().equalsIgnoreCase("B"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getprogram_component_eligibility", input);
	}
	else if (input.getFlag().equalsIgnoreCase("C"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getfinal_merit_components", input);
	}
	else if (input.getFlag().equalsIgnoreCase("ML"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getfinal_merit_List", input);
	}
	else if (input.getFlag().equalsIgnoreCase("MLA"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getfinal_merit_ListA", input);
	}
	else if (input.getFlag().equalsIgnoreCase("MLA1"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getfinal_merit_ListA1", input);
	}
	else if (input.getFlag().equalsIgnoreCase("MLA2"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getfinal_merit_ListA2", input);
	}
	else if(input.getFlag().equalsIgnoreCase("WRONGAPP"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.WRONGAPP_ETmarks", input);
	}
	else if(input.getFlag().equalsIgnoreCase("DPLAPP"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.DPLAPP_ETmarks", input);
	}
	
	else if(input.getFlag().equalsIgnoreCase("WRET"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.WRONGAPP_ETmarksWithAPP", input);
	}
	else if(input.getFlag().equalsIgnoreCase("VIEW_ET_MARKS"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.viewETmarks", input);
	}
	else if(input.getFlag().equalsIgnoreCase("viewMarks"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.viewStudentMarks", input);
	}
	else if(input.getFlag().equalsIgnoreCase("viewMarksforCounselling"))
	{
		if (input.getProgram_id().equalsIgnoreCase("0001067"))
		{
			getData=getSqlMapClientTemplate().queryForList("computation.viewStudentMarksforCounselling_btech", input);
		}
		else
		{
			getData=getSqlMapClientTemplate().queryForList("computation.viewStudentMarksforCounselling", input);
		}
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TT-ROOM"))
	{
		
		getData=getSqlMapClientTemplate().queryForList("computation.getRoomsforTT", input);
	}
	
	else if(input.getFlag().equalsIgnoreCase("TT-Teacher"))
	{
		getData=getSqlMapClientTemplate().queryForList("computation.getTeacherforTT", input);
		
	}
	else if(input.getFlag().equalsIgnoreCase("TT-JointLectures"))
	{
        List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			
			System.out.println("getData"+input.getSemester_start_date());
			System.out.println("getData"+input.getSemester_end_date());
			System.out.println("getData"+input.getCourse_code());
			System.out.println("getData"+input.getProgram_id());
			
			getData=getSqlMapClientTemplate().queryForList("computation.getJointLecturesData", input);
			System.out.println("getData"+getData.size());
		}
		else if (semDates.size()==0)
		{
		 semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
		 
			 if (semDates.size()>0)
			 {
				   input.setSemester_start_date(semDates.get(0).getSemester_start_date());
					input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			 }
			getData=getSqlMapClientTemplate().queryForList("computation.getJointLecturesDataOld", input);
		}
	}
	
	
	
	else if(input.getFlag().equalsIgnoreCase("TTView"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		input.setSemester_start_date(semDates.get(0).getSemester_start_date());
		input.setSemester_end_date(semDates.get(0).getSemester_end_date());
		
		getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTT", input);
		
	}
	else if(input.getFlag().equalsIgnoreCase("TTViewforEdit"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		
		System.out.println(input.getProgram_id());
		System.out.println(input.getCourse_code());
		System.out.println(input.getSemester_code());
		
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEdit", input);
		}
		else if (semDates.size()==0)
		{
		 semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
		 
			 if (semDates.size()>0)
			 {
				   input.setSemester_start_date(semDates.get(0).getSemester_start_date());
					input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			 }
		 
			
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditOld", input);
		}
		
		
		
		System.out.println(input.getSemester_start_date());
		System.out.println(input.getSemester_end_date());
		
		
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TTViewforEditPCKwise"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		
		System.out.println(input.getProgram_id());
		System.out.println(input.getCourse_code());
		System.out.println(input.getSemester_code());
		
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		
		
		if(semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditpckWise", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			
			if (semDates.size()>0)
			{
				input.setSemester_start_date(semDates.get(0).getSemester_start_date());
				input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			}
			
			
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditpckWiseOld", input);
		}
		System.out.println(input.getSemester_start_date());
		System.out.println(input.getSemester_end_date());
		
		
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TTRoom"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		if(semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getRoomDataforTT", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getRoomDataforTT", input);
		}
			
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TT-Course"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEdit", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditOld", input);
		}
		
		
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TT-CourseBS"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		
		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditBS", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditBSOld", input);
		}
		
		
	}
	
	else if(input.getFlag().equalsIgnoreCase("TT-CourseGS"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		
		System.out.println(input.getProgram_id());
		System.out.println(input.getCourse_code());
		System.out.println(input.getSemester_code());
		
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		

		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditGS", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditGSOld", input);
		}
		
		
		
		System.out.println(input.getSemester_start_date());
		System.out.println(input.getSemester_end_date());
		
		
		
	}
	
	
	else if(input.getFlag().equalsIgnoreCase("TT-CourseGB"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		
		System.out.println(input.getProgram_id());
		System.out.println(input.getCourse_code());
		System.out.println(input.getSemester_code());
		
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		
		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditGB", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditGBOld", input);
		}
		
		System.out.println(input.getSemester_start_date());
		System.out.println(input.getSemester_end_date());
		
		
		
	}
	
	
	
	else if(input.getFlag().equalsIgnoreCase("TT-CourseDS"))
	{
		System.out.println(input.getPck());
		System.out.println(input.getStart_date());
		System.out.println(input.getEnd_date());
		System.out.println(input.getEntity_id());
		
		System.out.println(input.getProgram_id());
		System.out.println(input.getCourse_code());
		System.out.println(input.getSemester_code());
		
		List<admissionBean> semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTT", input);
		

		if (semDates.size()>0)
		{
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditDS", input);
		}
		else if (semDates.size()==0)
		{
			semDates=getSqlMapClientTemplate().queryForList("computation.getSemDateforTTOld", input);
			input.setSemester_start_date(semDates.get(0).getSemester_start_date());
			input.setSemester_end_date(semDates.get(0).getSemester_end_date());
			getData=getSqlMapClientTemplate().queryForList("computation.getCourseInfoForTTforEditDSOld", input);
		}
		
		System.out.println(input.getSemester_start_date());
		System.out.println(input.getSemester_end_date());
		
		
		
	}
	
	else if (input.getFlag().equalsIgnoreCase("InsertroomData"))
	{
		List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkRoomCourseMapping", input);
		
		if(checkData.size()==0)
		{
			getSqlMapClientTemplate().insert("computation.InsertRoomCourseMapping", input);
		}
	
	}
	
	else if(input.getFlag().equalsIgnoreCase("InsertTeacherData"))
	{
		
	List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkTeacherCourseMapping", input);
		
		if(checkData.size()==0)
		{
			getSqlMapClientTemplate().insert("computation.InsertTeacherCourseMapping", input);
		}
	}
	
	
	else if(input.getFlag().equalsIgnoreCase("InsertTimeTableRowData"))
	{
      List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkTimeTableCourseInfo", input);
		
		if(checkData.size()==0)
		{
			getSqlMapClientTemplate().insert("computation.InsertTimeTableCourseInfo", input);
		}
		else
		{
			getSqlMapClientTemplate().update("computation.updateTimeTableCourseInfo", input);
		}
	}
	
	else if(input.getFlag().equalsIgnoreCase("InsertTimeTableNewData"))
	{
      List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkTimeTableCourseInfo", input);
		
		if(checkData.size()==0)
		{
			getSqlMapClientTemplate().insert("computation.InsertTimeTableCourseInfo", input);
			getSqlMapClientTemplate().insert("computation.InsertTimeTableCourseInfoNewCourse", input);
		}
		else
		{
			getSqlMapClientTemplate().update("computation.updateTimeTableCourseInfo", input);
			getSqlMapClientTemplate().update("computation.updateTimeTableCourseInfoNewCourse", input);
		}
	}
	
	
	else if(input.getFlag().equalsIgnoreCase("InsertSectionData"))
	{
		
		System.out.println(input.getSection()+"--"+input.getSectionType()+"--"+input.getGender()+"--"+input.getCapacity());
		System.out.println(input.getCourse_code()+input.getSection()+input.getSectionType()+input.getPck());
		input.setParam1(input.getCourse_code()+input.getSection()+input.getSectionType()+input.getPck());
      List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkTimeTableCourseSection", input);	
		if(checkData.size()==0)
		{
			getSqlMapClientTemplate().delete("computation.DeleteTimeTableCourseSection", input);
			getSqlMapClientTemplate().insert("computation.InsertTimeTableCourseSection", input);
		}
		else
		{
			getSqlMapClientTemplate().update("computation.updateTimeTableCourseSection", input);
		}
	}
	
	else if(input.getFlag().equalsIgnoreCase("InsertJointLectures"))
	{
		
      List<admissionBean> checkData=getSqlMapClientTemplate().queryForList("computation.checkTimeTableJointlecture", input);	
		if(checkData.size()==0)
		{
			//getSqlMapClientTemplate().delete("computation.DeleteTimeTableJointlecture", input);
			getSqlMapClientTemplate().insert("computation.InsertTimeTableJointlecture", input);
		}
		else
		{
			getSqlMapClientTemplate().update("computation.updateTimeTableJointlecture", input);
		}
	}
	
	return getData;
}


public String runComputationforAll(ReportInfoGetter input) {
	

	
	 System.out.println("Hello");
	 
	 String eligibility = "Eligible";
	 int start = 1 ;
	 Integer totalstudents = 0;
	 ReportInfoGetter entityInfo = new ReportInfoGetter();  //Bean
	 ReportInfoGetter lastentity = new  ReportInfoGetter(); //BEAN
	 List<ReportInfoGetter> li = null;
	 List<ReportInfoGetter> programWise = null;
	 
	 programWise = getSqlMapClientTemplate().queryForList("computation.getProgramList",entityInfo);
	 
	 for (int PI=0; PI<programWise.size();PI++)
	 {
	 //computation starts!
		 
		 entityInfo.setProgram_id(programWise.get(PI).getProgram_id());
		 entityInfo.setEntity_id(programWise.get(PI).getEntity_id());
		 entityInfo.setStart_date(programWise.get(PI).getStart_date());
		 entityInfo.setEnd_date(programWise.get(PI).getEnd_date());
		 
	// entityInfo.setProgram_id(input.getProgram_id());
	// entityInfo.setEntity_id(input.getEntity_id());
	 //entityInfo.setStart_date(input.getStart_date());
	 //entityInfo.setEnd_date(input.getEnd_date());
	 entityInfo.setSubject_code("%XX");
	 entityInfo.setType("A");
	 
	 try 
	 {
		 getSqlMapClientTemplate().update("computation.updateTestNumberBeforeStart", entityInfo);
		 
		li = getSqlMapClientTemplate().queryForList("computation.getapplicants",entityInfo);
		
		if(li.size()>0)
		{
						//////////////////////////////////
						start = 1;
						String registrationNumber = null;
						String message = "You are eligible";
						  double totalscore = 0;
						  double score_without_sp_wt = 0;
						  double finaltotalscore = 0;
						  double finalscore_without_sp_wt = 0;
						  double finaltotalscoreG1 = 0;
						  double finalscore_without_sp_wtG1 = 0;
						  double finaltotalscoreG2 = 0;
						  double finalscore_without_sp_wtG2 = 0;
						///////////////////////////////////
			
			for (ReportInfoGetter entity : li)
			{
				registrationNumber = entity.getRegistration_number();
				String Phyhandicaped = entity.getFlag();
				
			     totalscore = 0;
                score_without_sp_wt = 0;
                
				if (start==1 )
				 {
					lastentity.setRegistration_number(entity.getRegistration_number()) ;
					lastentity.setComponent_group(entity.getComponent_group());
               	lastentity.setCos_value(entity.getCos_value());
               	lastentity.setGender(entity.getGender());
               	
               	message = "you are eligible";
                   eligibility = "Eligible";
              
					start = start+1;
					
					System.out.println("A"+lastentity.getRegistration_number());
				 }
				if (!(lastentity.getRegistration_number().equalsIgnoreCase(entity.getRegistration_number())))
				{
					entityInfo.setRegistration_number(lastentity.getRegistration_number());
					entityInfo.setCos_value(lastentity.getCos_value());
               	entityInfo.setGender(lastentity.getGender());
               	
					// check gender eligibilty and component Eligibility method here 
               	
               	/* not required
               	 ReportInfoGetter getGenderEliibility = (ReportInfoGetter)client.queryForObject("GenderEliibility",entityInfo); //Q5
               	if ((getGenderEliibility.getStatus().equalsIgnoreCase("InEligible"))&&(getGenderEliibility.getReason_code().equalsIgnoreCase("GenderEligible")))
               	{
               		entityInfo.setMessage("InEligible");
                        entityInfo.setReason_code("GenderEligible");
               	}
               	
               	else
               	{
               	}
               	*/
               	
               	
               		 ReportInfoGetter compEligibility;
						try {
							compEligibility = getcomponenteligibility1(entityInfo);
							entityInfo.setMessage(compEligibility.getMessage());
		                    entityInfo.setReason_code(compEligibility.getReason_code());
						} catch (Exception e) {
							
							e.printStackTrace();
						}   //METHOD-4                          
                      
               	
                        entityInfo.setCalled("N"); 
                        Integer count=(Integer)getSqlMapClientTemplate().queryForObject("computation.checkDuplicateForComputeMarks", entityInfo);
                        if (count>0)
							{
                       	System.out.println("SUM COMP:"+entityInfo.getSum_computed_marks());
                       	
                       	getSqlMapClientTemplate().update("computation.updateTestNumber", entityInfo);
                       	
                       	
		                            List<ReportInfoGetter> FMC_CompInsert = getSqlMapClientTemplate().queryForList("computation.checkFMCInsert", entityInfo); //Q8
		                            if (FMC_CompInsert.size()==0)
		                            {
		                            	ReportInfoGetter RollNumber =(ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.checkFMCInsert2", entityInfo); //Q9
		                            	entityInfo.setNumber(RollNumber.getNumber());
		                            	getSqlMapClientTemplate().insert("computation.insertcheckFMCInsert", entityInfo); //Q10
		                            }
                           
                       	
                       	
							}
                        
					System.out.println("B_eligibility logic"+lastentity.getRegistration_number());
					
					
					
					totalstudents++;
					finalscore_without_sp_wt = 0;
           		finaltotalscore = 0 ;
           		finaltotalscoreG1 = 0;
           		finaltotalscoreG2 = 0 ;
           		finalscore_without_sp_wtG1 = 0 ;
           		finalscore_without_sp_wtG2 = 0 ;
					lastentity.setRegistration_number(entity.getRegistration_number());
           		lastentity.setCos_value(entity.getCos_value());
           		lastentity.setComponent_group(entity.getComponent_group());
           		lastentity.setGender(entity.getGender());
				}
				
				
				
				entityInfo.setRegistration_number(entity.getRegistration_number());
				System.out.println("Computation Logic"+entityInfo.getRegistration_number()+"-"+entity.getComponent_id());
				
				 double boardPercentage = 0.0;
				 
				      /////////////// no effect of board normalization////////////////
					 if((entity.getBoard_flag().equalsIgnoreCase("Y"))  &&  (entity.getNormalization_factor()>0) )
					 {
	     				boardPercentage = entity.getMarks_percentage()* (entity.getNormalization_factor());	
	     			 }
					 else
					 {
	     				boardPercentage = entity.getMarks_percentage();
	     			 }
       		
					 
					 
					 if (entity.getWeightage_flag().equalsIgnoreCase("Y"))
					 {
						double val11=0;
            			val11 =  (boardPercentage*entity.getComponent_Weightage())/100 ;
            			double d =val11 ;   
            			long l = (int)Math.round(d * 1000); 
            			totalscore = l / 1000.00; 
            			System.out.println("TS:"+totalscore);
            			score_without_sp_wt = totalscore ;
            			
            			
            			
            			
            			
            			// for dei student and staff weightage not used now //////////////////////////////////////
            			if ((entity.getSpecial_weightage_flag().equalsIgnoreCase("Y"))&& 
            					((entity.getStaffward().equalsIgnoreCase("Y"))||
            							(entity.getDeistudent().equalsIgnoreCase("Y"))))
            			
            			      {							
           					totalscore = totalscore+(totalscore*entity.getWeightage_percentage()/100);
           			      }
            			////////////////////////////////////////////////////////////////////////
            		
					 }
					 else
					 {
            			totalscore = 0;
            			score_without_sp_wt = 0;
            		 }
					 
						entityInfo.setRegistration_number(entity.getRegistration_number());
               		entityInfo.setComputed_Marks(totalscore);
               		entityInfo.setActual_computed_Marks(score_without_sp_wt);
               		entityInfo.setComponent_id(entity.getComponent_id());
					 
						ReportInfoGetter roundoffmarks = (ReportInfoGetter)getSqlMapClientTemplate().queryForObject("computation.getComputed_marks_roundoff",entityInfo); // Q11
                		
						entityInfo.setComputed_Marks_roundoff(roundoffmarks.getComputed_Marks_roundoff());                 
               		entityInfo.setActual_computed_marks_roundoff(roundoffmarks.getActual_computed_marks_roundoff());   
						System.out.println("roundoffmarks"+roundoffmarks.getComputed_Marks_roundoff());
						getSqlMapClientTemplate().update("computation.updateCallList", entityInfo);	//Q12
     
				
			}
			
			   // check gender eligibilty and component Eligibility method here 
			   System.out.println("C_eligibility logic"+lastentity.getRegistration_number());
			   entityInfo.setRegistration_number(lastentity.getRegistration_number());
          	   entityInfo.setCos_value(lastentity.getCos_value());
          	   entityInfo.setGender(lastentity.getGender());
          	   
          	 ReportInfoGetter compEligibility;
			try {
				compEligibility = getcomponenteligibility1(entityInfo);
				entityInfo.setMessage(compEligibility.getMessage());
	            entityInfo.setReason_code(compEligibility.getReason_code());
			} catch (Exception e) {
				
				e.printStackTrace();
			}   //METHOD-4                          
           
   	
            entityInfo.setCalled("N"); 
            Integer count=(Integer)getSqlMapClientTemplate().queryForObject("computation.checkDuplicateForComputeMarks", entityInfo);
            if (count>0)
				{
           	 getSqlMapClientTemplate().update("computation.updateTestNumber", entityInfo); 
           	// System.out.println("SUM COMP:"+entityInfo.getSum_computed_marks());
           
				}
          	   
          	   
          	// do some action // gender eligibility , component eligibility, insert FMC
          	   
          	totalstudents++;
		}
		else
		{
			System.out.println("Already Computed !!");
		}
		System.out.println(li.size());
		
		getSqlMapClientTemplate().update("computation.updatefinalscore", entityInfo); 
		System.out.println("updatefinalscore");
		
		getSqlMapClientTemplate().update("computation.updatefinalscoreroundoff", entityInfo);  //Q17
       System.out.println("updatefinalscoreroundoff");
		  
       getSqlMapClientTemplate().update("computation.updatefinalscore_AS", entityInfo);  //Q18 // program_component group wise computed marks total from (SCL) into academic_score coloum in (STN)
       System.out.println("updatefinalscore_AS");        //added by atul dayal 06-07-2016
		  
       getSqlMapClientTemplate().update("computation.updatebestscore", entityInfo);  //Q19 best between  between sum_computed_marks_old  and  sum_computed_marks_roundoff
       System.out.println("updatebestscore"); 
       
       getSqlMapClientTemplate().update("computation.updateET_AS_score", entityInfo);    //Q20  add sum_computed_score amd entrance score and uptate it into (STN) best_score column
       System.out.println("updateET_AS_score");
		
		
       getSqlMapClientTemplate().update("computation.updatestudentbycutoffnumber", entityInfo);
	 } 
	 
	 
	 catch (Exception e) 
	 {
		e.printStackTrace();
	  }
	 
	 magicmethod(entityInfo.getProgram_id());  //METHOD-7
    EnteranceTestEligibility(entityInfo.getProgram_id()); //METHOD-8
}
    return totalstudents.toString();

}



/*
public transferBean transferApp( transferBean input) {

	   
			
			transferBean transferstudent= new transferBean();
			transferBean inp1= new transferBean();
			
			List<transferBean> transferProgramList= new ArrayList<transferBean>();
			List <transferBean> paperoption1 =new ArrayList<transferBean>();
			List <String> COURSES =new ArrayList<String>();
		    List <String> Distinct =new ArrayList<String>();
		    List <String> pap1 =new ArrayList<String>();
		    List <String> pap2 =new ArrayList<String>();
		    List <String> pap3 =new ArrayList<String>();
		    List <String> pap4 =new ArrayList<String>();
		    List <String> pap5 =new ArrayList<String>();
		    List <String> programList =new ArrayList<String>();
		    String pasdob;
		    
		    
		    transferstudent = (transferBean)getSqlMapClientTemplate().queryForObject("transfer.checkfortransferapp",input);
			transferstudent.setTransferFlag(input.getTransferFlag());
			transferstudent.setAppnumber(input.getTransferapp());
			transferstudent.setFromprogram(input.getFromprogram());
			transferstudent.setActualApplication(input.getActualApplication());
			
			if (transferstudent.getTransferFlag().equalsIgnoreCase("1"))
			{
				transferstudent.setToprogram(input.getToprogram());
				
				programList.add(transferstudent.getToprogram());

				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("2"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("3"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("4"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				transferstudent.setToprogram3(input.getToprogram4());
				
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				programList.add(transferstudent.getToprogram4());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("5"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				transferstudent.setToprogram3(input.getToprogram4());
				transferstudent.setToprogram3(input.getToprogram5());
				
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				programList.add(transferstudent.getToprogram4());
				programList.add(transferstudent.getToprogram5());
				
			}
		    
			
			BubbleFormBean EntitySession = (BubbleFormBean) getSqlMapClientTemplate().queryForObject("bubbleform.EntitySession");      
		    transferstudent.setSession(EntitySession.getSession());
		    transferstudent.setStartdate(EntitySession.getStartdate());
		    transferstudent.setEnddate(EntitySession.getEnddate());
		    
		    pasdob=transferstudent.getDob().substring(0, 4);
		    String na1= transferstudent.getCandidateName().substring(0,3);
			 String asd =na1+pasdob;
			 transferstudent.setPassword(asd);
			
			
			transferstudent.setFee("0");
			transferstudent.setUniversityId("0001");
			transferstudent.setCos(transferstudent.getCategory()+"XX");
			transferstudent.setCosval(transferstudent.getCategory());
			
			try
			{
		
				 List<transferBean> checkApplication = new ArrayList<transferBean>();
				 checkApplication =getSqlMapClientTemplate().queryForList("transfer.checkApplication",transferstudent);
			
						if (checkApplication.size()==0)
						{
							
							try 
							{
							
								input.setUgpg("STUDID");
							input .setUniversityId("0001");
							input.setStudentId(getStudentId(input));
						    transferstudent.setStudentId(input.getStudentId());
						    
						    System.out.println("getStudentId:"+transferstudent.getStudentId());
							
							System.out.println("getRegistration:"+transferstudent.getCandidateName());
						    
							getSqlMapClientTemplate().insert("bubbleform.setApplicantAccountInfo",transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setEntityStudentUserform", transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setStudentAddressUserFormtransfer", transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setStudentAddressUserFormtransferForPER", transferstudent);
							
							
							System.out.println(programList.size());
							
							for (int k=0 ; k<programList.size();k++)
							{
								String programId= programList.get(k).substring(0,7);
								transferstudent.setProgramName(programId);
								
								System.out.println(k+"-"+programId);
								
							
								
								transferBean getentity = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getentityid",transferstudent);
								
								transferstudent.setProgram_id(programId);
								transferstudent.setEntityId(getentity.getEntityId());
								
								System.out.println(k+"-"+getentity.getEntityId());
								
								input.setUgpg("APPNUM");	
								input=getregistrationnumber(input);
								input.setRegistrationNumber("T"+input.getRegistration_number().get(0));
						         transferstudent.setRegistrationNumber(input.getRegistrationNumber());

						         System.out.println("getRegistration:"+transferstudent.getRegistrationNumber());
						         
						     
						         getSqlMapClientTemplate().insert("bubbleform.setStudentRegistrationUserForm", transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.setApplicantProgramRegistrationUserForm",transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.insertIntoStudentFinalMarks",transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.setStudenttestNumberUserForm",transferstudent);
								 getSqlMapClientTemplate().insert("transfer.setAcademicScore",transferstudent);
								 getSqlMapClientTemplate().insert("transfer.setAdmitCardVanue",transferstudent);
								
								System.out.println("*****"+transferstudent.getActualApplication()+"-"+transferstudent.getFromprogram());
								System.out.println(transferstudent.getAppnumber()+"-"+transferstudent.getProgram_id());
								
								
								System.out.println("a1");
							
								 if (input.getPaperOptionFlag1().equalsIgnoreCase("D"))
								{
									
									pap2.add(programList.get(k).substring(0,7));
								
									for (int y=0 ; y<pap2.size();y++)
									{
										System.out.println("a2");
										transferstudent.setProgram_id(pap2.get(y));
										System.out.println("X0-"+transferstudent.getProgram_id());
										transferBean getTempAppNumber = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getTempAppNumber",transferstudent);
										System.out.println("X1"+getTempAppNumber.getTempApplicationNumber());
										transferstudent.setTempApplicationNumber(getTempAppNumber.getTempApplicationNumber());
										System.out.println("tempApp-"+getTempAppNumber.getTempApplicationNumber()+"--"+transferstudent.getProgram_id());
										
									 List<transferBean> checkPaper = new ArrayList<transferBean>();
									 checkPaper =getSqlMapClientTemplate().queryForList("transfer.checkPaper",transferstudent);
									 
									 if (checkPaper.size()>0)
									 {
										 getSqlMapClientTemplate().insert("transfer.setDefaultPaperCodes",transferstudent);
									 }
									 else 
									 {
										 getSqlMapClientTemplate().insert("transfer.setDefaultPaperCodes2",transferstudent);
									 }
										
									 
										System.out.println("a3");
									}
									pap2.clear();
									

									 System.out.println(transferstudent.getCandidateName()
									 +"-"+"transfered Program:--"+transferstudent.getProgramName()
									 +"-"+transferstudent.getEntityId());
								}
								
							}
							programList.clear();
							
							getSqlMapClientTemplate().insert("bubbleform.setStudentApplicationStatusUserFormTrasfer",transferstudent);
							getSqlMapClientTemplate().update("bubbleform.updateStudentApplicationStatus",transferstudent);

							transferstudent.setStatus("OK");
							return transferstudent;
							
							 
							}
							catch (Exception e)
							{
								System.out.println("erroeI--"+e);
								transferstudent.setStatus(e.toString());
							}
						}
						else
						{
							transferstudent.setStatus("ALL_Ready_Transfered");
						}
			}
			catch(Exception e)
			{

				transferstudent.setStatus(e.toString());
				
			}
			
	
	return transferstudent;
}
 
*/

/*
public transferBean transferApp(final transferBean input) {
	
	transferBean transferstudent= new transferBean();
	transferBean inp1= new transferBean();
	
	transferBean abc = (transferBean)transactionTemplate.execute(new TransactionCallback()
	{
		//Object savePoint ;
		
		public Object doInTransaction(TransactionStatus ts) {
			
			
			Object savePoint= new Object();
			try 
			{
				System.out.println("Don");
				savePoint = ts.createSavepoint();
			}
			catch (Exception e)
			{
				ts.rollbackToSavepoint(savePoint);
			}
			
			return input;
		}
		
	});
	
	
	return null;
}

*/




public transferBean transferApp(final transferBean input) {

	   
	transactionTemplate.execute(new TransactionCallback()
	{
		Object savePoint ;

		public Object doInTransaction(TransactionStatus ts) {
			
			transferBean transferstudent= new transferBean();
			transferBean inp1= new transferBean();
			
			List<transferBean> transferProgramList= new ArrayList<transferBean>();
			List <transferBean> paperoption1 =new ArrayList<transferBean>();
			List <String> COURSES =new ArrayList<String>();
		    List <String> Distinct =new ArrayList<String>();
		    List <String> pap1 =new ArrayList<String>();
		    List <String> pap2 =new ArrayList<String>();
		    List <String> pap3 =new ArrayList<String>();
		    List <String> pap4 =new ArrayList<String>();
		    List <String> pap5 =new ArrayList<String>();
		    List <String> programList =new ArrayList<String>();
		    String pasdob;
		    
		    
		    
		    transferstudent = (transferBean)getSqlMapClientTemplate().queryForObject("transfer.checkfortransferapp",input);
			transferstudent.setTransferFlag(input.getTransferFlag());
			transferstudent.setAppnumber(input.getTransferapp());
			transferstudent.setFromprogram(input.getFromprogram());
			transferstudent.setActualApplication(input.getActualApplication());
			input.setAppnumber(input.getTransferapp());
			
			if (transferstudent.getTransferFlag().equalsIgnoreCase("1"))
			{
				transferstudent.setToprogram(input.getToprogram());
				
				programList.add(transferstudent.getToprogram());

				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("2"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("3"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("4"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				transferstudent.setToprogram3(input.getToprogram4());
				
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				programList.add(transferstudent.getToprogram4());
				
			}
			else if (transferstudent.getTransferFlag().equalsIgnoreCase("5"))
			{
				transferstudent.setToprogram(input.getToprogram());
				transferstudent.setToprogram2(input.getToprogram2());
				transferstudent.setToprogram3(input.getToprogram3());
				transferstudent.setToprogram3(input.getToprogram4());
				transferstudent.setToprogram3(input.getToprogram5());
				
				
				programList.add(transferstudent.getToprogram());
				programList.add(transferstudent.getToprogram2());
				programList.add(transferstudent.getToprogram3());
				programList.add(transferstudent.getToprogram4());
				programList.add(transferstudent.getToprogram5());
				
			}
		    
			
			BubbleFormBean EntitySession = (BubbleFormBean) getSqlMapClientTemplate().queryForObject("bubbleform.EntitySession");      
		    transferstudent.setSession(EntitySession.getSession());
		    transferstudent.setStartdate(EntitySession.getStartdate());
		    transferstudent.setEnddate(EntitySession.getEnddate());
		    
		    pasdob=transferstudent.getDob().substring(0, 4);
		    String na1= transferstudent.getCandidateName().substring(0,3);
			 String asd =na1+pasdob;
			 transferstudent.setPassword(asd);
			 input.setPassword(asd);
			 System.out.println("Hello");
			
			 
			transferstudent.setFee("0");
			transferstudent.setUniversityId("0001");
			transferstudent.setCos(transferstudent.getCategory()+"XX");
			transferstudent.setCosval(transferstudent.getCategory());
			
			try
			{
				savePoint= new Object();
				savePoint = ts.createSavepoint();
				
				 List<transferBean> checkApplication = new ArrayList<transferBean>();
				 checkApplication =getSqlMapClientTemplate().queryForList("transfer.checkApplication",transferstudent);
			
						if (checkApplication.size()==0)
						{
							
							try 
							{
								//getSqlMapClientTemplate().startTransaction();  	
							
								input.setUgpg("STUDID");
							input .setUniversityId("0001");
							input.setStudentId(getStudentId(input));
						    transferstudent.setStudentId(input.getStudentId());
						    
						    System.out.println("getStudentId:"+transferstudent.getStudentId());
							
							System.out.println("getRegistration:"+transferstudent.getCandidateName());
						    
							getSqlMapClientTemplate().insert("bubbleform.setApplicantAccountInfo",transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setEntityStudentUserform", transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setStudentAddressUserFormtransfer", transferstudent);
							getSqlMapClientTemplate().insert("bubbleform.setStudentAddressUserFormtransferForPER", transferstudent);
							
							
							System.out.println(programList.size());
							
							for (int k=0 ; k<programList.size();k++)
							{
								String programId= programList.get(k).substring(0,7);
								transferstudent.setProgramName(programId);
								
								System.out.println(k+"-"+programId);
								
							
								
								transferBean getentity = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getentityid",transferstudent);
								
								transferstudent.setProgram_id(programId);
								transferstudent.setEntityId(getentity.getEntityId());
								
								System.out.println(k+"-"+getentity.getEntityId());
								
								input.setUgpg("APPNUM");	
								inp1=getregistrationnumber(input);
								input.setRegistrationNumber("T"+inp1.getRegistration_number().get(0));
						         transferstudent.setRegistrationNumber(input.getRegistrationNumber());

						         System.out.println("getRegistration:"+transferstudent.getRegistrationNumber());
						         
						     
						         getSqlMapClientTemplate().insert("bubbleform.setStudentRegistrationUserForm", transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.setApplicantProgramRegistrationUserForm",transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.insertIntoStudentFinalMarks",transferstudent);
						         getSqlMapClientTemplate().insert("bubbleform.setStudenttestNumberUserForm",transferstudent);
								 getSqlMapClientTemplate().insert("transfer.setAcademicScore",transferstudent);
								 getSqlMapClientTemplate().insert("transfer.setAdmitCardVanue",transferstudent);
								
								System.out.println("*****"+transferstudent.getActualApplication()+"-"+transferstudent.getFromprogram());
								System.out.println(transferstudent.getAppnumber()+"-"+transferstudent.getProgram_id());
								
								
								System.out.println("a1");
							
								 if (input.getPaperOptionFlag1().equalsIgnoreCase("D"))
								{
									
									pap2.add(programList.get(k).substring(0,7));
								
									for (int y=0 ; y<pap2.size();y++)
									{
										System.out.println("a2");
										transferstudent.setProgram_id(pap2.get(y));
										System.out.println("X0-"+transferstudent.getProgram_id());
										//transferBean getTempAppNumber = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getTempAppNumber",transferstudent);
										List<transferBean>getTempAppNumber= getSqlMapClientTemplate().queryForList("transfer.getTempAppNumber",transferstudent);
										
										if (getTempAppNumber.size()>0)
										{
											System.out.println("X1"+getTempAppNumber.get(0).getTempApplicationNumber());
											transferstudent.setTempApplicationNumber(getTempAppNumber.get(0).getTempApplicationNumber());
											System.out.println("tempApp-"+getTempAppNumber.get(0).getTempApplicationNumber()+"--"+getTempAppNumber.get(0).getProgram_id());
										}
										else
										{
											transferstudent.setTempApplicationNumber(" ");
										}
										
									 List<transferBean> checkPaper = new ArrayList<transferBean>();
									 checkPaper =getSqlMapClientTemplate().queryForList("transfer.checkPaper",transferstudent);
									 
									 if (checkPaper.size()>0)
									 {
										 getSqlMapClientTemplate().insert("transfer.setDefaultPaperCodes",transferstudent);
									 }
									 else 
									 {
										 getSqlMapClientTemplate().insert("transfer.setDefaultPaperCodes2",transferstudent);
									 }
										
									 
										System.out.println("a3");
									}
									pap2.clear();
									

									 System.out.println(transferstudent.getCandidateName()
									 +"-"+"transfered Program:--"+transferstudent.getProgramName()
									 +"-"+transferstudent.getEntityId());
								}
								
							}
							programList.clear();
							
							getSqlMapClientTemplate().insert("bubbleform.setStudentApplicationStatusUserFormTrasfer",transferstudent);
							getSqlMapClientTemplate().update("bubbleform.updateStudentApplicationStatus",transferstudent);

							
							//getSqlMapClientTemplate().commitTransaction();
							//getSqlMapClientTemplate().endTransaction(); 
							input.setStatus("OK");
							return input;
							
							 
							}
							catch (Exception e)
							{
								ts.rollbackToSavepoint(savePoint);
								System.out.println("erroeI--"+e);
								input.setStatus(e.toString());
								//getSqlMapClientTemplate().endTransaction();
							}
						}
						else
						{
							input.setStatus("ALL_Ready_Transfered");
						}
			}
			catch(Exception e)
			{
				ts.rollbackToSavepoint(savePoint);
				input.setStatus(e.toString());
				
			}
			
			
			
			return transferstudent;
		}
		
	});
	//return input;
	
	return input;
}





private transferBean getregistrationnumber(transferBean inputbean)
{
	
	transferBean regno = new transferBean();
	int seqNum=1;
	int response=0;
	try 
	{
		String yy = (new java.sql.Timestamp(new java.util.Date().getTime())).toString().substring(2, 4);


	//	System.out.println("app"+inputbean.getUgpg());
		inputbean.setUniversityId("0001");
	//System.out.println("app"+inputbean.getUniversityId());

		transferBean sysValueObj = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getSystemValues", inputbean);
            	
        seqNum=Integer.parseInt(sysValueObj.getSequenceNumber())+1;
        inputbean.setOldSequenceNumber(sysValueObj.getSequenceNumber());
        inputbean.setSequenceNumber(String.valueOf(seqNum));
       // System.out.println("dingo");
        response=getSqlMapClientTemplate().update("transfer.updateSystemValuesUserForm", inputbean);
      //  System.out.println("response-"+response);
        List<String> reg=new ArrayList<String>();
        reg.add(yy+padZero(seqNum, 7));
        inputbean.setRegistration_number(reg);
       // System.out.println(yy+padZero(seqNum, 7));
       
	}
	catch (Exception e)
	{
		System.out.println(e);
	}
	
	return inputbean;
	
}



private String getStudentId(transferBean inputbean)
{
	String studentId = "";
	int seqNum=0;
	int response=0;
	try 
	{
		transferBean sysValueObj = (transferBean) getSqlMapClientTemplate().queryForObject("transfer.getSystemValues",inputbean);

		 if (sysValueObj != null) 
		    {
			    inputbean.setOldSequenceNumber(sysValueObj.getSequenceNumber());
                seqNum = Integer.parseInt(sysValueObj.getSequenceNumber()) +1;
                inputbean.setSequenceNumber(String.valueOf(seqNum));
             
                response=getSqlMapClientTemplate().update("transfer.updateSystemValuesUserForm", inputbean);
            
            }
		
		 String year=new SimpleDateFormat("yyyy").format(new Date().getTime());
         studentId = "T" + "00010013"+year + padZero(seqNum, 5);
	}
	catch (Exception e)
	{
		System.out.println(e);
	}
	return studentId;
	
}





private String padZero(Integer number, int length) {
    String output = String.valueOf(number);

    while (output.length() < length) {
        output = "0" + output;
    }

    return output;
}

public admissionBean UpdateSCLMarks(final admissionBean input) {
	 final admissionBean bean1 = new admissionBean();
		
		transactionTemplate.execute
        (new TransactionCallback()
	     {
        	Object savePoint ;
        	 public Object doInTransaction(TransactionStatus ts) 
             {
        		
		try
		{
			
			savePoint= new Object();
			savePoint = ts.createSavepoint();
			
			List<admissionBean> checkforINS=getSqlMapClientTemplate().queryForList("computation.viewStudentMarksINS",input);
			List<admissionBean> checkforUPD=getSqlMapClientTemplate().queryForList("computation.viewStudentMarksUPD",input);
			List<admissionBean> checkforUPD_Other=getSqlMapClientTemplate().queryForList("computation.viewStudentMarksUPD_Other",input);
			
			cca_intBean input1= new cca_intBean();
			input1.setApplication_number(input.getApplication_number());
			input1.setRegistration_number(input.getRegistration_number());
			List<cca_intBean> checkBinding=getSqlMapClientTemplate().queryForList("ccaint.getBindedApplicationforCouncelling",input1);
		
		System.out.println("C"+checkforINS.size());
		System.out.println("U"+checkforUPD.size());
		System.out.println("U1"+checkforUPD_Other.size());
		System.out.println("comp:"+input.getComponent_id());
		if (checkforINS.size()>0)
		{
			getSqlMapClientTemplate().update("computation.update_SCL_marks",input);
			
			if (input.getComponent_id().equalsIgnoreCase("JM"))
			{
				getSqlMapClientTemplate().update("computation.update_SCL_marks_Btech",input);
			}
			
			for (int j=0;j<checkBinding.size();j++)
			{
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setComponent_id(input.getComponent_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setUser_id(input.getUser_id());
				input1.setTotalMarks(input.getTotalMarks());
				input1.setMarks_obt(input.getMarks_obt());
				
				getSqlMapClientTemplate().update("computation.update_SCL_marksAll",input1);
				
			}
		}
		else if (checkforUPD.size()>0)
		{
			getSqlMapClientTemplate().update("computation.update_SCL_marks2",input);
			
			if (input.getComponent_id().equalsIgnoreCase("JM"))
			{
				getSqlMapClientTemplate().update("computation.update_SCL_marks_Btech",input);
			}
			
			for (int j=0;j<checkBinding.size();j++)
			{
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setComponent_id(input.getComponent_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setUser_id(input.getUser_id());
				input1.setTotalMarks(input.getTotalMarks());
				input1.setMarks_obt(input.getMarks_obt());
				
				getSqlMapClientTemplate().update("computation.update_SCL_marksAll",input1);
				
			}
		}
		else if (checkforUPD_Other.size()>0)
		{
			getSqlMapClientTemplate().update("computation.update_SCL_marks2_other",input);
			
			if (input.getComponent_id().equalsIgnoreCase("JM"))
			{
				getSqlMapClientTemplate().update("computation.update_SCL_marks_Btech",input);
			}
			
			
			for (int j=0;j<checkBinding.size();j++)
			{
				input1.setRegistration_number(checkBinding.get(j).getRegistration_number());
				input1.setComponent_id(input.getComponent_id());
				input1.setProgram_id(checkBinding.get(j).getProgram_id());
				input1.setUser_id(input.getUser_id());
				input1.setTotalMarks(input.getTotalMarks());
				input1.setMarks_obt(input.getMarks_obt());
				
				getSqlMapClientTemplate().update("computation.update_SCL_marksAll_other",input1);
				
			}
		}
		    bean1.setFlag("OK");
		}
		catch(Exception e)
		{
			ts.rollbackToSavepoint(savePoint);
			bean1.setFlag("NOTOK");
		}
		
			
 		 return bean1;
             }
        	
	     });	
	return bean1;
}

public admissionBean UpdateFinalStatus(admissionBean input) {

	 admissionBean input1 = new admissionBean ();
	try
	{
			if (input.getFlag().equalsIgnoreCase("REJECT"))
			{
				getSqlMapClientTemplate().update("computation.update_REJECT_Status",input);
				getSqlMapClientTemplate().update("computation.update_REJECT_SCL",input);
				input1.setFlag("OK");
			}
			
			else if (input.getFlag().equalsIgnoreCase("VERIFIED"))
			{
				getSqlMapClientTemplate().update("computation.update_VERIFIED_Status",input);
				getSqlMapClientTemplate().update("computation.update_VERIFIED_SCL",input);
				
				input1.setFlag("OK");
			}
	}
	catch (Exception e)
	{
		input1.setFlag("NOTOK");
	}
	
	return input1;
}

@Override
public String distributETMarks()  {
	// TODO Auto-generated method stub
	
	// Read comp_marks
	// check if students has binding application 
	// get his all applications
	List<ReportInfoGetter> applicants=getSqlMapClientTemplate().queryForList("computation.getapplicantsmarks");
	if(applicants.size()==0)
		return "There is no record for processing";
	
	String bindedapplications ="";
	String[] bindary ;
	
	 List<String> bindedapps; 
	 String commonpgmObj; 
	 ReportInfoGetter input = new ReportInfoGetter();
	 
	
	
	for (ReportInfoGetter applicant:applicants) {
		bindedapps  = new ArrayList<String>();
		bindary=null;
		bindedapplications="";
		bindedapplications =applicant.getComp();
		//bindedapplications = bindedapplications +")";
		 try {
		bindary=bindedapplications.split(",");
		bindedapps = Arrays.asList(bindary);
		input.setWrklist(bindedapps);
		
		List<ReportInfoGetter> commonpgms=getSqlMapClientTemplate().queryForList("computation.getcommonprograms",input);
		
		//commonpgms.get(0).getWrklist().size();
		
			
		
		boolean marksdistributed =false;
		String flag="";
		for (ReportInfoGetter common:commonpgms) {
			commonpgmObj =common.getProgram_id();
			boolean status =commonpgmObj.contains(applicant.getProgram_id() );
			if(status) {
				flag="Y";
				distributemarks(commonpgmObj,applicant,bindedapps,flag);
				marksdistributed =true;
				System.out.println("Application number "+applicant.getTest_number());
				System.out.println("marks distributed"+commonpgmObj);
			}
		}
		if (!marksdistributed) {
			
			flag ="N";
			// distribute only if candidate has applied into the program 
			
			//  check if applied  
			
			//if applied 
			distributemarks(applicant.getProgram_id(), applicant,bindedapps,flag);
			
		}
			
	
		
		System.out.println("This is test message");
		
			}catch(Exception ex) {
			return ex.getMessage();
			}
		
		
	}
	
	
			
	
	
			
			// get all programs for these applications
			
			
			// check which program has a mapping for common entrance test
			
			
			// insert marks for common entrance test
			
	return "Entrance test marks successfully distributed";
}

public void distributemarks(String commonpgmObj,ReportInfoGetter applicant,List<String> bindedapps,String flag ) throws SQLException {
	
	 ReportInfoGetter input = new ReportInfoGetter();
	 List<ReportInfoGetter> result;
	 List<ReportInfoGetter> checklist;
	 
			 String[] commonpgmary ;
			 List<String> commonpgmlist ;
	
//	if (flag.equalsIgnoreCase("N")){
//		 getSqlMapClientTemplate().insert("computation.insertcompmarksdist",applicant);
//		 getSqlMapClientTemplate().update("computation.updatecompmarksstatus",applicant);
//		 return;
//	}
	
	//if (flag.equalsIgnoreCase("Y")){
		
		
		commonpgmary=commonpgmObj.split(",");
		commonpgmlist = Arrays.asList(commonpgmary);
		input.setWrklist(bindedapps);
		input.setWrklist1(commonpgmlist);
		result =getSqlMapClientTemplate().queryForList("computation.getallapplication",input);
		
		for (ReportInfoGetter appl:result) {
			input.setTest_number(appl.getTest_number());
			input.setProgram_id(appl.getProgram_id());
			input.setEntranceTestMarks(applicant.getEntranceTestMarks());
			input.setReason_code(applicant.getReason_code());
			
			checklist= getSqlMapClientTemplate().queryForList("computation.checkindist",input);
			if(checklist.size()>0) {
				System.out.println("Application alreadu exists in comp_marks_dist\n Application Number: "+input.getTest_number()+"Program_id"+input.getProgram_id()+"bindedApplication:"+bindedapps);
			}else {
				getSqlMapClientTemplate().insert("computation.insertcompmarksdist",input);	
			}
			 
			 
		}
		
		 getSqlMapClientTemplate().update("computation.updatecompmarksstatus",applicant);
		 return;
	//}
	
		
}
 
}
