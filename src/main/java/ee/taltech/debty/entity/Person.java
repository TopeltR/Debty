package ee.taltech.debty.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email; // dummy user's email is null
    private String password;

    @NotNull
    private String firstName;
    private String lastName;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime modifiedAt;

    @Embedded
    private BankAccount bankAccount;
    @OneToMany
    private List<Event> events;

}

