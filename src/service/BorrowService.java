package service;

import exception.BookNotFoundException;
import exception.BusinessRuleViolationException;
import exception.InvalidInputException;
import model.Book;
import util.DataInputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Xử lý toàn bộ logic liên quan đến sách. Composition: BookService sở hữu
 * List<Book> (mũi tên ◆ trong UML).
 */
public class BookService {

    private List<Book> books;           // Danh sách sách (Composition)
    private DataInputValidator validator;

    public BookService() {
        this.books = new ArrayList<>();
        this.validator = new DataInputValidator();
    }

    // ===================== CRUD =====================
    /**
     * B1 — Thêm sách mới. BR1: bookId phải unique. BR2: title, author, genre
     * không được rỗng. BR9: Validate trước khi xử lý.
     */
    public void addBook(Book book) throws InvalidInputException, BusinessRuleViolationException {
        if (!validator.validateBookData(book)) {
            throw new InvalidInputException("Book data is incomplete or invalid.");
        }
        // BR1: Kiểm tra ID trùng
        if (isIdExist(book.getBookId())) {
            throw new BusinessRuleViolationException(
                    "Book ID '" + book.getBookId() + "' already exists.");
        }
        books.add(book);
    }

    /**
     * B2 — Cập nhật thông tin sách (không được đổi ID). Chỉ cập nhật các field
     * được truyền vào (khác null/rỗng).
     */
    public void updateBook(String id, Book b)
            throws BookNotFoundException, InvalidInputException {
        Book existing = findBook(id);

        if (b.getTitle() != null && !b.getTitle().isEmpty()) {
            existing.setTitle(b.getTitle());
        }
        if (b.getAuthor() != null && !b.getAuthor().isEmpty()) {
            existing.setAuthor(b.getAuthor());
        }
        if (b.getGenre() != null && !b.getGenre().isEmpty()) {
            existing.setGenre(b.getGenre());
        }
        if (b.getPublicationYear() > 0) {
            existing.setPublicationYear(b.getPublicationYear());
        }
        if (b.getQuantity() > 0) {
            existing.setQuantity(b.getQuantity());
        }
    }

    /**
     * B3 — Xóa sách. BR: Chỉ xóa được nếu không có ai đang mượn
     * (availableQuantity == quantity).
     */
    public void removeBook(String id)
            throws BookNotFoundException, BusinessRuleViolationException {
        Book book = findBook(id);
        if (book.getAvailableQuantity() < book.getQuantity()) {
            throw new BusinessRuleViolationException(
                    "Cannot remove book '" + book.getTitle() + "': it is currently borrowed.");
        }
        books.remove(book);
    }

    /**
     * B4 — Lấy toàn bộ danh sách sách.
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * B5 — Tìm kiếm sách theo title, author, hoặc genre (không phân biệt hoa
     * thường). * Tìm sách theo ID. UML: + findBook(id: String): Book throws
     * BookNotFoundException
     *
     * Null-safe: ném BookNotFoundException nếu id null/rỗng thay vì để
     * NullPointerException lan ra ngoài.
     *
     * Dùng nội bộ bởi các method CRUD và BorrowService.
     *
     * @throws BookNotFoundException nếu không tìm thấy hoặc id không hợp lệ
     */
    public Book findBook(String id) throws BookNotFoundException {

        if (id == null || id.isBlank()) {
            throw new BookNotFoundException("Book ID must not be empty.");
        }

        return books.stream()
                .filter(b -> b.getBookId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(
                "Book with ID '" + id + "' not found."));
    }

    /**
     * Tìm sách theo ID — dùng nội bộ và BorrowService.
     */
    public Book findBook(String id) throws BookNotFoundException {
        return books.stream()
                .filter(b -> b.getBookId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // ════════════════════════════════════════════════════════════════
    //  PACKAGE-LEVEL HELPERS  —  dùng nội bộ bởi BorrowService
    // ════════════════════════════════════════════════════════════════
    /**
     * Giảm stock khi mượn sách — BR8. Gọi bởi BorrowService.borrowBook(). BR4
     * được enforce bên trong Book.decreaseAvailable().
     *
     * @throws BookNotFoundException nếu không tìm thấy sách
     * @throws BusinessRuleViolationException nếu sách hết stock (BR4)
     */
    void decreaseStock(String bookId)
            throws BookNotFoundException, BusinessRuleViolationException {

        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BusinessRuleViolationException(
                    "Book '" + book.getTitle() + "' is out of stock.");
        }

        // BR8: giảm available; BR10: tăng borrowCount (bên trong Book)
        book.decreaseAvailable();
    }

    /**
     * Tăng stock khi trả sách — BR8. Gọi bởi BorrowService.returnBook().
     *
     * @throws BookNotFoundException nếu không tìm thấy sách
     */
    void increaseStock(String bookId) throws BookNotFoundException {
        Book book = findBook(bookId);
        book.increaseAvailable(); // BR8
    }

    // ════════════════════════════════════════════════════════════════
    //  FILE I/O SUPPORT  —  dùng bởi FileStorageService (Milestone 4)
    // ════════════════════════════════════════════════════════════════
    /**
     * Dùng cho FileStorageService.saveBooks(): trả về list gốc (không phải
     * copy) vì chỉ đọc để ghi file.
     */
    public List<Book> getBooksRaw() {
        return books;
    }

    /**
     * Dùng cho FileStorageService.loadBooks(): khôi phục toàn bộ list sau khi
     * đọc từ file .dat.
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // ════════════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ════════════════════════════════════════════════════════════════
    /**
     * BR1: kiểm tra ID đã tồn tại trong danh sách chưa.
     */
    private boolean isIdExist(String id) {
        return books.stream()
                .anyMatch(b -> b.getBookId().equalsIgnoreCase(id));
    }
}
