// BorrowedBook class (Encapsulation & Inheritance)
class BorrowedBook {
    private int bookId;
    private String title;
    private int memberId;
    private String memberName;

    public BorrowedBook(int bookId, String title, int memberId, String memberName) {
        this.bookId = bookId;
        this.title = title;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String toFileFormat() {
        return bookId + ", " + title + "," + memberId + "," + memberName;
    }

    @Override
    public String toString() {
        return bookId + ": " + title + " borrowed by " + memberName;
    }
}
