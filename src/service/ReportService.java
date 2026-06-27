package Services;

import Entities.Book;
import Entities.BorrowingTransaction;
import Entities.Member;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportService {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final MemberService memberService;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReportService(BorrowService borrowService, BookService bookService,
                         MemberService memberService) {
        this.borrowService = borrowService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    public List<BorrowingTransaction> listBorrowedBooks() {
        return borrowService.viewBorrowedBooks();
    }

    public List<BorrowingTransaction> listOverdueBooks() {
        List<BorrowingTransaction> result = new ArrayList<>();
        for (BorrowingTransaction t : borrowService.getAllTransactions()) {
            if (t.isOverdue()) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Book> popularBooks() {
        List<Book> sorted = new ArrayList<>(bookService.getAllBooks());
        Collections.sort(sorted, (a, b) -> Integer.compare(b.getBorrowCount(), a.getBorrowCount()));
        return sorted;
    }

    public List<Member> topMembers() {
        List<Member> sorted = new ArrayList<>(memberService.getAllMembers());
        Collections.sort(sorted, (a, b) ->
                Integer.compare(countBorrowings(b), countBorrowings(a)));
        return sorted;
    }

    private int countBorrowings(Member member) {
        int count = 0;
        for (BorrowingTransaction t : borrowService.getAllTransactions()) {
            if (t.getMember().getMemberId().equalsIgnoreCase(member.getMemberId())) {
                count++;
            }
        }
        return count;
    }

    public String generate(String type) {
        StringBuilder sb = new StringBuilder();
        switch (type.toLowerCase()) {
            case "borrowed":
                sb.append("CURRENTLY BORROWED BOOKS\n");
                for (BorrowingTransaction t : listBorrowedBooks()) {
                    sb.append(t.getBook().getBookId()).append(" - ")
                      .append(t.getBook().getTitle()).append(" | ")
                      .append(t.getMember().getName()).append("\n");
                }
                break;
            case "overdue":
                sb.append("OVERDUE BOOKS\n");
                for (BorrowingTransaction t : listOverdueBooks()) {
                    sb.append(t.getBook().getBookId()).append(" - ")
                      .append(t.getBook().getTitle())
                      .append(" | Due: ").append(t.getDueDate().format(DATE_FORMAT))
                      .append(" | Fine: ").append((long) t.calculateFine()).append(" VND\n");
                }
                break;
            case "popular":
                sb.append("MOST POPULAR BOOKS\n");
                for (Book b : popularBooks()) {
                    sb.append(b.getBookId()).append(" - ").append(b.getTitle())
                      .append(" | Times borrowed: ").append(b.getBorrowCount()).append("\n");
                }
                break;
            case "members":
                sb.append("TOP MEMBERS\n");
                for (Member m : topMembers()) {
                    sb.append(m.getMemberId()).append(" - ").append(m.getName())
                      .append(" | Borrowings: ").append(countBorrowings(m)).append("\n");
                }
                break;
            default:
                sb.append("Unknown report type: ").append(type);
        }
        return sb.toString();
    }

//xuatreport
    
    public void export(String filename) throws Exception {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(generate("borrowed"));
            writer.write("\n");
            writer.write(generate("overdue"));
            writer.write("\n");
            writer.write(generate("popular"));
            writer.write("\n");
            writer.write(generate("members"));
        }
    }
}
