package pup.com.gsouapp.Domain;

public class Grade {

    private String subjectCode;
    private String description;
    private String faculty;
    private String units;
    private String sectionCode;
    private String finalGrade;
    private String status;

    public Grade(String subjectCode, String description, String faculty, String units, String sectionCode, String finalGrade, String status) {
        this.subjectCode = subjectCode;
        this.description = description;
        this.faculty = faculty;
        this.units = units;
        this.sectionCode = sectionCode;
        this.finalGrade = finalGrade;
        this.status = status;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
