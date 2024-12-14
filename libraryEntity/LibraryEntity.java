import java.io.*;

// Base class for shared functionality (Encapsulation & Abstraction)
abstract class LibraryEntity {
    protected abstract void loadFromFile(String fileName) throws IOException;
    protected abstract void saveToFile(String fileName) throws IOException;
}
