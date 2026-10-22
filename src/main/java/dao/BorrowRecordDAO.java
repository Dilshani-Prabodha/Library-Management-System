package dao;

import database.DatabaseConnection;
import model.Book;
import model.BorrowRecord;
import model.Member;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowRecordDAO {

    // Create a new borrowing record
    public boolean createBorrowRecord(BorrowRecord record) {

        String insertRecord = "INSERT INTO borrow_records "
                + "(book_id, member_id, borrow_date, status) "
                + "VALUES (?, ?, ?, ?)";

        String updateBook = "UPDATE books SET status = 'Borrowed' "
                + "WHERE book_id = ? AND status = 'Available'";

        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement bookStatement =
                         con.prepareStatement(updateBook)) {

                bookStatement.setString(1, record.getBook().getBookId());

                int rowsUpdated = bookStatement.executeUpdate();

                if (rowsUpdated == 0) {
                    con.rollback();
                    return false;
                }
            }

            // Create borrowing record
            try (PreparedStatement recordStatement =
                         con.prepareStatement(insertRecord)) {

                recordStatement.setString(
                        1,
                        record.getBook().getBookId()
                );

                recordStatement.setString(
                        2,
                        record.getMember().getMemberId()
                );

                recordStatement.setDate(
                        3,
                        Date.valueOf(record.getBorrowDate())
                );

                recordStatement.setString(
                        4,
                        record.getStatus()
                );

                recordStatement.executeUpdate();
            }

            con.commit();

            return true;

        } catch (SQLException e) {

            System.out.println("Error creating borrow record.");
            e.printStackTrace();

            // Cancel changes if an error occurs
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            return false;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Return a borrowed book
    public boolean returnBook(String bookId) {

        String updateBook = "UPDATE books SET status = 'Available' "
                + "WHERE book_id = ? AND status = 'Borrowed'";

        String updateRecord = "UPDATE borrow_records "
                + "SET return_date = ?, status = 'Returned' "
                + "WHERE book_id = ? "
                + "AND status = 'Borrowed'";

        Connection con = null;

        try {

            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // Update book status
            try (PreparedStatement bookStatement =
                         con.prepareStatement(updateBook)) {

                bookStatement.setString(1, bookId);

                int rowsUpdated = bookStatement.executeUpdate();

                if (rowsUpdated == 0) {
                    con.rollback();
                    return false;
                }
            }

            try (PreparedStatement recordStatement =
                         con.prepareStatement(updateRecord)) {

                recordStatement.setDate(
                        1,
                        Date.valueOf(LocalDate.now())
                );

                recordStatement.setString(2, bookId);

                recordStatement.executeUpdate();
            }

            con.commit();

            return true;

        } catch (SQLException e) {

            System.out.println("Error returning book.");
            e.printStackTrace();

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            return false;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Get all active borrowing records
    public ArrayList<BorrowRecord> getActiveBorrowRecords() {

        ArrayList<BorrowRecord> records = new ArrayList<>();

        String sql = """
            SELECT br.record_id,
                   br.borrow_date,
                   br.return_date,
                   br.status,

                   b.book_id,
                   b.title,
                   b.author,
                   b.category,
                   b.status AS book_status,

                   m.member_id,
                   m.name,
                   m.contact_number

            FROM borrow_records br

            INNER JOIN books b
                ON br.book_id = b.book_id

            INNER JOIN members m
                ON br.member_id = m.member_id

            WHERE br.status = 'Borrowed'
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("book_status")
                );

                Member member = new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("contact_number")
                );

                BorrowRecord record = new BorrowRecord(
                        rs.getInt("record_id"),
                        book,
                        member,
                        rs.getDate("borrow_date").toLocalDate(),
                        null,
                        rs.getString("status")
                );

                records.add(record);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving active borrow records."
            );
            e.printStackTrace();
        }

        return records;
    }

    public ArrayList<BorrowRecord> getAllBorrowRecords() {

        ArrayList<BorrowRecord> records = new ArrayList<>();

        String sql = """
            SELECT br.record_id,
                   br.borrow_date,
                   br.return_date,
                   br.status,

                   b.book_id,
                   b.title,
                   b.author,
                   b.category,
                   b.status AS book_status,

                   m.member_id,
                   m.name,
                   m.contact_number

            FROM borrow_records br

            INNER JOIN books b
                ON br.book_id = b.book_id

            INNER JOIN members m
                ON br.member_id = m.member_id

            ORDER BY br.record_id DESC
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("book_status")
                );

                Member member = new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("contact_number")
                );

                Date returnDate = rs.getDate("return_date");

                BorrowRecord record = new BorrowRecord(
                        rs.getInt("record_id"),
                        book,
                        member,
                        rs.getDate("borrow_date").toLocalDate(),
                        returnDate != null
                                ? returnDate.toLocalDate()
                                : null,
                        rs.getString("status")
                );

                records.add(record);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving borrowing history."
            );
            e.printStackTrace();
        }

        return records;
    }
}