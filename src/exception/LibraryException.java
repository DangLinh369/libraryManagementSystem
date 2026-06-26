package exception;

/**
 * Lớp cha cho tất cả exception trong hệ thống thư viện.
 * Kế thừa từ Exception (checked exception).
 */
public abstract class LibraryException extends Exception {

    // Constructor nhận message mô tả lỗi
    public LibraryException(String message) {
        super(message);
    }
}