package pup.com.gsouapp.Domain;

public class Subject {

    private Long subjectId;
    private String subjectDisplay;
    private String code;
    private String description;
    private int units;
    private String sy;
    private String semester;

    public Subject(Long subjectId, String subjectDisplay, String code, String description, int units, String sy, String semester) {
        this.subjectId = subjectId;
        this.subjectDisplay = subjectDisplay;
        this.code = code;
        this.description = description;
        this.units = units;
        this.sy = sy;
        this.semester = semester;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectDisplay() {
        return subjectDisplay;
    }

    public void setSubjectDisplay(String subjectDisplay) {
        this.subjectDisplay = subjectDisplay;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.description;
    }
}
