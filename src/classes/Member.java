package model;

import java.io.Serializable;

/**
 * Lớp trừu tượng đại diện cho thành viên thư viện.
 * Là lớp cha của RegularMember và PremiumMember.
 *
 * Liên quan UML:
 *   - «abstract» class
 *   - RegularMember và PremiumMember kế thừa (Inheritance)
 *   - MemberService có Composition với Member
 *   - BorrowingTransaction có Composition với Member
 *
 * Polymorphism: các method abstract bên dưới sẽ được
 * override khác nhau ở RegularMember và PremiumMember.
 */
public abstract class Member implements Serializable {

    // ===================== FIELDS =====================
    private String memberId;     // BR1: Unique, không được thay đổi
    private String name;
    private String phone;
    private String email;
    private int currentBorrowCount; // Số sách đang mượn hiện tại

    // ===================== CONSTRUCTOR =====================
    public Member(String memberId, String name, String phone, String email) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentBorrowCount = 0;
    }

    // ===================== ABSTRACT METHODS (Polymorphism) =====================

    /**
     * Tính tiền phạt dựa trên số ngày quá hạn.
     * BR7: Overdue fine = days overdue × fine rate.
     * RegularMember: 5,000 VND/ngày
     * PremiumMember: 2,000 VND/ngày
     */
    public abstract double calculateFine(int days);

    /**
     * Giới hạn số sách được mượn cùng lúc.
     * BR5: RegularMember = 3, PremiumMember = 5
     */
    public abstract int getBorrowLimit();

    /**
     * Trả về loại thành viên dưới dạng String.
     * RegularMember → "Regular", PremiumMember → "Premium"
     */
    public abstract String getMemberType();

    /**
     * Trả về đơn giá phạt mỗi ngày.
     */
    public abstract double getFineRate();

    // ===================== BUSINESS METHODS =====================

    /**
     * Kiểm tra thành viên có thể mượn thêm sách không.
     * BR5: A book can only be borrowed if the member has not exceeded their borrowing limit.
     */
    public boolean canBorrow(int currentBorrowCount) {
        return currentBorrowCount < getBorrowLimit();
    }

    /** Tăng số sách đang mượn (gọi khi mượn sách). */
    public void increaseBorrowCount() { this.currentBorrowCount++; }

    /** Giảm số sách đang mượn (gọi khi trả sách). */
    public void decreaseBorrowCount() {
        if (this.currentBorrowCount > 0) this.currentBorrowCount--;
    }

    // ===================== GETTERS =====================
    public String getMemberId()        { return memberId; }
    public String getName()            { return name; }
    public String getPhone()           { return phone; }
    public String getEmail()           { return email; }
    public int getCurrentBorrowCount() { return currentBorrowCount; }

    // ===================== SETTERS =====================
    // memberId KHÔNG có setter → BR1: ID không được thay đổi

    public void setName(String name)   { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    // ===================== TOSTRING =====================
    @Override
    public String toString() {
        return String.format("%-6s %-20s %-12s %-25s %-10s %d/%d",
                memberId, name, phone, email,
                getMemberType(), currentBorrowCount, getBorrowLimit());
    }
}