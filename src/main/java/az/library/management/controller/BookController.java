        package az.library.management.controller;

        import az.library.management.model.dto.book.BookDTO;
        import az.library.management.model.dto.book.NewBookDTO;
        import az.library.management.model.dto.book.UpdateBookDTO;
        import az.library.management.model.exception.NoBookFoundException;
        import az.library.management.service.BookService;
        import az.library.management.model.exception.BooksNotReturnedException;
        import lombok.RequiredArgsConstructor;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.*;

        import jakarta.validation.Valid;

        @RestController
        @RequestMapping("/books")
        @RequiredArgsConstructor
        public class BookController {
            private final BookService bookService;

            @PostMapping()
            @ResponseStatus(HttpStatus.CREATED)
            BookDTO addNewBook(@RequestBody @Valid NewBookDTO newBookDTO) {
                return bookService.addNewBook(newBookDTO);
            }

            @GetMapping()
            @ResponseStatus(HttpStatus.OK)
            Page<BookDTO> getBooks(Pageable pageable) {
                return bookService.getBooks(pageable);
            }

            @GetMapping("/{book_id}")
            @ResponseStatus(HttpStatus.OK)
            BookDTO getBookById(@PathVariable Long book_id) throws NoBookFoundException {
                return bookService.getBookById(book_id);
            }

            @PutMapping("/{book_id}")
            @ResponseStatus(HttpStatus.OK)
            BookDTO updateBook(@PathVariable Long book_id, @RequestBody @Valid UpdateBookDTO updateBookDTO) throws NoBookFoundException {
                return bookService.updateBook(book_id, updateBookDTO);
            }

            @DeleteMapping("/{book_id}")
            @ResponseStatus(HttpStatus.NO_CONTENT)
            void deleteBook(@PathVariable Long book_id) throws NoBookFoundException, BooksNotReturnedException {
                bookService.deleteBook(book_id);
            }

            @GetMapping("/available")
            @ResponseStatus(HttpStatus.OK)
            Page<BookDTO> getAvailableBooks(Pageable pageable){
                return bookService.getAvailableBooks(pageable);
            }
        }
