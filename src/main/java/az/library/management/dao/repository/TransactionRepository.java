package az.library.management.dao.repository;

import az.library.management.dao.entity.Book;
import az.library.management.dao.entity.User;
import az.library.management.dao.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserAndBookAndReturnTimeIsNull(User user, Book book);
}
