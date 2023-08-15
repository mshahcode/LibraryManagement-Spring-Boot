package az.library.management.mapper;

import az.library.management.dao.entity.Transaction;
import az.library.management.model.dto.transaction.TransactionDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T12:58:13+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl extends TransactionMapper {

    @Override
    public TransactionDTO mapTransactionToTransactionDto(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setId( transaction.getId() );
        transactionDTO.setBorrow_time( transaction.getBorrow_time() );
        transactionDTO.setFine_amount( transaction.getFine_amount() );

        return transactionDTO;
    }
}
