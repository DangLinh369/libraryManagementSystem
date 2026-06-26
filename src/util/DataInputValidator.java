package util;

import model.Book;
import model.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validate tất cả dữ liệu đầu vào trước khi xử lý.
 * BR9: All inputs must be validated before processing.
 *
 * Liên quan UML:
 *   - BookService, MemberService, BorrowService đều dùng class này (Dependency)
 */
public class DataInputValidator {

    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===================== VALIDATE BOOK =====================

    /**
     * Validate toàn bộ thông tin sách.
     * BR2: title, author, genre không được rỗng.
     */
    public boolean validateBookData(Book b) {
        if (b == null)                              return false;
        if (isNullOrEmpty(b.getBookId()))           return false;
        if (isNullOrEmpty(b.getTitle()))            return false;
        if (isNullOrEmpty(b.getAuthor()))           return false;
        if (isNullOrEmpty(b.getGenre()))            return false;
        if (b.getPublicationYear() < 1000
         || b.getPublicationYear() > LocalDate.now().getYear()) return false;
        if (b.getQuantity() <= 0)                   return false;
        return true;
    }

    // ===================== VALIDATE MEMBER =====================

    /**
     * Validate toàn bộ thông tin thành viên.
     */
    public boolean validateMemberData(Member m) {
        if (m == null)                    return false;
        if (isNullOrEmpty(m.getMemberId())) return false;
        if (isNullOrEmpty(m.getName()))   return false;
        if (isNullOrEmpty(m.getPhone()))  return false;
        if (isNullOrEmpty(m.getEmail()))  return false;
        return true;
    }

    // ===================== VALIDATE DATE =====================

    /**
     * Validate chuỗi ngày theo định dạng DD/MM/YYYY.
     * BR6: Borrow date must be current or in the past.
     */
    public boolean validateDate(String dateStr) {
        if (isNullOrEmpty(dateStr)) return false;
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // ===================== VALIDATE ID =====================

    /**
     * Validate ID không rỗng và đúng định dạng.
     */
    public boolean validate(String id) {
        return !isNullOrEmpty(id);
    }

    // ===================== HELPER =====================

    /** Parse chuỗi ngày thành LocalDate. */
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
    }

    /** Format LocalDate thành chuỗi DD/MM/YYYY. */
    public static String formatDate(LocalDate date) {
        if (date == null) return "---";
        return date.format(DATE_FORMATTER);
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}