package model;

/**
 * Thành viên thường — kế thừa Member.
 *
 * Business rules:
 *   - Giới hạn mượn: 3 sách cùng lúc (BR5)
 *   - Phí phạt: 5,000 VND/ngày (BR7)
 */
public class RegularMember extends Member {

    private static final int    BORROW_LIMIT = 3;
    private static final double FINE_RATE    = 5000.0; // VND/ngày

    // ===================== CONSTRUCTOR =====================
    public RegularMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email);
    }

    // ===================== OVERRIDE ABSTRACT METHODS =====================

    @Override
    public double calculateFine(int days) {
        if (days <= 0) return 0;
        return days * FINE_RATE;
    }

    @Override
    public int getBorrowLimit() {
        return BORROW_LIMIT;
    }

    @Override
    public String getMemberType() {
        return "Regular";
    }

    @Override
    public double getFineRate() {
        return FINE_RATE;
    }

    // ===================== TOSTRING =====================
    @Override
    public String toString() {
        return super.toString();
    }
}