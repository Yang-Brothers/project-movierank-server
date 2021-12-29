package yangbrothers.movierank.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import yangbrothers.movierank.dto.UserDTO;
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
        UserDTO userDTO = new UserDTO("yhw", "123");
        User user = new User(userDTO, passwordEncoder);

        userRepo.save(user);
    }
}