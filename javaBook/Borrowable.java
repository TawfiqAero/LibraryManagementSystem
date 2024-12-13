public interface Borrowable {
    boolean isBookBorrowed(int bookId);

    void addBorrowedBook(BorrowedBook book);

    void removeBorrowedBook(int bookId);
}