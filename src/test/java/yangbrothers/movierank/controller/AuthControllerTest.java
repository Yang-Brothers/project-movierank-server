package yangbrothers.movierank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import yangbrothers.movierank.controller.exhandler.ExControllerAdvice;
import yangbrothers.movierank.controller.exhandler.ValidationControllerAdvice;
import yangbrothers.movierank.dto.request.LoginDTO;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.dto.response.TokenDTO;
import yangbrothers.movierank.entity.Member;
import yangbrothers.movierank.jwt.JwtFilter;
import yangbrothers.movierank.util.ApiUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    SpyAuthService spyAuthService = new SpyAuthService();

    @BeforeEach
    public void setUp() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(spyAuthService))
                .setControllerAdvice()
                .setControllerAdvice(new ValidationControllerAdvice(messageSource), new ExControllerAdvice())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void signUp_isCreated() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void signUp_usernameBlank() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형 욱").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_nickNameFormatNotMatch() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName(" sdf").password("aaaaa").passwordConfirm("aaaaa").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_passwordFormatNotMatch() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("aaaaa").passwordConfirm("aaaaa").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_passSignUpDTOToService() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        assertThat(spyAuthService.signUp_argument.getUsername()).isEqualTo("양형욱");
        assertThat(spyAuthService.signUp_argument.getPassword()).isEqualTo("1234Adfdsf");
        assertThat(spyAuthService.signUp_argument.getPasswordConfirm()).isEqualTo("1234Adfdsf");
    }

    @Test
    public void signUp_returnValue() throws Exception {
        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO("양형욱", "양형욱");
        ApiUtil.makeSuccessResult(signUpResponseDTO, ApiUtil.SUCCESS_CREATED);
        spyAuthService.signUp_returnValue = new ResponseEntity<>(signUpResponseDTO, HttpStatus.CREATED);

        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("양형욱"))
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.msg").isEmpty())
                .andExpect(jsonPath("$.validationMsg").isEmpty());
    }

    @Test
    public void signUp_throwsSignUpEx() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName("양형욱").password("1234Adfdsf").passwordConfirm("4321Adfdsf").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("양형욱"))
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.validationMsg").isEmpty());
    }

    @Test
    public void signUp_throwsAlreadyExistsException() throws Exception {
        spyAuthService.userRepo_findUserByNickName = Optional.of(Member.builder().memberId(1L).build());
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").nickName("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
        String json = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/v1/authentication/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.msg").value("이미 존재하는 아이디입니다."))
                .andExpect(jsonPath("$.validationMsg").isEmpty());
    }

    @Test
    public void login_isOk() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234Asdfg").build();
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void login_nickNameFormatNotMatch() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder().nickName(" yhw").password("1234Asdfg").build();
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login_passwordFormatNotMatch() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234").build();
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login_passLoginDTOToService() throws Exception {
        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234Asdfg").build();
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertThat(spyAuthService.login_argument.getNickName()).isEqualTo("yhw");
        assertThat(spyAuthService.login_argument.getPassword()).isEqualTo("1234Asdfg");
    }

    @Test
    public void login_returnValue() throws Exception {
        String jwt = "token";
        TokenDTO tokenDTO = TokenDTO.builder().nickName("yhw").token("Bearer " + jwt).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        ApiUtil.makeSuccessResult(tokenDTO, ApiUtil.SUCCESS_OK);
        spyAuthService.login_returnValue = new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);

        LoginDTO loginDTO = LoginDTO.builder().nickName("yhw").password("1234Asdfg").build();
        String json = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").isEmpty())
                .andExpect(jsonPath("$.validationMsg").isEmpty())
                .andExpect(jsonPath("$.token").value("Bearer token"))
                .andExpect(jsonPath("$.nickName").value("yhw"));
    }
}