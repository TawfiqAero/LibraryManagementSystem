import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class LibrarySystem extends LibraryEntity {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private Scanner scanner = new Scanner(System.in);

    @Override
    protected void loadFromFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");  // Split by comma

                // Handle book data
                if (fileName.equals("book.txt")) {
                    if (parts.length == 4) {  // Check if there are exactly 4 parts (ID, Title, Author, Availability)
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            String title = parts[1].trim();
                            String author = parts[2].trim();
                            boolean isAvailable = Integer.parseInt(parts[3].trim()) == 1;
                            books.add(new Book(id, title, author, isAvailable));
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid book data: " + line);
                        }
                    }
                }
                // Handle member data
                else if (fileName.equals("members.txt")) {
                    if (parts.length == 2) {  // Check if there are exactly 2 parts (ID, Name)
                        try {
                            int memberId = Integer.parseInt(parts[0].trim());
                            String memberName = parts[1].trim();
                            members.add(new Member(memberId, memberName));
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid member data: " + line);
                        }
                    }
                }
                // Handle borrowed book data
                else if (fileName.equals("borrowed_book.txt")) {
                    if (parts.length == 4) {  // Check if there are exactly 4 parts (Book ID, Book Title, Member ID, Borrower Name)
                        try {
                            int bookId = Integer.parseInt(parts[0].trim());
                            String bookTitle = parts[1].trim();
                            int memberId = Integer.parseInt(parts[2].trim());
                            String borrowerName = parts[3].trim();
                            borrowedBooks.add(new BorrowedBook(bookId, bookTitle, memberId, borrowerName));
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid borrowed book data: " + line);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void saveToFile(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            if (fileName.equals("book.txt")) {
                for (Book book : books) {
                    bw.write(book.toFileFormat());
                    bw.newLine();
                }
            } else if (fileName.equals("borrowed_book.txt")) {
                for (BorrowedBook borrowedBook : borrowedBooks) {
                    bw.write(borrowedBook.toFileFormat());
                    bw.newLine();
                }
            }
        }
    }

    // Login method
    public boolean login() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    // Method to display the menu after login
    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Library System Menu ---");
            System.out.println("1. View Books");
            System.out.println("2. Search Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Renew Book");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewBooks();
                    break;
                case 2:
                    System.out.print("Enter search keyword: ");
                    String keyword = scanner.nextLine();
                    searchBook(keyword);
                    break;
                case 3:
                    System.out.print("Enter book ID to borrow: ");
                    int bookIdToBorrow = scanner.nextInt();
                    System.out.print("Enter member ID: ");
                    int memberIdToBorrow = scanner.nextInt();
                    borrowBook(bookIdToBorrow, memberIdToBorrow);
                    break;
                case 4:
                    System.out.print("Enter book ID to return: ");
                    int bookIdToReturn = scanner.nextInt();
                    returnBook(bookIdToReturn);
                    break;
                case 5:
                    System.out.print("Enter book ID to renew: ");
                    int bookIdToRenew = scanner.nextInt();
                    renewBook(bookIdToRenew);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void viewBooks() {
        books.forEach(System.out::println);
    }

    public void searchBook(String keyword) {
        books.stream().filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(System.out::println);
    }

    public void borrowBook(int bookId, int memberId) {
        Optional<Book> book = books.stream().filter(b -> b.getBookId() == bookId && b.isAvailable()).findFirst();
        Optional<Member> member = members.stream().filter(m -> m.getMemberId() == memberId).findFirst();

        if (book.isPresent() && member.isPresent()) {
            Book b = book.get();
            b.setAvailable(false);
            borrowedBooks.add(new BorrowedBook(bookId, b.getTitle(), memberId, member.get().getName()));
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Book not available or invalid member ID.");
        }
    }

    public void returnBook(int bookId) {
        Optional<BorrowedBook> borrowedBook = borrowedBooks.stream().filter(bb -> bb.getBookId() == bookId).findFirst();

        if (borrowedBook.isPresent()) {
            borrowedBooks.remove(borrowedBook.get());
            books.stream().filter(b -> b.getBookId() == bookId).findFirst().ifPresent(b -> b.setAvailable(true));
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Book not found in borrowed list.");
        }
    }

    public void renewBook(int bookId) {
        Optional<BorrowedBook> borrowedBook = borrowedBooks.stream().filter(borrowedBookEntity -> borrowedBookEntity.getBookId() == bookId).findFirst();

        if (borrowedBook.isPresent()) {
            BorrowedBook borrowed = borrowedBook.get();
            // Here you can add logic to extend the due date or mark as renewed.
            System.out.println("Book renewed successfully.");
        } else {
            System.out.println("Book not found in borrowed list.");
        }
    }

    public static void main(String[] args) throws IOException {
        LibrarySystem library = new LibrarySystem();

        // Check login first
        if (library.login()) {
            // Load data from files
            library.loadFromFile("book.txt");
            library.loadFromFile("members.txt");
            library.loadFromFile("borrowed_book.txt");

            // Show the menu after successful login
            library.showMenu();

            // Save the changes back to file
            library.saveToFile("book.txt");
            library.saveToFile("borrowed_book.txt");
        }
    }
}