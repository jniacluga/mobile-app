package pup.com.gsouapp.Domain;

public class Semester {

    private Long id;
    private String semester;

    public Semester(Long id, String semester) {
        this.id = id;
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return this.semester;
    }
}
