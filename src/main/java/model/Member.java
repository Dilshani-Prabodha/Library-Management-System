package model;

public class Member {

    private String memberId;
    private String name;
    private String contactNumber;

    public Member() {
    }

    public Member(String memberId, String name, String contactNumber) {

        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return memberId + " - " + name;
    }
}