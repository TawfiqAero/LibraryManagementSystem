public class Book {
    private int bookId;
    private String bookTitle;
    private String author;

    public Book(int bookId, String bookTitle, String author) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.author = author;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthor() {
        return author;
    }
}
