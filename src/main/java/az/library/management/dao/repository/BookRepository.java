package az.library.management.dao.repository;

import az.library.management.dao.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT b from Book b where b.availableCopies > 0")
    Page<Book> getAvailableBooks(Pageable pageable);
}
