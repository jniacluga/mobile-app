package pup.com.gsouapp.Domain;

public class Sy {

    private Long id;
    private String abbr;
    private String start;
    private String end;

    public Sy(Long id, String abbr, String start, String end) {
        this.id = id;
        this.abbr = abbr;
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return this.abbr;
    }
}
