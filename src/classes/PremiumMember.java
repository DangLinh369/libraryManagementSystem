package model;

/**
 * Thành viên cao cấp — kế thừa Member.
 *
 * Business rules:
 *   - Giới hạn mượn: 5 sách cùng lúc (BR5)
 *   - Phí phạt: 2,000 VND/ngày (BR7) — ưu đãi hơn Regular
 */
public class PremiumMember extends Member {

    private static final int    BORROW_LIMIT = 5;
    private static final double FINE_RATE    = 2000.0; // VND/ngày

    // ===================== CONSTRUCTOR =====================
    public PremiumMember(String memberId, String name, String phone, String email) {
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
        return "Premium";
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