package service;

import dao.MemberDAO;
import model.Member;
import java.util.ArrayList;

public class MemberService {

    private final MemberDAO memberDAO;

    public MemberService() {
        memberDAO = new MemberDAO();
    }

    // Add a new member
    public String addMember(Member member) {

        if (member == null) {
            return "Invalid member.";
        }

        if (member.getMemberId() == null
                || member.getMemberId().trim().isEmpty()) {
            return "Member ID cannot be empty.";
        }

        if (member.getName() == null
                || member.getName().trim().isEmpty()) {
            return "Member name cannot be empty.";
        }

        if (member.getContactNumber() == null
                || member.getContactNumber().trim().isEmpty()) {
            return "Contact number cannot be empty.";
        }

        // Check duplicate Member ID
        if (memberDAO.searchMember(member.getMemberId().trim()) != null) {
            return "Member ID already exists.";
        }

        member.setMemberId(member.getMemberId().trim());
        member.setName(member.getName().trim());
        member.setContactNumber(member.getContactNumber().trim());

        if (memberDAO.addMember(member)) {
            return "Member added successfully.";
        }

        return "Failed to add member.";
    }

    // Get all members
    public ArrayList<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    // Search member
    public Member searchMember(String memberId) {

        if (memberId == null || memberId.trim().isEmpty()) {
            return null;
        }

        return memberDAO.searchMember(memberId.trim());
    }

    public ArrayList<Member> searchMembers(String keyword) {

    if (keyword == null || keyword.trim().isEmpty()) {
        return getAllMembers();
    }

    return memberDAO.searchMembers(keyword.trim());
}
    
    // Update member
    public String updateMember(Member member) {

        if (member == null) {
            return "Invalid member.";
        }

        if (member.getMemberId() == null
                || member.getMemberId().trim().isEmpty()) {
            return "Member ID cannot be empty.";
        }

        if (member.getName() == null
                || member.getName().trim().isEmpty()) {
            return "Member name cannot be empty.";
        }

        if (member.getContactNumber() == null
                || member.getContactNumber().trim().isEmpty()) {
            return "Contact number cannot be empty.";
        }

        if (memberDAO.searchMember(member.getMemberId().trim()) == null) {
            return "Member not found.";
        }

        member.setMemberId(member.getMemberId().trim());
        member.setName(member.getName().trim());
        member.setContactNumber(member.getContactNumber().trim());

        if (memberDAO.updateMember(member)) {
            return "Member updated successfully.";
        }

        return "Failed to update member.";
    }
    
    public int getTotalMemberCount() {
    return memberDAO.getTotalMemberCount();
}
    
    
    public String deleteMember(String memberId) {

        if (memberId == null || memberId.trim().isEmpty()) {
            return "Please select a member to delete.";
        }

        String id = memberId.trim();

        if (memberDAO.searchMember(id) == null) {
            return "Member not found.";
        }

        if (memberDAO.deleteMember(id)) {
            return "Member deleted successfully.";
        }

        return "Failed to delete member.";
    }

}