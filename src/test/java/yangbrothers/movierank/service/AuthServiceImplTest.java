package yangbrothers.movierank.service;

import com.mchange.util.AlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.ex.SignUpEx;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest {

    SpyUserRepo spyUserRepo = new SpyUserRepo();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    AuthServiceImpl authServiceImpl = new AuthServiceImpl(
            new ProviderManager(new DaoAuthenticationProvider()),
            spyUserRepo,
            passwordEncoder,
            new SpyTokenProvider(),
            new StringRedisTemplate());


    @Test
    public void signUp_throwsAlreadyExistsException() throws Exception {
        spyUserRepo.findUserByUsername_returnValue = Optional.of(User.builder().userId(1L).build());
        SignUpDTO signUpDTO = new SignUpDTO();

        Assertions.assertThatThrownBy(() -> authServiceImpl.signUp(signUpDTO)).isInstanceOf(AlreadyExistsException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    public void signUp_throwsSignUpEx() throws Exception {
        spyUserRepo.findUserByUsername_returnValue = Optional.empty();
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234").passwordConfirm("4321").build();

        Assertions.assertThatThrownBy(() -> authServiceImpl.signUp(signUpDTO)).isInstanceOf(SignUpEx.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    public void signUp_passUserToRepository() throws Exception {
        spyUserRepo.findUserByUsername_returnValue = Optional.empty();

        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234").passwordConfirm("1234").build();
        authServiceImpl.signUp(signUpDTO);

        assertThat(spyUserRepo.save_argument.getUsername()).isEqualTo("양형욱");
        assertThat(passwordEncoder.matches("1234", spyUserRepo.save_argument.getPassword())).isTrue();
    }
}