package az.library.management.model.dto.book;


import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
