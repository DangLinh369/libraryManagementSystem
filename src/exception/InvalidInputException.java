package exception;

/**
 * Ném ra khi dữ liệu đầu vào không hợp lệ.
 * Ví dụ: ID rỗng, ngày sai định dạng, tên trống...
 * Liên quan đến BR9: All inputs must be validated before processing.
 */
public class InvalidInputException extends LibraryException {

    public InvalidInputException(String message) {
        super("Invalid input: " + message);
    }
}