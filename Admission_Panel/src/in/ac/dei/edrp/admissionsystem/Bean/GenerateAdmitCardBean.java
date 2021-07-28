package in.ac.dei.edrp.admissionsystem.Bean;

import java.util.ArrayList;
import java.util.List;

public class GenerateAdmitCardBean {

	    private String university_id;
	    private String component_id;
	    private String description;
	    private String admitcard_month; //added by atul dayal
	    private String Interview_Flag;//added by atul dayal
	    private String paper_code;
	    private String program_id;
	    private String entity_id;
	    private String registration_number ;
	    private String branch_code;
	    
	    private String entity_name;
	    private String program_name;
	    private String branch_name;
	    private String specialization_id;
	    private String specialization_name;
	    private String doc_path ;
	    
	    private String name;
	    private String fathername ;
	    private String testnumber ;
	    private String venue;
	    private String testdate;
	    private String testtime;
	    
	    private String interviewdate;
	    private String interviewtime;
	    private String address;
	    private String city;
	    private String state;
	    private String pincode;
	    private String admitCardPath;
	    private  String userid;
	    private   String instructions;
	    private  String application_number ;
	    
	    
	    private  String start_date;
	    private  String end_date;
	    
	  
	    private  String hindiInstructons;
	    private  String applicationType;
	    
	    
	    private  String emailId; //added by manpreet
	    private  String emailSentStatus;
	    private  String errorReason;
	    
	    private  String programCode;

	    private List<admissionBean> entranceTests;
	    private List<admissionBean> programs = new ArrayList<admissionBean>();
	    
		public String getUniversity_id() {
			return university_id;
		}

		public void setUniversity_id(String universityId) {
			university_id = universityId;
		}

		public String getComponent_id() {
			return component_id;
		}

		public void setComponent_id(String componentId) {
			component_id = componentId;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getAdmitcard_month() {
			return admitcard_month;
		}

		public void setAdmitcard_month(String admitcardMonth) {
			admitcard_month = admitcardMonth;
		}

		public String getInterview_Flag() {
			return Interview_Flag;
		}

		public void setInterview_Flag(String interviewFlag) {
			Interview_Flag = interviewFlag;
		}

		public String getPaper_code() {
			return paper_code;
		}

		public void setPaper_code(String paperCode) {
			paper_code = paperCode;
		}

		public String getProgram_id() {
			return program_id;
		}

		public void setProgram_id(String programId) {
			program_id = programId;
		}

		public String getEntity_id() {
			return entity_id;
		}

		public void setEntity_id(String entityId) {
			entity_id = entityId;
		}

		public String getRegistration_number() {
			return registration_number;
		}

		public void setRegistration_number(String registrationNumber) {
			registration_number = registrationNumber;
		}

		public String getBranch_code() {
			return branch_code;
		}

		public void setBranch_code(String branchCode) {
			branch_code = branchCode;
		}

		public String getEntity_name() {
			return entity_name;
		}

		public void setEntity_name(String entityName) {
			entity_name = entityName;
		}

		public String getProgram_name() {
			return program_name;
		}

		public void setProgram_name(String programName) {
			program_name = programName;
		}

		public String getBranch_name() {
			return branch_name;
		}

		public void setBranch_name(String branchName) {
			branch_name = branchName;
		}

		public String getSpecialization_id() {
			return specialization_id;
		}

		public void setSpecialization_id(String specializationId) {
			specialization_id = specializationId;
		}

		public String getSpecialization_name() {
			return specialization_name;
		}

		public void setSpecialization_name(String specializationName) {
			specialization_name = specializationName;
		}

		public String getDoc_path() {
			return doc_path;
		}

		public void setDoc_path(String docPath) {
			doc_path = docPath;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFathername() {
			return fathername;
		}

		public void setFathername(String fathername) {
			this.fathername = fathername;
		}

		public String getTestnumber() {
			return testnumber;
		}

		public void setTestnumber(String testnumber) {
			this.testnumber = testnumber;
		}

		public String getVenue() {
			return venue;
		}

		public void setVenue(String venue) {
			this.venue = venue;
		}

		public String getTestdate() {
			return testdate;
		}

		public void setTestdate(String testdate) {
			this.testdate = testdate;
		}

		public String getTesttime() {
			return testtime;
		}

		public void setTesttime(String testtime) {
			this.testtime = testtime;
		}

		public String getInterviewdate() {
			return interviewdate;
		}

		public void setInterviewdate(String interviewdate) {
			this.interviewdate = interviewdate;
		}

		public String getInterviewtime() {
			return interviewtime;
		}

		public void setInterviewtime(String interviewtime) {
			this.interviewtime = interviewtime;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPincode() {
			return pincode;
		}

		public void setPincode(String pincode) {
			this.pincode = pincode;
		}

		public String getAdmitCardPath() {
			return admitCardPath;
		}

		public void setAdmitCardPath(String admitCardPath) {
			this.admitCardPath = admitCardPath;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getInstructions() {
			return instructions;
		}

		public void setInstructions(String instructions) {
			this.instructions = instructions;
		}

		public String getApplication_number() {
			return application_number;
		}

		public void setApplication_number(String applicationNumber) {
			application_number = applicationNumber;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String startDate) {
			start_date = startDate;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String endDate) {
			end_date = endDate;
		}

		public String getHindiInstructons() {
			return hindiInstructons;
		}

		public void setHindiInstructons(String hindiInstructons) {
			this.hindiInstructons = hindiInstructons;
		}

		public String getApplicationType() {
			return applicationType;
		}

		public void setApplicationType(String applicationType) {
			this.applicationType = applicationType;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getEmailSentStatus() {
			return emailSentStatus;
		}

		public void setEmailSentStatus(String emailSentStatus) {
			this.emailSentStatus = emailSentStatus;
		}

		public String getErrorReason() {
			return errorReason;
		}

		public void setErrorReason(String errorReason) {
			this.errorReason = errorReason;
		}

		public String getProgramCode() {
			return programCode;
		}

		public void setProgramCode(String programCode) {
			this.programCode = programCode;
		}

		public List<admissionBean> getEntranceTests() {
			return entranceTests;
		}

		public void setEntranceTests(List<admissionBean> entranceTests) {
			this.entranceTests = entranceTests;
		}

		public List<admissionBean> getPrograms() {
			return programs;
		}

		public void setPrograms(List<admissionBean> programs) {
			this.programs = programs;
		}

		
}
