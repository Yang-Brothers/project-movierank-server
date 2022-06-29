package yangbrothers.movierank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.jwt.JwtAccessDeniedHandler;
import yangbrothers.movierank.jwt.JwtAuthenticationEntryPoint;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.service.AuthService;
import yangbrothers.movierank.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({ObjectMapper.class, JwtAuthenticationEntryPoint.class, HttpEncodingAutoConfiguration.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    @DisplayName("signUpTest")
    @WithAnonymousUser
    public void signUpTest() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO("yhw", "123", "123");
        given(authService.signUp(any(SignUpDTO.class))).willReturn(any(ResponseEntity.class));

        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/authentication/signup").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("signUpFailTest")
    @WithAnonymousUser
    public void signUpFailTest() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO(null, null, null);
        String json = objectMapper.writeValueAsString(signUpDTO);
        mockMvc.perform(post("/api/authentication/signup").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationMsg.password").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("logoutFailTest")
    @WithAnonymousUser
    public void logoutFailTest() throws Exception {
        mockMvc.perform(post("/api/authentication/logout"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("loginFailTest1")
    public void loginFailTest1() throws Exception {
        doThrow(new UsernameNotFoundException("데이터베이스에서 찾을수 없습니다.")).when(authService).login(any(LoginDTO.class));
        LoginDTO loginDTO = new LoginDTO("yhw", "1234");
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/authentication/login").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UsernameNotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("데이터베이스에서 찾을수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("loginFailTest2")
    public void loginFailTest2() throws Exception {
        LoginDTO loginDTO = new LoginDTO("yhw", "1234");
        doThrow(new BadCredentialsException("비밀번호가 일치하지 않습니다.")).when(authService).login(new LoginDTO("yhw", "1234"));
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/authentication/login").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BadCredentialsException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("비밀번호가 일치하지 않습니다."))
                .andDo(print());
    }
}