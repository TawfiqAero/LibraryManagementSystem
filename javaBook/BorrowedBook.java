import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BorrowedBook extends Book implements Borrowable {
    private int memberId;
    private String memberName;

    public BorrowedBook(int bookId, String bookTitle, String author, int memberId, String memberName) {
        super(bookId, bookTitle, author);
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    // Check if a book is borrowed
    @Override
    public static boolean isBookBorrowed(int bookId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == bookId) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed_books.txt: " + e.getMessage());
        }
        return false;
    }

    // Add a borrowed book
    @Override
    public static void addBorrowedBook(BorrowedBook book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed_books.txt", true))) {
            writer.write(book.getBookId() + "," + book.getBookTitle() + "," +
                    book.getAuthor() + "," + book.getMemberId() + "," + book.getMemberName());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to borrowed_books.txt: " + e.getMessage());
        }
    }

    // Remove a borrowed book
    @Override
    public static void removeBorrowedBook(int bookId) {
        File borrowedFile = new File("borrowed_books.txt");
        File tempFile = new File("borrowed_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(borrowedFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) != bookId) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating borrowed_books.txt: " + e.getMessage());
        }

        if (borrowedFile.delete() && tempFile.renameTo(borrowedFile)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Error updating borrowed books file.");
        }
    }
}