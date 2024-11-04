package az.library.management.model.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksReturnRequestDTO {
    @Size(min = 1,message = "At least one book has to be returned")
    @NotNull
    List<Long> book_ids;
}
