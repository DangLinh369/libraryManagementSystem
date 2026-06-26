package exception;

/**
 * Ném ra khi vi phạm business rule của hệ thống.
 * Ví dụ:
 *   - BR4: Sách hết hàng, không thể mượn
 *   - BR5: Thành viên đã mượn quá giới hạn
 *   - BR3: Xóa sách đang được mượn
 *   - BR3: Xóa member còn sách chưa trả
 */
public class BusinessRuleViolationException extends LibraryException {

    public BusinessRuleViolationException(String message) {
        super("Business rule violation: " + message);
    }
}