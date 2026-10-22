package dao;

import database.DatabaseConnection;
import model.Member;

import java.sql.*;
import java.util.ArrayList;

public class MemberDAO {

    // Add a new member
    public boolean addMember(Member member) {

        String sql = "INSERT INTO members "
                + "(member_id, name, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, member.getMemberId());
            pst.setString(2, member.getName());
            pst.setString(3, member.getContactNumber());

            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding member.");
            e.printStackTrace();
            return false;
        }
    }

    // Get all members
    public ArrayList<Member> getAllMembers() {

        ArrayList<Member> members = new ArrayList<>();

        String sql = "SELECT * FROM members";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Member member = new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("contact_number")
                );

                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving members.");
            e.printStackTrace();
        }

        return members;
    }

    // Search member by ID
    public Member searchMember(String memberId) {

        String sql = "SELECT * FROM members WHERE member_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, memberId);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    return new Member(
                            rs.getString("member_id"),
                            rs.getString("name"),
                            rs.getString("contact_number")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for member.");
            e.printStackTrace();
        }

        return null;
    }
    
    public ArrayList<Member> searchMembers(String keyword) {

    ArrayList<Member> members = new ArrayList<>();

    String sql = "SELECT * FROM members "
            + "WHERE member_id LIKE ? "
            + "OR name LIKE ? "
            + "OR contact_number LIKE ? "
            + "ORDER BY member_id";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        String searchValue = "%" + keyword.trim() + "%";

        pst.setString(1, searchValue);
        pst.setString(2, searchValue);
        pst.setString(3, searchValue);

        try (ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Member member = new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("contact_number")
                );

                members.add(member);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error searching for members.");
        e.printStackTrace();
    }

    return members;
}

    // Update member
    public boolean updateMember(Member member) {

        String sql = "UPDATE members SET "
                + "name = ?, contact_number = ? "
                + "WHERE member_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, member.getName());
            pst.setString(2, member.getContactNumber());
            pst.setString(3, member.getMemberId());

            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating member.");
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTotalMemberCount() {
        String sql = "SELECT COUNT(*) FROM members";

        try (java.sql.Connection con =
                 database.DatabaseConnection.getConnection();
            java.sql.PreparedStatement pst =
                 con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    
    public boolean deleteMember(String memberId) {

        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, memberId);

            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting member.");
            e.printStackTrace();
            return false;
        }
    }

}