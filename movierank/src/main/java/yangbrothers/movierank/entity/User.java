package yangbrothers.movierank.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import yangbrothers.movierank.dto.request.SignUpDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<BookMark> bookMarkList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(SignUpDTO signUpDTO, PasswordEncoder passwordEncoder) {
        this.username = signUpDTO.getUsername();
        this.password = passwordEncoder.encode(signUpDTO.getPassword());
        this.role = Role.USER;
    }
}