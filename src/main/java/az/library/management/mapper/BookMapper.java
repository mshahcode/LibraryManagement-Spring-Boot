package az.library.management.mapper;

import az.library.management.dao.entity.Book;
import az.library.management.model.dto.book.BookDTO;
import az.library.management.model.dto.book.NewBookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class BookMapper {
    public static BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    public abstract BookDTO mapBookToBookDto(Book book);

    @Mapping(target = "totalCopies",source = "totalCopies")
    @Mapping(target = "availableCopies", source = "totalCopies")
    public abstract Book mapNewBookDtoToBook(NewBookDTO newBookDTO);

}
