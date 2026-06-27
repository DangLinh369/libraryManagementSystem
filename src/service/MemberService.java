
package service;


import exception.BusinessRuleViolationException;
import exception.MemberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.Member;
import util.DataInputValidator;

public class MemberService {
    private List<Member> members;
    private DataInputValidator validator;

    public MemberService(List<Member> Member, DataInputValidator validator) {
        this.members = members;
        this.validator = validator;
    }
    // add new member
    public  void addMember(Member m){
        if (validator.validateMemberData(m)){
            members.add(m);
            System.out.println("Thêm thành viên mới thành công");
        }
        else {
            System.out.println("Dữ liệu thành viên không hợp lệ");
        }
    }
     public void updateMember (String id , Member m) throws MemberNotFoundException{
         if(!validator.validateMemberData(m)){
             throw new IllegalArgumentException("Dữ liệu thành viên mới không hợp lệ !");
         }
         if(!m.getMemberId().equals(id)){
             throw new IllegalArgumentException("Không được phép thay đổi ID của thành viên");
             
         }
         Member oldMember = findMember(id);
          int index = members.indexOf(oldMember);
          members.set(index,m);
     }
     public Member findMember(String memberId) throws MemberNotFoundException {
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) {
                return m;
            }
        }
        throw new MemberNotFoundException("Lỗi: Không tìm thấy thành viên với ID: " + memberId);
    }
            
    public void removeMember(String id) throws MemberNotFoundException, BusinessRuleViolationException {
    Member m = findMember(id); 

   
    members.remove(m); 
}
    
    public List<Member>  getAllMembers (){
    return members;
        
    }
    public List<Member> searchMembers(String keyword) {
    List<Member> result = new ArrayList<>();
    for (Member m : members) {
        if (m.getName().contains(keyword)) {
            result.add(m);
        }
    }
    return result;
}
}
