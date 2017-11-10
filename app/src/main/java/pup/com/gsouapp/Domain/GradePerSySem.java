package pup.com.gsouapp.Domain;

import java.util.List;

public class GradePerSySem {

    private String sySem;
    private String sy;
    private String sem;

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    private List<Grade> gradeList;

    public String getSySem() {
        return sySem;
    }

    public void setSySem(String sySem) {
        this.sySem = sySem;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

}
