package az.library.management.controller;

import az.library.management.model.dto.book.BooksReturnRequestDTO;
import az.library.management.model.dto.transaction.PaymentTransactionDTO;
import az.library.management.model.dto.transaction.TransactionDTO;
import az.library.management.model.exception.BookUnavailableException;
import az.library.management.model.exception.NoTransactionException;
import az.library.management.model.exception.NoUserFoundException;
import az.library.management.service.TransactionService;
import az.library.management.model.dto.book.BooksBorrowRequestDTO;
import az.library.management.model.exception.NoBookFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/{user_id}/books/borrow")
    List<TransactionDTO> borrowBookTransaction(@PathVariable Long user_id, @RequestBody @Valid BooksBorrowRequestDTO borrowRequestDTO) throws NoBookFoundException, NoUserFoundException, BookUnavailableException {
        return transactionService.borrowBookTransaction(user_id,borrowRequestDTO);
    }

    @PostMapping("/{user_id}/books/return")
    PaymentTransactionDTO returnBook(@PathVariable Long user_id, @RequestBody @Valid BooksReturnRequestDTO booksReturnRequestDTO) throws NoBookFoundException, NoUserFoundException, BookUnavailableException, NoTransactionException {
        return transactionService.returnBook(user_id,booksReturnRequestDTO);
    }


}
