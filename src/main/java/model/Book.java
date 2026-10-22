package model;

public class Book {

    private String bookId;
    private String title;
    private String author;
    private String category;
    private String status;

    public Book() {
    }

    public Book(String bookId, String title, String author,
                String category, String status) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return bookId + " - " + title;
    }
}