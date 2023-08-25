package az.library.management.service

import az.library.management.client.CashbackClient
import az.library.management.dao.entity.Book
import az.library.management.dao.entity.Transaction
import az.library.management.dao.entity.User
import az.library.management.dao.repository.BookRepository
import az.library.management.dao.repository.PaymentTransactionRepository
import az.library.management.dao.repository.TransactionRepository
import az.library.management.dao.repository.UserRepository
import az.library.management.model.dto.book.BooksBorrowRequestDTO
import az.library.management.model.dto.book.BooksReturnRequestDTO
import az.library.management.model.exception.BookUnavailableException
import az.library.management.model.exception.NoBookFoundException
import az.library.management.model.exception.NoTransactionException
import az.library.management.model.exception.NoUserFoundException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

@Subject(TransactionService)
class TransactionServiceTest extends Specification {

    // Classes to be mocked

    private TransactionRepository transactionRepository
    private UserRepository userRepository
    private BookRepository bookRepository
    private PaymentTransactionRepository paymentTransactionRepository
    private CashbackClient cashbackClient

    // class to be tested
    private TransactionService transactionService

    def setup(){
        this.transactionRepository = Mock(TransactionRepository)
        this.userRepository = Mock(UserRepository)
        this.bookRepository = Mock(BookRepository)
        this.paymentTransactionRepository = Mock(PaymentTransactionRepository)
        this.cashbackClient = Mock(CashbackClient)
        this.transactionService = new TransactionService(transactionRepository,userRepository,bookRepository,paymentTransactionRepository,cashbackClient)
    }

    def "BorrowBookTransaction no user found"() {
        given:
        def user_id = 1L
        def borrowRequestDTO = new BooksBorrowRequestDTO()

        when:
        transactionService.borrowBookTransaction(user_id,borrowRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.empty()
        thrown(NoUserFoundException)
        0 * _
    }

    def "BorrowBookTransaction no book found"() {
        given: "user with ID 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to borrow"
        def borrowRequestDTO = new BooksBorrowRequestDTO(book_ids: [1L,2L])

        and: "the book id that doesnt exist"
        def book_id = 1L

        when:
        transactionService.borrowBookTransaction(user_id,borrowRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        1 * bookRepository.findById(book_id) >> Optional.empty()
        thrown(NoBookFoundException)
    }

    def "BorrowBookTransaction no book found in second iteration"() {
        given: "user with ID 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to borrow"
        def borrowRequestDTO = new BooksBorrowRequestDTO(book_ids: [1L,2L])

        and: "the book id that doesnt exist"
        def book_id = 2L

        and: "Book that exists"
        Book book = new Book(id: 1L, availableCopies: 10)

        when:
        transactionService.borrowBookTransaction(user_id,borrowRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        1 * bookRepository.findById(1L) >> Optional.of(book)
        1 * bookRepository.save(_) >> book
        1 * transactionRepository.save(_) >> new Transaction()
        1 * bookRepository.findById(2L) >> Optional.empty()
        thrown(NoBookFoundException)

        and:
        book.availableCopies == 9
    }


    def "BorrowBookTransaction not enough books to borrow"() {
        given: "user with ID 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to borrow"
        def borrowRequestDTO = new BooksBorrowRequestDTO(book_ids: [1L,2L])

        and: "the book id that doesnt exist"
        def book_id = 1L

        when:
        transactionService.borrowBookTransaction(user_id,borrowRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        1 * bookRepository.findById(book_id) >> Optional.of(new Book(id: 1L,availableCopies: 0))
        thrown(BookUnavailableException)
        0 * bookRepository.save(_)
    }


    def "BorrowBookTransaction all good with transaction returned"() {
        given: "user with ID 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to borrow"
        def borrowRequestDTO = new BooksBorrowRequestDTO(book_ids: [1L,2L])

        and: "Book that exists"
        def books = [
                new Book(id: 1L, availableCopies: 4),
                new Book(id: 2L, availableCopies: 5)
        ]
        when:
        def results = transactionService.borrowBookTransaction(user_id,borrowRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        books.each {book ->
            1 * bookRepository.findById(book.id) >> Optional.of(book)
            1 * bookRepository.save(book) >> book
        }
        2 * transactionRepository.save(_) >> new Transaction()
        notThrown(BookUnavailableException)

        and:
        books.get(0).availableCopies == 3
        books.get(1).availableCopies == 4
        results.size() == 2
        results.get(0).user_id == 1L
    }

    def "ReturnBook when there is nothing to return"() {
        given: "user id 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to return"
        def booksReturnRequestDTO = new BooksReturnRequestDTO(book_ids: [1L, 2L])

        and: "Book that exists in db"
        def book = new Book(id: 1L, availableCopies: 4)

        when:
        transactionService.returnBook(user_id,booksReturnRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        1 * bookRepository.findById(book.id) >> Optional.of(book)
        1 * bookRepository.save(book)
        1 * transactionRepository.findByUserAndBookAndReturnTimeIsNull(user,book) >> []
        thrown(NoTransactionException)
    }


    def "ReturnBook return correctly"() {
        given: "user id 1"
        def user_id = 1L
        User user = new User(id: user_id)

        and: "the list of books IDs the user wants to return"
        def booksReturnRequestDTO = new BooksReturnRequestDTO(book_ids: [1L, 2L])

        and: "Book that exists in db"
        def books = [
                new Book(id: 1L, availableCopies: 4),
                new Book(id: 2L, availableCopies: 5)
        ]

        and: "Transaction that have not been returned"
        def transactions = [
                new Transaction(id:1L,borrow_time: LocalDateTime.now().minusMinutes(1L),user: user),
                new Transaction(id:2L,borrow_time: LocalDateTime.now().minusMinutes(1L),user: user)
        ]

        when:
        def results = transactionService.returnBook(user_id,booksReturnRequestDTO)

        then:
        1 * userRepository.findById(user_id) >> Optional.of(user)
        books.each {book ->
            1 * bookRepository.findById(book.id) >> Optional.of(book)
            1 * bookRepository.save(book)
            1 * transactionRepository.findByUserAndBookAndReturnTimeIsNull(user,book) >> transactions
        }
        transactions.each { transaction ->
            2 * transactionRepository.save(transaction)
        }
        1 * cashbackClient.getCashBack(_) >> 0.0
        1 * paymentTransactionRepository.save(_)
        transactions.get(0).fine_amount == 3.0
        results.finalAmount == 12.0
    }
}



