package in.ac.dei.edrp.admissionsystem.Bean;

import java.io.Serializable;
import java.util.Date; 

public class GenerateAdmitCardBeanNew implements Serializable {

    private static final long serialVersionUID = 1L;

    private String programId;
    private String programName;
    private String programCode;
    private String tencodes;
    private String entranceTestExist;

    private String applicationNumber;
    private String registrationNumber;
    private String rollNumber;

    private String name;
    private String fatherName;
    private String dob;
    private String address;
    private String venue;

    private String testdate;
    private String testtime;
    private String interviewdate;
    private String interviewtime;

    private String admitCardPath;
    private String docPath;
    private String instructions;
    private String sessionYear;
    private Date startDate;
    private Date endDate;
    private String subjects;
    
    private String retMonthYear;
    
    public String getRetMonthYear() { return retMonthYear; }
    public void setRetMonthYear(String retMonthYear) { this.retMonthYear = retMonthYear; }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }


    // Getter for startDate
    public Date getStartDate() {
        return startDate;
    }

    // Setter for startDate
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter for endDate
    public Date getEndDate() {
        return endDate;
    }

    // Setter for endDate
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getSessionYear() {
       return sessionYear;
    }

   public void setSessionYear(String sessionYear) {
        this.sessionYear = sessionYear;
    }

    // ---- Getters and Setters ----
    public String getProgramId() {
        return programId;
    }
    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramCode() {
        return programCode;
    }
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getTencodes() {
        return tencodes;
    }
    public void setTencodes(String tencodes) {
        this.tencodes = tencodes;
    }

    public String getEntranceTestExist() {
        return entranceTestExist;
    }
    public void setEntranceTestExist(String entranceTestExist) {
        this.entranceTestExist = entranceTestExist;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }
    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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

    public String getAdmitCardPath() {
        return admitCardPath;
    }
    public void setAdmitCardPath(String admitCardPath) {
        this.admitCardPath = admitCardPath;
    }
    
    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }
    
    

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
