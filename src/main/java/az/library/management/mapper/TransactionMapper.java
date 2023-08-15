package az.library.management.mapper;

import az.library.management.model.dto.transaction.TransactionDTO;
import az.library.management.dao.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public abstract class TransactionMapper {
    public static final TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    public abstract TransactionDTO mapTransactionToTransactionDto(Transaction transaction);

}
