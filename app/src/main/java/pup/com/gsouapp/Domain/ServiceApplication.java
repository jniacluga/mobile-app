package pup.com.gsouapp.Domain;

public class ServiceApplication {

    private Long id;
    private String applicationNo;
    private String status;
    private String dateRequested;
    private String type;
    private String summary;

    public ServiceApplication() {}

    public ServiceApplication(Long id, String applicationNo, String status, String dateRequested, String type, String summary) {
        this.id = id;
        this.applicationNo = applicationNo;
        this.status = status;
        this.dateRequested = dateRequested;
        this.type = type;
        this.summary = summary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
