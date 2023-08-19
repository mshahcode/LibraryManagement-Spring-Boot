package az.library.management.client;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CashbackClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public Double getCashBack(Double transactionAmount){
        String url = "https://cardzone-cashback-api-c2f5b8105e2b.herokuapp.com/api/cashback?transactionAmount={transactionAmount}"
                .replace("{transactionAmount}",transactionAmount.toString());
        CashbackResponse cashbackResponse = this.restTemplate.getForObject(url, CashbackResponse.class);
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
        if(cashbackResponse == null){
            return 0.0;
        }
        return cashbackResponse.getCashbackAmount();
    }
}
