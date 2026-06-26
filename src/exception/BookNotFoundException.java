package exception;

/**
 * Ném ra khi tìm không thấy sách theo ID.
 * Ví dụ: BookService.findBook("B999") → sách không tồn tại
 */
public class BookNotFoundException extends LibraryException {

    public BookNotFoundException(String bookId) {
        super("Book not found with ID: " + bookId);
    }
}