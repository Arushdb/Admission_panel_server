
package in.ac.dei.edrp.admissionsystem.Bean;

import java.util.List;

public class BubbleFormBean 
{
	private String ugpg;
	private String sessionStartDate;
	private String sessionEndDate;
	private String cos;
	private String cosval;
	private String verification;
	private String DocPath;
	private String paperDis;
	private String startdate;
	private String enddate;
	private String grouping;
	private String papsum;
	private String mainGroup;
	private String papercode;
	private String UserId;
	private String appnumber;
	private String formnumber;
	private String transferapp;
	private String fee;
	private String session;
	private String country;
	private String registrationNumber;
	private List<String> registration_number;
	private List<String>application_number;
	private List<String>student_id;
	private String studentId;
	private String entityId;
	private String oldSequenceNumber;
	private String SequenceNumber;
	private String universityId;
	private String programType;
	private String paperOption;
	private String paperGroup;
	private String programName;
	private String candidateName;
	private String fatherName;
	private String MotherName;
	private String phoneNumber;
	private String category;
	private String addressline1;
	private String addressline2;
	private String state;
	private String city;
	private String staff_status;
	private String staff_code;
	private String hostel;
	private String cca;
	private String phyhandicaped;
	private String email_id;
	private String gender;
	private String religion;
	private String minority;
	private String meritalstatus;
	private String dob;
	private String fatherincome;
	private String motherincome;
	private String gardianincome;
	private String examCenter;
	private String nationality;
	private String pincode;
	private int row;
	private int col;
	private String group_code;
	private String status;
	private String Description;
	private String Program_id;
	private String title;
	private String detail[];
	private String detail1[];
	private String password;
	private List<String> courseCodeList;
	
	private List<String> pap1;
	private List<String> pap2;
	
	
	private List<BubbleFormBean> courseCode;
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	public BubbleFormBean()
	{
		
	}
	
	public BubbleFormBean(List<String> pap1,List<String> pap2)
	{
		this.pap1 = pap1;
		this.pap2 = pap2;
	}
	public BubbleFormBean(String detail[],String detail1[])
	{
		this.detail = detail;
	}
	
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getGroup_code() {
		return group_code;
	}
	public void setGroup_code(String groupCode) {
		group_code = groupCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getProgram_id() {
		return Program_id;
	}
	public void setProgram_id(String programId) {
		Program_id = programId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUgpg() {
		return ugpg;
	}
	public void setUgpg(String ugpg) {
		this.ugpg = ugpg;
	}
	public String getSessionStartDate() {
		return sessionStartDate;
	}
	public void setSessionStartDate(String sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	public String getSessionEndDate() {
		return sessionEndDate;
	}
	public void setSessionEndDate(String sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	public String getCos() {
		return cos;
	}
	public void setCos(String cos) {
		this.cos = cos;
	}
	public String getPaperDis() {
		return paperDis;
	}
	public void setPaperDis(String paperDis) {
		this.paperDis = paperDis;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getGrouping() {
		return grouping;
	}
	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}
	public String getMainGroup() {
		return mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this.mainGroup = mainGroup;
	}
	public String getPapercode() {
		return papercode;
	}
	public void setPapercode(String papercode) {
		this.papercode = papercode;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getAppnumber() {
		return appnumber;
	}
	public void setAppnumber(String appnumber) {
		this.appnumber = appnumber;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public List<String> getRegistration_number() {
		return registration_number;
	}
	public void setRegistration_number(List<String> registrationNumber) {
		registration_number = registrationNumber;
	}
	public List<String> getApplication_number() {
		return application_number;
	}
	public void setApplication_number(List<String> applicationNumber) {
		application_number = applicationNumber;
	}
	public List<String> getStudent_id() {
		return student_id;
	}
	public void setStudent_id(List<String> studentId) {
		student_id = studentId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getOldSequenceNumber() {
		return oldSequenceNumber;
	}
	public void setOldSequenceNumber(String oldSequenceNumber) {
		this.oldSequenceNumber = oldSequenceNumber;
	}
	public String getSequenceNumber() {
		return SequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		SequenceNumber = sequenceNumber;
	}
	public String getUniversityId() {
		return universityId;
	}
	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getPaperOption() {
		return paperOption;
	}
	public void setPaperOption(String paperOption) {
		this.paperOption = paperOption;
	}
	public String getPaperGroup() {
		return paperGroup;
	}
	public void setPaperGroup(String paperGroup) {
		this.paperGroup = paperGroup;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return MotherName;
	}
	public void setMotherName(String motherName) {
		MotherName = motherName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAddressline1() {
		return addressline1;
	}
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}
	public String getAddressline2() {
		return addressline2;
	}
	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStaff_status() {
		return staff_status;
	}
	public void setStaff_status(String staffStatus) {
		staff_status = staffStatus;
	}
	public String getStaff_code() {
		return staff_code;
	}
	public void setStaff_code(String staffCode) {
		staff_code = staffCode;
	}
	public String getHostel() {
		return hostel;
	}
	public void setHostel(String hostel) {
		this.hostel = hostel;
	}
	public String getCca() {
		return cca;
	}
	public void setCca(String cca) {
		this.cca = cca;
	}
	public String getPhyhandicaped() {
		return phyhandicaped;
	}
	public void setPhyhandicaped(String phyhandicaped) {
		this.phyhandicaped = phyhandicaped;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String emailId) {
		email_id = emailId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getMinority() {
		return minority;
	}
	public void setMinority(String minority) {
		this.minority = minority;
	}
	public String getMeritalstatus() {
		return meritalstatus;
	}
	public void setMeritalstatus(String meritalstatus) {
		this.meritalstatus = meritalstatus;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getFatherincome() {
		return fatherincome;
	}
	public void setFatherincome(String fatherincome) {
		this.fatherincome = fatherincome;
	}
	public String getMotherincome() {
		return motherincome;
	}
	public void setMotherincome(String motherincome) {
		this.motherincome = motherincome;
	}
	public String getGardianincome() {
		return gardianincome;
	}
	public void setGardianincome(String gardianincome) {
		this.gardianincome = gardianincome;
	}
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getFormnumber() {
		return formnumber;
	}
	public void setFormnumber(String formnumber) {
		this.formnumber = formnumber;
	}
	public List<String> getCourseCodeList() {
		return courseCodeList;
	}
	public void setCourseCodeList(List<String> courseCodeList) {
		this.courseCodeList = courseCodeList;
	}
	public List<BubbleFormBean> getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(List<BubbleFormBean> courseCode) {
		this.courseCode = courseCode;
	}
	public String[] getDetail() {
		return detail;
	}
	public void setDetail(String[] detail) {
		this.detail = detail;
	}
	public String[] getDetail1() {
		return detail1;
	}
	public void setDetail1(String[] detail1) {
		this.detail1 = detail1;
	}
	public List<String> getPap1() {
		return pap1;
	}
	public void setPap1(List<String> pap1) {
		this.pap1 = pap1;
	}
	public List<String> getPap2() {
		return pap2;
	}
	public void setPap2(List<String> pap2) {
		this.pap2 = pap2;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPapsum() {
		return papsum;
	}
	public void setPapsum(String papsum) {
		this.papsum = papsum;
	}
	public String getDocPath() {
		return DocPath;
	}
	public void setDocPath(String docPath) {
		DocPath = docPath;
	}
	public String getCosval() {
		return cosval;
	}
	public void setCosval(String cosval) {
		this.cosval = cosval;
	}
	public String getTransferapp() {
		return transferapp;
	}
	public void setTransferapp(String transferapp) {
		this.transferapp = transferapp;
	}
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
}
