package service;

import model.Book;
import model.BorrowingTransaction;
import model.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc/ghi dữ liệu xuống file .dat (binary serialization).
 *
 * Liên quan UML:
 *   - FILE_BOOKS    = "books.dat"
 *   - FILE_MEMBERS  = "members.dat"
 *   - FILE_TRANS    = "transactions.dat"
 *
 * Milestone 4: File I/O & data persistence.
 */
public class FileStorageService {

    private static final String FILE_BOOKS   = "books.dat";
    private static final String FILE_MEMBERS = "members.dat";
    private static final String FILE_TRANS   = "transactions.dat";

    // ===================== SAVE =====================

    public void saveBooks(List<Book> books) {
        saveToFile(FILE_BOOKS, books);
    }

    public void saveMembers(List<Member> members) {
        saveToFile(FILE_MEMBERS, members);
    }

    public void saveTransactions(List<BorrowingTransaction> txns) {
        saveToFile(FILE_TRANS, txns);
    }

    // ===================== LOAD =====================

    @SuppressWarnings("unchecked")
    public List<Book> loadBooks() {
        List<Book> result = loadFromFile(FILE_BOOKS);
        return result != null ? result : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Member> loadMembers() {
        List<Member> result = loadFromFile(FILE_MEMBERS);
        return result != null ? result : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<BorrowingTransaction> loadTransactions() {
        List<BorrowingTransaction> result = loadFromFile(FILE_TRANS);
        return result != null ? result : new ArrayList<>();
    }

    // ===================== HELPERS =====================

    private <T> void saveToFile(String filename, List<T> data) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("[ERROR] Cannot save to file '" + filename + "': " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return null; // File chưa tồn tại → trả về null để dùng list rỗng

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Cannot load from file '" + filename + "': " + e.getMessage());
            return null;
        }
    }
}
