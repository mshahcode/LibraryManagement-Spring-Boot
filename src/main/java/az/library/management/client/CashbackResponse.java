package az.library.management.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CashbackResponse {
    @JsonProperty("cashbackAmount")
    private Double cashbackAmount;
}
