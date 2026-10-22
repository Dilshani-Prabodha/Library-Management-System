package model;

import java.time.LocalDate;

public class BorrowRecord {

    private int recordId;
    private Book book;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;

    public BorrowRecord() {
    }

    public BorrowRecord(int recordId, Book book, Member member,
                        LocalDate borrowDate, LocalDate returnDate,
                        String status) {

        this.recordId = recordId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}