package az.library.management.model.dto.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUserDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;
}
