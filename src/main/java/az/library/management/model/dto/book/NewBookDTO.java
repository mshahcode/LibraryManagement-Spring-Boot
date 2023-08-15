package az.library.management.model.dto.book;


import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewBookDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    @Size(min = 14,max = 14, message = "ISBN must be exactly 14 characters")
    private String isbn;

    @Min(value = 1)
    @NotNull
    private Integer totalCopies;
}
