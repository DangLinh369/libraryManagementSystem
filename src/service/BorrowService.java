/package service;

import exception.BookNotFoundException;
import exception.BusinessRuleViolationException;
import exception.InvalidInputException;
import exception.MemberNotFoundException;
import model.Book;
import model.BorrowingTransaction;
import model.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowService {

    private final List<BorrowingTransaction> transactions;

    private final BookService bookService;
    private final MemberService memberService;

    public BorrowService(BookService bookService,
                         MemberService memberService) {

        this.bookService = bookService;
        this.memberService = memberService;
        this.transactions = new ArrayList<>();
    }

    public void borrowBook(String transactionId,
                           String memberId,
                           String bookId,
                           LocalDate borrowDate)

            throws InvalidInputException,
                   BookNotFoundException,
                   MemberNotFoundException,
                   BusinessRuleViolationException {

        validateBorrowInput(
                transactionId,
                memberId,
                bookId,
                borrowDate);

        Member member = memberService.findMember(memberId);

        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }

        Book book = bookService.findBook(bookId);

        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        if (!book.isAvailable()) {
            throw new BusinessRuleViolationException(
                    "Book is currently unavailable.");
        }

        if (!member.canBorrow()) {
            throw new BusinessRuleViolationException(
                    "Member has reached borrowing limit.");
        }

        book.decreaseAvailable();

        book.setBorrowCount(
                book.getBorrowCount() + 1);

        BorrowingTransaction transaction =
                new BorrowingTransaction(
                        transactionId,
                        book,
                        member,
                        borrowDate);

        transactions.add(transaction);
    }

    public double returnBook(String memberId,
                             String bookId,
                             LocalDate returnDate)

            throws InvalidInputException,
                   BusinessRuleViolationException {

        if (memberId == null || memberId.isBlank()) {
            throw new InvalidInputException(
                    "Member ID cannot be empty.");
        }

        if (bookId == null || bookId.isBlank()) {
            throw new InvalidInputException(
                    "Book ID cannot be empty.");
        }

        if (returnDate == null) {
            throw new InvalidInputException(
                    "Return date cannot be null.");
        }

        BorrowingTransaction transaction =
                getCurrentBorrowRecord(
                        memberId,
                        bookId);

        if (transaction == null) {
            throw new BusinessRuleViolationException(
                    "No active borrowing record found.");
        }

        transaction.setReturnDate(returnDate);

        transaction.getBook()
                   .increaseAvailable();

        return transaction.calculateFine();
    }

    public List<BorrowingTransaction> viewBorrowedBooks() {

        List<BorrowingTransaction> result =
                new ArrayList<>();

        for (BorrowingTransaction t : transactions) {

            if (t.getReturnDate() == null) {
                result.add(t);
            }
        }

        return result;
    }

    public List<BorrowingTransaction> viewOverdueBooks() {

        List<BorrowingTransaction> result =
                new ArrayList<>();

        for (BorrowingTransaction t : transactions) {

            if (t.isOverdue()) {
                result.add(t);
            }
        }

        return result;
    }

    public List<BorrowingTransaction> viewMemberHistory(
            String memberId) {

        List<BorrowingTransaction> result =
                new ArrayList<>();

        for (BorrowingTransaction t : transactions) {

            if (t.getMember()
                 .getMemberId()
                 .equalsIgnoreCase(memberId)) {

                result.add(t);
            }
        }

        return result;
    }

    public BorrowingTransaction getCurrentBorrowRecord(
            String memberId,
            String bookId) {

        for (BorrowingTransaction t : transactions) {

            if (t.getMember()
                 .getMemberId()
                 .equalsIgnoreCase(memberId)

                    &&

                t.getBook()
                 .getBookId()
                 .equalsIgnoreCase(bookId)

                    &&

                t.getReturnDate() == null) {

                return t;
            }
        }

        return null;
    }

    public List<BorrowingTransaction> getAllTransactions() {
        return transactions;
    }

    private void validateBorrowInput(
            String transactionId,
            String memberId,
            String bookId,
            LocalDate borrowDate)

            throws InvalidInputException {

        if (transactionId == null
                || transactionId.isBlank()) {

            throw new InvalidInputException(
                    "Transaction ID cannot be empty.");
        }

        if (memberId == null
                || memberId.isBlank()) {

            throw new InvalidInputException(
                    "Member ID cannot be empty.");
        }

        if (bookId == null
                || bookId.isBlank()) {

            throw new InvalidInputException(
                    "Book ID cannot be empty.");
        }

        if (borrowDate == null) {

            throw new InvalidInputException(
                    "Borrow date cannot be null.");
        }
    }
}