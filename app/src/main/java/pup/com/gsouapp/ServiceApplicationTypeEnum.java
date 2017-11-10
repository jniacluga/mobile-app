package pup.com.gsouapp;

public enum ServiceApplicationTypeEnum {

    ADD_SUBJECT ("Add Subject"),
    CHANGE_SUBJECT ("Change Subject"),
    DROP_SUBJECT ("Drop Subject"),
    LEAVE_OF_ABSENCE ("Leave of Absence"),
    COMPLETION ("Completion"),
    COMPREHENSIVE_EXAM ("Comprehensive Exam"),
    GRADUATION ("Graduation"),
    PETITION_TUTORIAL_CLASS ("Petition/Tutorial Class"),
    ACADEMIC_RECORDS ("Academic Records");

    private String type;

    ServiceApplicationTypeEnum(String type) {
        this.type = type;
    }

    String getType() {
        return this.type;
    }
}
