package service;

import dao.BookDAO;
import dao.BorrowRecordDAO;
import dao.MemberDAO;
import model.Book;
import model.BorrowRecord;
import model.Member;

import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowService {

    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowRecordDAO borrowRecordDAO;

    public BorrowService() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        borrowRecordDAO = new BorrowRecordDAO();
    }

    // Borrow a book
    public String borrowBook(String bookId, String memberId) {

        // Validate Book ID
        if (bookId == null || bookId.trim().isEmpty()) {
            return "Book ID cannot be empty.";
        }

        // Validate Member ID
        if (memberId == null || memberId.trim().isEmpty()) {
            return "Member ID cannot be empty.";
        }

        bookId = bookId.trim();
        memberId = memberId.trim();

        // Check whether the book exists
        Book book = bookDAO.searchBook(bookId);

        if (book == null) {
            return "Book not found.";
        }

        // Check whether the member exists
        Member member = memberDAO.searchMember(memberId);

        if (member == null) {
            return "Member not found.";
        }

        // Check book availability
        if (!"Available".equalsIgnoreCase(book.getStatus())) {
            return "This book is already borrowed.";
        }

        BorrowRecord record = new BorrowRecord(
                0,
                book,
                member,
                LocalDate.now(),
                null,
                "Borrowed"
        );

        // Save borrowing record
        if (borrowRecordDAO.createBorrowRecord(record)) {
            return "Book borrowed successfully.";
        }

        return "Failed to borrow book.";
    }

    // Return a book
    public String returnBook(String bookId) {

        if (bookId == null || bookId.trim().isEmpty()) {
            return "Book ID cannot be empty.";
        }

        bookId = bookId.trim();

        Book book = bookDAO.searchBook(bookId);

        if (book == null) {
            return "Book not found.";
        }

        if (!"Borrowed".equalsIgnoreCase(book.getStatus())) {
            return "This book is not currently borrowed.";
        }

        if (borrowRecordDAO.returnBook(bookId)) {
            return "Book returned successfully.";
        }

        return "Failed to return book.";
    }

    public ArrayList<BorrowRecord> getActiveBorrowRecords() {
        return borrowRecordDAO.getActiveBorrowRecords();
    }

    public ArrayList<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDAO.getAllBorrowRecords();
    }

    public ArrayList<Book> getAvailableBooks() {
        return bookDAO.getAvailableBooks();
    }

    public ArrayList<Book> getBorrowedBooks() {
        return bookDAO.getBorrowedBooks();
    }
}