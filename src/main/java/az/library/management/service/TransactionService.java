package az.library.management.service;

import az.library.management.client.CashbackClient;
import az.library.management.dao.entity.Book;
import az.library.management.dao.entity.PaymentTransaction;
import az.library.management.dao.entity.User;
import az.library.management.dao.repository.PaymentTransactionRepository;
import az.library.management.dao.repository.TransactionRepository;
import az.library.management.mapper.PaymentTransactionMapper;
import az.library.management.mapper.TransactionMapper;
import az.library.management.model.dto.book.BooksReturnRequestDTO;
import az.library.management.model.dto.transaction.PaymentTransactionDTO;
import az.library.management.model.dto.transaction.TransactionDTO;
import az.library.management.model.exception.BookUnavailableException;
import az.library.management.model.exception.NoTransactionException;
import az.library.management.model.exception.NoUserFoundException;
import az.library.management.dao.entity.Transaction;
import az.library.management.dao.repository.BookRepository;
import az.library.management.dao.repository.UserRepository;
import az.library.management.model.dto.book.BooksBorrowRequestDTO;
import az.library.management.model.exception.NoBookFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final CashbackClient cashbackClient;

    public List<TransactionDTO> borrowBookTransaction(Long user_id, BooksBorrowRequestDTO borrowRequestDTO) throws NoUserFoundException, NoBookFoundException, BookUnavailableException {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new NoUserFoundException("No user exists with id: " + user_id));
        List<TransactionDTO> transactionDTOList = new ArrayList<>();
        for (Long book_id : borrowRequestDTO.getBook_ids()) {
            Book book = bookRepository.findById(book_id).
                    orElseThrow(() -> new NoBookFoundException("No book exists with id: " + book_id));
            if (book.getAvailableCopies() <= 0)
                throw new BookUnavailableException("Chosen book is unavailable anymore!");

            book.setAvailableCopies(book.getAvailableCopies() - 1);
            book = bookRepository.save(book);

            Transaction transaction = new Transaction();
            transaction.setFine_amount(0.0);
            transaction.setBorrow_time(LocalDateTime.now());
            transaction.setBook(book);
            transaction.setUser(user);
            TransactionDTO transactionDTO = TransactionMapper.INSTANCE
                    .mapTransactionToTransactionDto(transactionRepository.save(transaction));
            transactionDTO.setUser_id(user_id);
            transactionDTO.setBook_id(book_id);

            transactionDTOList.add(transactionDTO);
        }
        return transactionDTOList;
    }

    public PaymentTransactionDTO returnBook(Long user_id, BooksReturnRequestDTO booksReturnRequestDTO) throws NoUserFoundException, NoBookFoundException, NoTransactionException {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new NoUserFoundException("No user exists with id: " + user_id));

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setUser(user);
        LocalDateTime todayDate = LocalDateTime.now();

        var finalAmount = 0.0;
        paymentTransaction.setFinalAmount(finalAmount);
        for (var book_id : booksReturnRequestDTO.getBook_ids()) {
            Book book = bookRepository.findById(book_id).
                    orElseThrow(() -> new NoBookFoundException("No book exists with id: " + book_id));
            var transactions = transactionRepository.findByUserAndBookAndReturnTimeIsNull(user, book);

            if (transactions.isEmpty()) {
                throw new NoTransactionException("No Transaction made with user id: " + user_id + " and book id: " + book_id);
            }

            for (var transaction : transactions) {
                System.out.println(transaction);
                transaction.setReturnTime(todayDate);
                finalAmount = calculateFineAmount(transaction.getBorrow_time(), transaction.getReturnTime());
                paymentTransaction.setFinalAmount(paymentTransaction.getFinalAmount() + finalAmount);
                transaction.setFine_amount(finalAmount);
                transactionRepository.save(transaction);
            }

        }
        paymentTransaction.setFinalAmount(paymentTransaction.getFinalAmount() + cashbackClient.getCashBack(paymentTransaction.getFinalAmount()));
        paymentTransactionRepository.save(paymentTransaction);
        return PaymentTransactionMapper.INSTANCE.mapPaymentTrantoPatmentTranDTO(paymentTransaction);
    }

    private double calculateFineAmount(LocalDateTime borrowDate, LocalDateTime returnDate) {
        long daysPassed = Duration.between(borrowDate, returnDate).toMinutes();
        System.out.println(daysPassed);
        return 3 * daysPassed;
    }

}

