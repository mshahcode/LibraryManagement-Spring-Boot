    package az.library.management.dao.entity;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.springframework.format.annotation.DateTimeFormat;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.DecimalMin;
    import jakarta.validation.constraints.NotNull;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "transactions")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @DecimalMin("0.0")
        private Double fine_amount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime borrow_time;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime returnTime;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "book_id")
        private Book book;

    }
