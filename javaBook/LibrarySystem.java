import java.io.*;
import java.util.*;

public class LibrarySystem {

    private static Map<Integer, BorrowedBook> borrowedBooks = new HashMap<>(); // Store borrowed books

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Library System ---");
            System.out.println("1. View Books");
            System.out.println("2. Search Book");
            System.out.println("3. Borrow Book");
            System.out.println("4. Renew Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    viewBooks();
                    break;
                case 2:
                    System.out.print("Enter the title or author to search for: ");
                    String searchQuery = scanner.nextLine();
                    searchBooks(searchQuery);
                    break;
                case 3:
                    System.out.print("Enter your Member ID: ");
                    int memberId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter the Book ID to borrow: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    borrowBook(memberId, bookId);
                    break;
                case 4:
                    System.out.print("Enter the Book ID to renew: ");
                    int renewBookId = scanner.nextInt();
                    scanner.nextLine();
                    renewBook(renewBookId);
                    break;
                case 5:
                    System.out.print("Enter the Book ID to return: ");
                    int returnBookId = scanner.nextInt();
                    scanner.nextLine();
                    returnBook(returnBookId);
                    break;
                case 6:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close(); // Close scanner to release resources
    }

    public static void viewBooks() {
        File booksFile = new File("book.txt");
        if (!booksFile.exists()) {
            System.out.println("Error: books.txt file not found.");
            return;
        }

        try (BufferedReader booksReader = new BufferedReader(new FileReader(booksFile))) {
            System.out.printf("\n%-10s%-30s%-30s%-15s\n", "Book ID", "Title", "Author", "Availability");
            System.out.println("---------------------------------------------------------------------------------");

            String line;
            while ((line = booksReader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    int bookId = Integer.parseInt(bookData[0]);
                    String title = bookData[1];
                    String author = bookData[2];
                    boolean isAvailable = !BorrowedBook.isBookBorrowed(bookId);

                    System.out.printf("%-10d%-30s%-30s%-15s\n",
                            bookId, title, author, isAvailable ? "Available" : "Borrowed");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
        }
    }

    public static void searchBooks(String searchQuery) {
        File booksFile = new File("book.txt");
        if (!booksFile.exists()) {
            System.out.println("Error: books.txt file not found.");
            return;
        }

        boolean found = false;
        try (BufferedReader booksReader = new BufferedReader(new FileReader(booksFile))) {
            System.out.printf("\n%-10s%-30s%-30s%-15s\n", "Book ID", "Title", "Author", "Availability");
            System.out.println("---------------------------------------------------------------------------------");

            String line;
            while ((line = booksReader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (line.toLowerCase().contains(searchQuery.toLowerCase())) {
                    int bookId = Integer.parseInt(bookData[0]);
                    String title = bookData[1];
                    String author = bookData[2];
                    boolean isAvailable = !BorrowedBook.isBookBorrowed(bookId);

                    System.out.printf("%-10d%-30s%-30s%-15s\n",
                            bookId, title, author, isAvailable ? "Available" : "Borrowed");
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
        }

        if (!found) {
            System.out.println("No books matched your search query.");
        }
    }

    public static void borrowBook(int memberId, int bookId) {
        // Assuming Member class exists and provides this method
        Member member = Member.getMemberById(memberId);
        if (member == null) {
            System.out.println("Invalid Member ID.");
            return;
        }

        if (BorrowedBook.isBookBorrowed(bookId)) {
            System.out.println("Book is already borrowed.");
            return;
        }

        File booksFile = new File("book.txt");
        if (!booksFile.exists()) {
            System.out.println("Error: books.txt file not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(booksFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (Integer.parseInt(bookData[0]) == bookId) {
                    String bookTitle = bookData[1];
                    BorrowedBook.addBorrowedBook(new BorrowedBook(bookId, bookTitle, memberId, member.getName()));
                    System.out.println("Book borrowed successfully!");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
        }
    }

    public static void renewBook(int bookId) {
        if (BorrowedBook.isBookBorrowed(bookId)) {
            System.out.println("Book renewed successfully!");
        } else {
            System.out.println("Book not found in borrowed list.");
        }
    }

    public static void returnBook(int bookId) {
        if (!BorrowedBook.isBookBorrowed(bookId)) {
            System.out.println("Book not found in borrowed list.");
            return;
        }

        BorrowedBook.removeBorrowedBook(bookId);
        System.out.println("Book returned successfully!");
    }
}
