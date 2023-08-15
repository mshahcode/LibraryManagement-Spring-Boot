package az.library.management.model.dto.book;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateBookDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String author;
}
