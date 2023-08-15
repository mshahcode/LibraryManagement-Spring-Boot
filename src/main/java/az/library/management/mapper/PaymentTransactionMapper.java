package az.library.management.mapper;

import az.library.management.dao.entity.PaymentTransaction;
import az.library.management.model.dto.transaction.PaymentTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class PaymentTransactionMapper {
    public static PaymentTransactionMapper INSTANCE = Mappers.getMapper(PaymentTransactionMapper.class);

    public abstract PaymentTransactionDTO mapPaymentTrantoPatmentTranDTO(PaymentTransaction paymentTransaction);
}
