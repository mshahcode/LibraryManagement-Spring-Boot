package az.library.management.model.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksBorrowRequestDTO {
    @Size(min = 1,message = "At least one book has to be borrowed")
    @NotNull
    List<Long> book_ids;
}
