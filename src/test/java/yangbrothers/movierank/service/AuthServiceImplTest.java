package yangbrothers.movierank.service;

import com.mchange.util.AlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.TokenDTO;
import yangbrothers.movierank.entity.Member;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.JwtFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceImplTest {

    SpyUserRepo spyUserRepo = new SpyUserRepo();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    SpyAuthenticationManager spyAuthenticationManager = new SpyAuthenticationManager();
    SpyTokenProvider spyTokenProvider = new SpyTokenProvider();

    AuthServiceImpl authServiceImpl = new AuthServiceImpl(
            spyAuthenticationManager,
            spyUserRepo,
            passwordEncoder,
            spyTokenProvider,
            new StringRedisTemplate());


    @Test
    public void signUp_throwsAlreadyExistsException() throws Exception {
        spyUserRepo.findUserByNickname_returnValue = Optional.of(Member.builder().memberId(1L).build());
        SignUpDTO signUpDTO = new SignUpDTO();

        Assertions.assertThatThrownBy(() -> authServiceImpl.signUp(signUpDTO)).isInstanceOf(AlreadyExistsException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    public void signUp_throwsSignUpEx() throws Exception {
        spyUserRepo.findUserByNickname_returnValue = Optional.empty();
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234").passwordConfirm("4321").build();

        Assertions.assertThatThrownBy(() -> authServiceImpl.signUp(signUpDTO)).isInstanceOf(SignUpEx.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    public void signUp_passUserToRepository() throws Exception {
        spyUserRepo.findUserByNickname_returnValue = Optional.empty();

        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234").passwordConfirm("1234").build();
        authServiceImpl.signUp(signUpDTO);

        assertThat(spyUserRepo.save_argument.getUsername()).isEqualTo("양형욱");
        assertThat(passwordEncoder.matches("1234", spyUserRepo.save_argument.getPassword())).isTrue();
    }

    @Test
    public void login_throwsBadCredentialsException() throws Exception {
        spyAuthenticationManager.isPasswordNotMath = true;
        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234").build();

        assertThatThrownBy(() -> authServiceImpl.login(loginDTO)).isInstanceOf(BadCredentialsException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    public void login_passAuthenticationToTokenProvider() throws Exception {
        UserDetails user = User.builder().username("yhw").password("1234").roles("USER").build();
        Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
        spyAuthenticationManager.authenticate_returnValue = new UsernamePasswordAuthenticationToken(user, "token", authorities);

        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234").build();
        authServiceImpl.login(loginDTO);

        Authentication result = spyTokenProvider.createToken_argument;
        User principal = (User) result.getPrincipal();

        assertThat(principal).isInstanceOf(User.class);
        assertThat(principal.getUsername()).isEqualTo("yhw");
        assertThat(principal.getAuthorities()).hasSize(1);

        assertThat(result.getCredentials()).isEqualTo("token");
        assertThat(result.getAuthorities().contains(new SimpleGrantedAuthority("USER"))).isTrue();
    }

    @Test
    public void login_returnValue() throws Exception {
        spyTokenProvider.createToken_returnValue = "token";
        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234").build();
        ResponseEntity<TokenDTO> result = authServiceImpl.login(loginDTO);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().get(JwtFilter.AUTHORIZATION_HEADER)).contains("Bearer token");
        assertThat(result.getBody().getNickName()).isEqualTo("yhw");
        assertThat(result.getBody().getToken()).isEqualTo("Bearer token");
    }
}