package in.ac.dei.edrp.admissionsystem.Bean;

import java.io.Serializable;

public class QrAttendanceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /* ================= STUDENT / QR ================= */

    private String application_number;
    private String program_id;
    private String program_name;        // ✅ ADDED
    private String student_name;
    private String dob;
    private String photo_path;
    private String signature_path;

    /* ================= ATTENDANCE ================= */

    private String status;
    private String reason;

    /* ================= LOGIN ================= */

    private String userName;
    private String password;

    /* ================= GETTERS & SETTERS ================= */

    public String getApplication_number() {
        return application_number;
    }
    public void setApplication_number(String application_number) {
        this.application_number = application_number;
    }

    public String getProgram_id() {
        return program_id;
    }
    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getProgram_name() {
        return program_name;
    }
    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getStudent_name() {
        return student_name;
    }
    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoto_path() {
        return photo_path;
    }
    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getSignature_path() {
        return signature_path;
    }
    public void setSignature_path(String signature_path) {
        this.signature_path = signature_path;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
