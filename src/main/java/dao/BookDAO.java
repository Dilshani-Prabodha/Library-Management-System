package dao;

import database.DatabaseConnection;
import model.Book;

import java.sql.*;
import java.util.ArrayList;

public class BookDAO {

    // Add a new book
    public boolean addBook(Book book) {

        String sql = "INSERT INTO books "
                + "(book_id, title, author, category, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, book.getBookId());
            pst.setString(2, book.getTitle());
            pst.setString(3, book.getAuthor());
            pst.setString(4, book.getCategory());
            pst.setString(5, book.getStatus());

            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding book.");
            e.printStackTrace();
            return false;
        }
    }

    // Get all books
    public ArrayList<Book> getAllBooks() {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("status")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books.");
            e.printStackTrace();
        }

        return books;
    }

    public Book searchBook(String bookId) {

        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, bookId);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    return new Book(
                            rs.getString("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for book.");
            e.printStackTrace();
        }

        return null;
    }
    
    
    // Search books by ID, title, or author
    public ArrayList<Book> searchBooks(String keyword) {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books "
                   + "WHERE book_id LIKE ? "
                   + "OR title LIKE ? "
                   + "OR author LIKE ? "
                   + "ORDER BY book_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String searchValue = "%" + keyword.trim() + "%";

            pst.setString(1, searchValue);
            pst.setString(2, searchValue);
            pst.setString(3, searchValue);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("status")
                    );

                    books.add(book);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching books.");
            e.printStackTrace();
        }

        return books;
    }

    
    // Check whether a book has borrowing records
    public boolean hasBorrowingRecords(String bookId) {

        String sql = "SELECT COUNT(*) FROM borrow_records WHERE book_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, bookId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking borrowing records.");
            e.printStackTrace();

            // Do not allow deletion if the check fails
            return true;
        }

        return true;
    }


    // Delete book
    public boolean deleteBook(String bookId) {

        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, bookId);

            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting book.");
            e.printStackTrace();
            return false;
        }
    }

    // Update book
    public boolean updateBook(Book book) {

        String sql = "UPDATE books SET "
                + "title = ?, author = ?, category = ?, status = ? "
                + "WHERE book_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getCategory());
            pst.setString(4, book.getStatus());
            pst.setString(5, book.getBookId());

            int rowsAffected = pst.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating book.");
            e.printStackTrace();
            return false;
        }
    }

    // Get only available books
    public ArrayList<Book> getAvailableBooks() {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books WHERE status = 'Available'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("status")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving available books.");
            e.printStackTrace();
        }

        return books;
    }

    // Get only borrowed books
    public ArrayList<Book> getBorrowedBooks() {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books WHERE status = 'Borrowed'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("status")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving borrowed books.");
            e.printStackTrace();
        }

        return books;
    }
    
    public int getTotalBookCount() {
        String sql = "SELECT COUNT(*) FROM books";

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
    
    
    public int getAvailableBookCount() {
        String sql = "SELECT COUNT(*) FROM books WHERE status = 'Available'";

        try (java.sql.Connection con =
                 database.DatabaseConnection.getConnection();
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getBorrowedBookCount() {
        String sql = "SELECT COUNT(*) FROM books WHERE status = 'Borrowed'";

        try (java.sql.Connection con =
                 database.DatabaseConnection.getConnection();
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}