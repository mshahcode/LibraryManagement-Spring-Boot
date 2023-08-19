    package az.library.management.dao.entity;

    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import javax.persistence.*;
    import javax.validation.constraints.Email;
    import javax.validation.constraints.NotBlank;
    import java.util.Collection;
    import java.util.Collections;
    import java.util.List;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
        private List<Transaction> transactions;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
        private List<PaymentTransaction> paymentTransactions;

        @Enumerated(EnumType.STRING)
        private Role role;


    }
