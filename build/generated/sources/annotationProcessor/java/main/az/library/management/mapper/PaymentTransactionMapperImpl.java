package az.library.management.mapper;

import az.library.management.dao.entity.PaymentTransaction;
import az.library.management.model.dto.transaction.PaymentTransactionDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-25T15:16:50+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PaymentTransactionMapperImpl extends PaymentTransactionMapper {

    @Override
    public PaymentTransactionDTO mapPaymentTrantoPatmentTranDTO(PaymentTransaction paymentTransaction) {
        if ( paymentTransaction == null ) {
            return null;
        }

        PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();

        paymentTransactionDTO.setId( paymentTransaction.getId() );
        paymentTransactionDTO.setFinalAmount( paymentTransaction.getFinalAmount() );

        return paymentTransactionDTO;
    }
}
