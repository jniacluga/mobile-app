package pup.com.gsouapp.Domain;

public class Schedule {

    private String sy;
    private String sem;
    private String day;
    private String startTime;
    private String endTime;
    private String subjectCode;
    private String description;
    private String faculty;
    private String room;
    private String sectionCode;

    private Long scheduleId;
    private Long subjectId;
    private int units;

    public Schedule(String sy, String sem, String day, String startTime, String endTime, String subjectCode, String description, String faculty, String room, String sectionCode) {
        this.sy = sy;
        this.sem = sem;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectCode = subjectCode;
        this.description = description;
        this.faculty = faculty;
        this.room = room;
        this.sectionCode = sectionCode;
    }

    public Schedule(String sy, String sem, String day, String startTime, String endTime, String subjectCode, String description, String faculty, Long scheduleId, Long subjectId, int units) {
        this.sy = sy;
        this.sem = sem;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectCode = subjectCode;
        this.description = description;
        this.faculty = faculty;
        this.scheduleId = scheduleId;
        this.subjectId = subjectId;
        this.units = units;
    }

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
