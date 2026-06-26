package model;

import java.io.Serializable;

/**
 * Lớp đại diện cho một cuốn sách trong thư viện.
 * Implements Serializable để có thể lưu xuống file .dat (FileStorageService).
 *
 * Liên quan UML:
 *   - BookService có Composition với Book (BookService sở hữu List<Book>)
 *   - BorrowingTransaction có Composition với Book
 */
public class Book implements Serializable {

    // ===================== FIELDS =====================
    private String bookId;           // BR1: Unique, không được thay đổi
    private String title;            // BR2: Không được rỗng
    private String author;           // BR2: Không được rỗng
    private String genre;            // BR2: Không được rỗng
    private int publicationYear;
    private int quantity;            // Tổng số lượng sách
    private int availableQuantity;   // Số lượng hiện có thể mượn
    private int borrowCount;         // BR10: Đếm tổng số lần được mượn (dùng cho báo cáo popular books)

    // ===================== CONSTRUCTOR =====================
    public Book(String bookId, String title, String author, String genre,
                int publicationYear, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.availableQuantity = quantity; // Ban đầu tất cả đều available
        this.borrowCount = 0;
    }

    // ===================== BUSINESS METHODS =====================

    /**
     * Kiểm tra sách còn có thể mượn không.
     * BR4: A book must be available (stock > 0) before it can be borrowed.
     */
    public boolean isAvailable() {
        return availableQuantity > 0;
    }

    /**
     * Giảm số lượng available khi có người mượn.
     * BR8: Book stock is reduced upon borrowing.
     */
    public void decreaseAvailable() {
        if (availableQuantity > 0) {
            availableQuantity--;
            borrowCount++; // Tăng tổng số lần mượn
        }
    }

    /**
     * Tăng số lượng available khi có người trả.
     * BR8: Book stock is increased upon returning.
     */
    public void increaseAvailable() {
        if (availableQuantity < quantity) {
            availableQuantity++;
        }
    }

    // ===================== GETTERS =====================
    public String getBookId()          { return bookId; }
    public String getTitle()           { return title; }
    public String getAuthor()          { return author; }
    public String getGenre()           { return genre; }
    public int getPublicationYear()    { return publicationYear; }
    public int getQuantity()           { return quantity; }
    public int getAvailableQuantity()  { return availableQuantity; }
    public int getBorrowCount()        { return borrowCount; }

    // ===================== SETTERS =====================
    // bookId KHÔNG có setter → BR1: ID không được thay đổi

    public void setTitle(String title)                   { this.title = title; }
    public void setAuthor(String author)                 { this.author = author; }
    public void setGenre(String genre)                   { this.genre = genre; }
    public void setPublicationYear(int publicationYear)  { this.publicationYear = publicationYear; }
    public void setQuantity(int quantity) {
        // Khi cập nhật tổng quantity, điều chỉnh available tương ứng
        int borrowed = this.quantity - this.availableQuantity;
        this.quantity = quantity;
        this.availableQuantity = Math.max(0, quantity - borrowed);
    }

    // ===================== TOSTRING =====================
    @Override
    public String toString() {
        return String.format("%-6s %-25s %-20s %-12s %-6d %-5d/%-5d",
                bookId, title, author, genre, publicationYear,
                availableQuantity, quantity);
    }
}