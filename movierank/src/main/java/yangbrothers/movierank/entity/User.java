package yangbrothers.movierank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import yangbrothers.movierank.dto.SignUpDTO;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(SignUpDTO signUpDTO, PasswordEncoder passwordEncoder) {
        this.username = signUpDTO.getUsername();
        this.password = passwordEncoder.encode(signUpDTO.getPassword());
        this.role = Role.USER;
    }
}