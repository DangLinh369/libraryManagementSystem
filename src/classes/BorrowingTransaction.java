package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Lưu thông tin một lần mượn sách.
 *
 * Liên quan UML:
 *   - Composition với Book và Member
 *   - LOAN_DAYS = 14 (hằng số static final)
 *   - BorrowService sở hữu List<BorrowingTransaction>
 */
public class BorrowingTransaction implements Serializable {

    // Số ngày mượn tối đa
    public static final int LOAN_DAYS = 14;

    // ===================== FIELDS =====================
    private String      transactionId;
    private Book        book;
    private Member      member;
    private LocalDate   borrowDate;
    private LocalDate   dueDate;       // = borrowDate + LOAN_DAYS
    private LocalDate   returnDate;    // null nếu chưa trả

    // ===================== CONSTRUCTOR =====================
    public BorrowingTransaction(String transactionId, Book book, Member member,
                                LocalDate borrowDate) {
        this.transactionId = transactionId;
        this.book          = book;
        this.member        = member;
        this.borrowDate    = borrowDate;
        this.dueDate       = borrowDate.plusDays(LOAN_DAYS);
        this.returnDate    = null; // Chưa trả
    }

    // ===================== BUSINESS METHODS =====================

    /**
     * Tính tiền phạt khi trả sách.
     * BR7: Dựa trên số ngày quá hạn × fine rate của member.
     * Dùng Polymorphism: gọi member.calculateFine() → tự động đúng loại member.
     */
    public double calculateFine() {
        if (returnDate == null) return 0;
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysOverdue <= 0) return 0;
        return member.calculateFine((int) daysOverdue);
    }

    /**
     * Kiểm tra giao dịch có quá hạn không.
     * So sánh với ngày hôm nay nếu chưa trả, hoặc ngày trả nếu đã trả.
     */
    public boolean isOverdue() {
        LocalDate checkDate = (returnDate != null) ? returnDate : LocalDate.now();
        return checkDate.isAfter(dueDate);
    }

    /**
     * Số ngày quá hạn (dùng cho báo cáo overdue).
     * Trả về 0 nếu chưa quá hạn.
     */
    public long getDaysOverdue() {
        LocalDate checkDate = (returnDate != null) ? returnDate : LocalDate.now();
        long days = ChronoUnit.DAYS.between(dueDate, checkDate);
        return Math.max(0, days);
    }

    /** Trả về trạng thái giao dịch dưới dạng String. */
    public String getStatus() {
        if (returnDate != null) return "Returned";
        if (isOverdue())        return "Overdue";
        return "Borrowing";
    }

    // ===================== GETTERS =====================
    public String    getTransactionId() { return transactionId; }
    public Book      getBook()          { return book; }
    public Member    getMember()        { return member; }
    public LocalDate getBorrowDate()    { return borrowDate; }
    public LocalDate getDueDate()       { return dueDate; }
    public LocalDate getReturnDate()    { return returnDate; }

    // ===================== SETTERS =====================
    /** Gọi khi thành viên trả sách. */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // ===================== TOSTRING =====================
    @Override
    public String toString() {
        return String.format("%-6s %-25s %-20s %-12s %-12s %-10s %-10s",
                transactionId,
                book.getTitle(),
                member.getName(),
                borrowDate.toString(),
                dueDate.toString(),
                returnDate != null ? returnDate.toString() : "---",
                getStatus());
    }
}