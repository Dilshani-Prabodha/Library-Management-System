package service;

import dao.BookDAO;
import model.Book;

import java.util.ArrayList;

public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        bookDAO = new BookDAO();
    }

    // Add a new book
    public String addBook(Book book) {

        if (book == null) {
            return "Invalid book.";
        }

        if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            return "Book ID cannot be empty.";
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return "Book title cannot be empty.";
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return "Author cannot be empty.";
        }

        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            return "Category cannot be empty.";
        }

        // Check duplicate Book ID
        if (bookDAO.searchBook(book.getBookId().trim()) != null) {
            return "Book ID already exists.";
        }

        book.setBookId(book.getBookId().trim());
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor().trim());
        book.setCategory(book.getCategory().trim());

        book.setStatus("Available");

        if (bookDAO.addBook(book)) {
            return "Book added successfully.";
        }

        return "Failed to add book.";
    }

    // Get all books
    public ArrayList<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    // Search book
    public Book searchBook(String bookId) {

        if (bookId == null || bookId.trim().isEmpty()) {
            return null;
        }

        return bookDAO.searchBook(bookId.trim());
    }
    
    
    // Search books by ID, title, or author
    public ArrayList<Book> searchBooks(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }

        return bookDAO.searchBooks(keyword.trim());
    }


    // Update book
    public String updateBook(Book book) {

        if (book == null) {
            return "Invalid book.";
        }

        if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            return "Book ID cannot be empty.";
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return "Book title cannot be empty.";
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return "Author cannot be empty.";
        }

        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            return "Category cannot be empty.";
        }

        if (bookDAO.searchBook(book.getBookId().trim()) == null) {
            return "Book not found.";
        }

        if (bookDAO.updateBook(book)) {
            return "Book updated successfully.";
        }

        return "Failed to update book.";
    }
    
    // Delete book
    public String deleteBook(String bookId) {

        if (bookId == null || bookId.trim().isEmpty()) {
            return "Book ID cannot be empty.";
        }

        bookId = bookId.trim();

        Book book = bookDAO.searchBook(bookId);

        if (book == null) {
            return "Book not found.";
        }

        // Do not delete a borrowed book
        if ("Borrowed".equalsIgnoreCase(book.getStatus())) {
            return "Cannot delete a borrowed book.";
        }

        if (bookDAO.hasBorrowingRecords(bookId)) {
            return "Cannot delete this book because borrowing records exist.";
        }

        if (bookDAO.deleteBook(bookId)) {
            return "Book deleted successfully.";
        }

        return "Failed to delete book.";
    }


    // Get available books
    public ArrayList<Book> getAvailableBooks() {
        return bookDAO.getAvailableBooks();
    }

    // Get borrowed books
    public ArrayList<Book> getBorrowedBooks() {
        return bookDAO.getBorrowedBooks();
    }
    
    public int getTotalBookCount() {
        return bookDAO.getTotalBookCount();
    }
    
    
    public int getAvailableBookCount() {
        return bookDAO.getAvailableBookCount();
    }

    public int getBorrowedBookCount() {
        return bookDAO.getBorrowedBookCount();
    }
}