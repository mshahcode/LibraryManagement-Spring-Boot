package az.library.management.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String errorMessage;
    private List<String> errors;

    public ErrorResponse(String errorMessage, String error){
        this.errorMessage = errorMessage;
        this.errors = Collections.singletonList(error);

    }
}
