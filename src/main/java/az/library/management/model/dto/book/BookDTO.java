package az.library.management.model.dto.book;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private Integer totalCopies;
}
