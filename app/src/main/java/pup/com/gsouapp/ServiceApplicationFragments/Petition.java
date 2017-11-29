package pup.com.gsouapp.ServiceApplicationFragments;

import java.util.Map;

class Petition {
    private Long petitionId;
    private String initiator;
    private int numberOfStudentsInvited;
    private int numberOfStudentsApproved;
    private Map<String, String> studentsInPetition;
    private String dateRequested;

    public Petition(Long petitionId, String initiator, int numberOfStudentsInvited, int numberOfStudentsApproved, Map<String, String> studentsInPetition, String dateRequested) {
        this.petitionId = petitionId;
        this.initiator = initiator;
        this.numberOfStudentsInvited = numberOfStudentsInvited;
        this.numberOfStudentsApproved = numberOfStudentsApproved;
        this.studentsInPetition = studentsInPetition;
        this.dateRequested = dateRequested;
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public int getNumberOfStudentsInvited() {
        return numberOfStudentsInvited;
    }

    public void setNumberOfStudentsInvited(int numberOfStudentsInvited) {
        this.numberOfStudentsInvited = numberOfStudentsInvited;
    }

    public int getNumberOfStudentsApproved() {
        return numberOfStudentsApproved;
    }

    public void setNumberOfStudentsApproved(int numberOfStudentsApproved) {
        this.numberOfStudentsApproved = numberOfStudentsApproved;
    }

    public Map<String, String> getStudentsInPetition() {
        return studentsInPetition;
    }

    public void setStudentsInPetition(Map<String, String> studentsInPetition) {
        this.studentsInPetition = studentsInPetition;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }
}
