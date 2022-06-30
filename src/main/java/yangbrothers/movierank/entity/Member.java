package yangbrothers.movierank.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        allocationSize = 1
)
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    private Long memberId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickName;

    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    List<BookMark> bookMarkList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(SignUpDTO signUpDTO, PasswordEncoder passwordEncoder) {
        this.username = signUpDTO.getUsername();
        this.nickName = signUpDTO.getNickName();
        this.password = passwordEncoder.encode(signUpDTO.getPassword());
        this.role = Role.USER;
    }
}