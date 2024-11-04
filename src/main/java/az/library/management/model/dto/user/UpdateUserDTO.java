package az.library.management.model.dto.user;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateUserDTO {
    @NotBlank
    private String name;

    @Email
    private String email;
}
