package az.library.management.service

import az.library.management.dao.entity.Book
import az.library.management.dao.entity.Transaction
import az.library.management.dao.repository.BookRepository
import az.library.management.model.dto.book.NewBookDTO
import az.library.management.model.dto.book.UpdateBookDTO
import az.library.management.model.exception.BooksNotReturnedException
import az.library.management.model.exception.NoBookFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime


@Subject(BookService)
class BookServiceTest extends Specification {

    // Class to be mocked
    private BookRepository bookRepository

    // Class to be tested
    private BookService bookService

    def setup() {
        this.bookRepository = Mock(BookRepository)
        this.bookService = new BookService(bookRepository)
    }

    def "AddNewBook Should save and Return BookDTO"() {
        given: "a new book is given to add to database"
        NewBookDTO newBookDTO = new NewBookDTO(author: "Mikayil Shah")

        and: "the saved and returned back book that we expect"
        Book expectedSavedBook = new Book(author: "Mikayil Shah")

        when:
        def addedBook = bookService.addNewBook(newBookDTO)

        then:
        1 * bookRepository.save(_ as Book) >> expectedSavedBook
        addedBook.author == "Mikayil Shah"

    }

    def "GetBooks Page when there is no book"() {
        given:
        Pageable pageable = PageRequest.of(0, 10)

        when:
        def resultBooks = bookService.getBooks(pageable)

        then:
        1 * bookRepository.findAll(pageable) >> Page.empty(pageable)
        0 * _
        resultBooks.empty
    }

    def "GetBooks Page when there some books"() {
        given:
        Pageable pageable = PageRequest.of(0, 10)

        and:
        Page<Book> expectedBooks = new PageImpl<>([new Book(author: "Mika"), new Book(author: "Aydin")], pageable, 2)

        when:
        def resultBooks = bookService.getBooks(pageable)

        then:
        1 * bookRepository.findAll(pageable) >> expectedBooks
        with(resultBooks) {
            !empty
            numberOfElements == 2
        }
        with(resultBooks.content[0]) {
            author == "Mika"
        }
    }

    def "GetBookById When Book Id isNull"() {
        given: "book id is not known"
        def book_id = null

        when:
        def resultGetBookById = bookService.getBookById(book_id)

        then: " result is null"
        resultGetBookById == null

        and: "no book is found exception will not be invoked"
        0 * bookRepository.findById(book_id)
        notThrown(NoBookFoundException)
    }

    def "GetBookById but when there is no such book"() {
        given: "book id is not known"
        def book_id = 1

        when:
        bookService.getBookById(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.empty()
        NoBookFoundException e = thrown()
        e.getMessage() == "No book exists with id: " + book_id
    }

    def "GetBookById correct usage"() {
        given: "book id is not known"
        def book_id = 1

        when:
        def resultBook = bookService.getBookById(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(new Book(id: 1L, author: "Mikayil"))
        notThrown(NoBookFoundException)
        with(resultBook) {
            id == 1L
            author == "Mikayil"
        }
    }

    def "UpdateBook"() {
        given:
        def book_id = 1L
        and:
        UpdateBookDTO updateBookDTO = new UpdateBookDTO(title: "Girl in Forest", author: "Mikayil")

        when:
        def updatedBook = bookService.updateBook(book_id, updateBookDTO)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(new Book(id: 1L, author: "Mika"))
        1 * bookRepository.save(_ as Book) >> new Book(id: 1L, author: "Mikayil", title: "Girl in Forest")
        0 * _
        updatedBook.author == updateBookDTO.getAuthor()
        updatedBook.title == updateBookDTO.getTitle()
    }


    def "DeleteBook when no transaction related"() {
        given: "book with fully returned state"
        def book_id = 1L
        Book book = new Book(id: book_id)

        expect: "that no transaction is related to given book"
        book.getTransactions() == null

        when:
        bookService.deleteBook(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(book)
        notThrown(NoBookFoundException)

        and:
        1 * bookRepository.deleteById(book_id)
        // 0 * _ // No other interactions with bookRepository should happen
    }

    def "DeleteBook when no transaction related"() {
        given: "book with fully returned state"
        def book_id = 1L
        Book book = new Book(id: book_id)

        expect: "that no transaction is related to given book"
        book.getTransactions() == null

        when:
        bookService.deleteBook(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(book)
        notThrown(NoBookFoundException)

        and:
        1 * bookRepository.deleteById(book_id)
        // 0 * _ // No other interactions with bookRepository should happen
    }

    def "DeleteBook when transaction related"() {
        given: "book with fully returned state"
        def book_id = 1L
        Book book = new Book(id: book_id,transactions: [])

        and: "corresponding unfinished transaction"
        Transaction transaction = new Transaction()
        book.getTransactions().add(transaction)
        transaction.setBook(book)

        expect: "that there is some unfinished transaction related to given book"
        book.getTransactions() != null

        when:
        bookService.deleteBook(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(book)
        BooksNotReturnedException e = thrown()
        e.message == "Book with id: " + book_id + " can't be deleted without returning all books!"
        0 * _ // no other methods of bookRepository were called

    }

    def "DeleteBook when finished transaction related"() {
        given: "book with fully returned state"
        def book_id = 1L
        Book book = new Book(id: book_id,transactions: [])

        and: "corresponding finished transaction"
        Transaction transaction = new Transaction(returnTime: LocalDateTime.now())
        book.getTransactions().add(transaction)
        transaction.setBook(book)

        expect: "that there is some finished transaction related to given book"
        book.getTransactions() != null

        when:
        bookService.deleteBook(book_id)

        then:
        1 * bookRepository.findById(book_id) >> Optional.of(book)
        notThrown(BooksNotReturnedException)
        book.transactions == null
        1 * bookRepository.deleteById(book_id)
    }

    def "GetAvailableBooks"() {
    }
}
