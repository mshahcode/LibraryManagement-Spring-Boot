package az.library.management.service;

import az.library.management.dao.entity.Book;
import az.library.management.model.dto.book.NewBookDTO;
import az.library.management.model.dto.book.UpdateBookDTO;
import az.library.management.model.exception.NoBookFoundException;
import az.library.management.dao.repository.BookRepository;
import az.library.management.mapper.BookMapper;
import az.library.management.model.dto.book.BookDTO;
import az.library.management.model.exception.BooksNotReturnedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookDTO addNewBook(NewBookDTO newBookDTO) {
        Book book = BookMapper.INSTANCE.mapNewBookDtoToBook(newBookDTO);
        bookRepository.save(book);
        return BookMapper.INSTANCE.mapBookToBookDto(book);
    }

    public Page<BookDTO> getBooks(Pageable pageable){
        return bookRepository.findAll(pageable).map(BookMapper.INSTANCE::mapBookToBookDto);
    }

    public BookDTO getBookById(Long book_id) throws NoBookFoundException {
        Book book = bookRepository.findById(book_id).
                orElseThrow(() -> new NoBookFoundException("No book exists with id: " + book_id));
        return BookMapper.INSTANCE.mapBookToBookDto(book);
    }

    public BookDTO updateBook(Long book_id, UpdateBookDTO updateBookDTO) throws NoBookFoundException {
        Book book = bookRepository.findById(book_id).
                orElseThrow(() -> new NoBookFoundException("No book exists with id: " + book_id));
        book.setTitle(updateBookDTO.getTitle());
        book.setAuthor(updateBookDTO.getAuthor());
        return BookMapper.INSTANCE.mapBookToBookDto(bookRepository.save(book));
    }

    public void deleteBook(Long book_id) throws NoBookFoundException, BooksNotReturnedException {
        Book book = bookRepository.findById(book_id).
                orElseThrow(() -> new NoBookFoundException("No book exists with id: " + book_id));
        if (book.getTransactions() == null || book.getTransactions().isEmpty()) { // if books don't have reserved books
            bookRepository.deleteById(book_id);
        } else {
            boolean allBooksReturned = book.getTransactions().stream().anyMatch(transaction -> transaction.getReturnTime() == null);
            if (allBooksReturned) {
                book.setTransactions(null);
                bookRepository.deleteById(book_id);
            } else {
                throw new BooksNotReturnedException("Book with id: " + book_id + " can't be deleted without returning all books!");
            }
        }
    }

	public Page<BookDTO> getAvailableBooks(Pageable pageable){
		return bookRepository.getAvailableBooks(pageable).map(BookMapper.INSTANCE::mapBookToBookDto);
    }
}
