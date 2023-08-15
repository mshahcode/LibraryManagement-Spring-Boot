package az.library.management.mapper;

import az.library.management.dao.entity.Book;
import az.library.management.model.dto.book.BookDTO;
import az.library.management.model.dto.book.NewBookDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T12:58:13+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class BookMapperImpl extends BookMapper {

    @Override
    public BookDTO mapBookToBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();

        bookDTO.setId( book.getId() );
        bookDTO.setTitle( book.getTitle() );
        bookDTO.setAuthor( book.getAuthor() );
        bookDTO.setIsbn( book.getIsbn() );
        bookDTO.setAvailableCopies( book.getAvailableCopies() );
        bookDTO.setTotalCopies( book.getTotalCopies() );

        return bookDTO;
    }

    @Override
    public Book mapNewBookDtoToBook(NewBookDTO newBookDTO) {
        if ( newBookDTO == null ) {
            return null;
        }

        Book book = new Book();

        book.setTotalCopies( newBookDTO.getTotalCopies() );
        book.setAvailableCopies( newBookDTO.getTotalCopies() );
        book.setTitle( newBookDTO.getTitle() );
        book.setAuthor( newBookDTO.getAuthor() );
        book.setIsbn( newBookDTO.getIsbn() );

        return book;
    }
}
