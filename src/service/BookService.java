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
 * Xử lý toàn bộ logic liên quan đến sách.
 * Composition: BookService sở hữu List<Book> (mũi tên ◆ trong UML).
 */
public class BookService {

    private List<Book> books;           // Danh sách sách (Composition)
    private DataInputValidator validator;

    public BookService() {
        this.books     = new ArrayList<>();
        this.validator = new DataInputValidator();
    }

    // ===================== CRUD =====================

    /**
     * B1 — Thêm sách mới.
     * BR1: bookId phải unique.
     * BR2: title, author, genre không được rỗng.
     * BR9: Validate trước khi xử lý.
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
     * B2 — Cập nhật thông tin sách (không được đổi ID).
     * Chỉ cập nhật các field được truyền vào (khác null/rỗng).
     */
    public void updateBook(String id, Book b)
            throws BookNotFoundException, InvalidInputException {
        Book existing = findBook(id);

        if (b.getTitle()  != null && !b.getTitle().isEmpty())
            existing.setTitle(b.getTitle());
        if (b.getAuthor() != null && !b.getAuthor().isEmpty())
            existing.setAuthor(b.getAuthor());
        if (b.getGenre()  != null && !b.getGenre().isEmpty())
            existing.setGenre(b.getGenre());
        if (b.getPublicationYear() > 0)
            existing.setPublicationYear(b.getPublicationYear());
        if (b.getQuantity() > 0)
            existing.setQuantity(b.getQuantity());
    }

    /**
     * B3 — Xóa sách.
     * BR: Chỉ xóa được nếu không có ai đang mượn
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
     * B5 — Tìm kiếm sách theo title, author, hoặc genre (không phân biệt hoa thường).
     */
    public List<Book> searchBooks(String kw) {
        String keyword = kw.toLowerCase().trim();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(keyword)
                          || b.getAuthor().toLowerCase().contains(keyword)
                          || b.getGenre().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
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

    // ===================== HELPERS =====================

    public void setBooks(List<Book> books) { this.books = books; }

    private boolean isIdExist(String id) {
        return books.stream()
                .anyMatch(b -> b.getBookId().equalsIgnoreCase(id));
    }
}
