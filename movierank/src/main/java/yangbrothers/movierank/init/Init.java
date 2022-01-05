package yangbrothers.movierank.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import yangbrothers.movierank.dto.SignUpDTO;
import yangbrothers.movierank.entity.Role;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.repo.UserRepo;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Init {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        SignUpDTO signUpDTO = new SignUpDTO("yhw", "123", "123");
        User user = new User(signUpDTO, passwordEncoder, Role.ADMIN);

        userRepo.save(user);
    }
}