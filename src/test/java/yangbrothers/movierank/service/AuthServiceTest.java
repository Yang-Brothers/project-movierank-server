package yangbrothers.movierank.service;

import com.mchange.util.AlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.repo.UserRepo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepo userRepo;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ValueOperations valueOperations;

    @Spy
    TokenProvider tokenProvider = new TokenProvider("ksdjfhksjdhfjkskjdhfkjsdhfkjsdhjkfdsjhfkjsdhfjksdkjfjhksdkjfhskjdhfkjsdhfdsjkhfdshsdfsdff", 3213L, redisTemplate);

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("signUpTest")
    public void signUpTest() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO("yhw", "123", "123");

        User user = new User(signUpDTO, passwordEncoder);
        given(userRepo.save(any(User.class))).willReturn(user);

        ResponseEntity<SignUpResponseDTO> signUpResponseDTO = authService.signUp(signUpDTO);

        assertThat(signUpResponseDTO.getBody().getUsername()).isEqualTo("yhw");
    }

    @Test
    @DisplayName("signUpFailTest1")
    public void signUpFailTest1() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO("yhw", "123", "321");
        assertThatThrownBy(() -> authService.signUp(signUpDTO)).isInstanceOf(SignUpEx.class).hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("signUpFailTest2")
    public void signUpFailTest2() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO("yhw", "123", "123");
        User user = new User(new SignUpDTO("yhw", "123", "123"), passwordEncoder);
        given(userRepo.findUserByUsername("yhw")).willReturn(Optional.of(user));
        assertThatThrownBy(() -> authService.signUp(signUpDTO)).isInstanceOf(AlreadyExistsException.class).hasMessageContaining("이미 존재하는 아이디입니다.");
    }

    @Test
    @DisplayName("loginTest")
    public void loginTest() throws Exception {
        LoginDTO loginDTO = new LoginDTO("yhw", "123");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User("yhw", "123",
                List.of(new SimpleGrantedAuthority("USER"))), "123");

        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken);

        assertThat(authService.login(loginDTO).getBody().getToken()).isEqualTo(jwt);
    }

    @Test
    @DisplayName("logoutTest")
    public void logoutTest() throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User("yhw", "123",
                List.of(new SimpleGrantedAuthority("USER"))), "123");
        String jwt = tokenProvider.createToken(authenticationToken);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        ResponseEntity<CommonResult> commonResult = authService.logout(request);

        assertThat(commonResult.getBody().getResult()).isEqualTo("SUCCESS");
        assertThat(commonResult.getBody().getCode()).isEqualTo("200");
        assertThat(commonResult.getBody().getValidationMsg()).isNullOrEmpty();
    }
}