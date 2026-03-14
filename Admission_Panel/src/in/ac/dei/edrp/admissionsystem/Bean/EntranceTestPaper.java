package in.ac.dei.edrp.admissionsystem.Bean;

import java.io.Serializable;

public class EntranceTestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private String paperCode;
    private String paperName;
    private String group;

    // ---- Getters and Setters ----
    public String getPaperCode() {
        return paperCode;
    }
    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPaperName() {
        return paperName;
    }
    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
}
