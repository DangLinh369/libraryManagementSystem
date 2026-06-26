package exception;

/**
 * Ném ra khi tìm không thấy thành viên theo ID.
 * Ví dụ: MemberService.findMember("M999") → member không tồn tại
 */
public class MemberNotFoundException extends LibraryException {

    public MemberNotFoundException(String memberId) {
        super("Member not found with ID: " + memberId);
    }
}