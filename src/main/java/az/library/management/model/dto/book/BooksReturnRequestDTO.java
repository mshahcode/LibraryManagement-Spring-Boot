package az.library.management.model.dto.book;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BooksReturnRequestDTO {
    @Size(min = 1,message = "At least one book has to be returned")
    @NotNull
    List<Long> book_ids;
}
