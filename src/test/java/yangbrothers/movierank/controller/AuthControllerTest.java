package yangbrothers.movierank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import yangbrothers.movierank.controller.exhandler.ExControllerAdvice;
import yangbrothers.movierank.controller.exhandler.ValidationControllerAdvice;
import yangbrothers.movierank.dto.request.SignUpDTO;
import yangbrothers.movierank.dto.response.SignUpResponseDTO;
import yangbrothers.movierank.entity.User;
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
                .setControllerAdvice(new ValidationControllerAdvice(messageSource))
                .setControllerAdvice(new ExControllerAdvice())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void signUp_isCreated() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
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
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
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
        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO("양형욱");
        ApiUtil.makeSuccessResult(signUpResponseDTO, ApiUtil.SUCCESS_CREATED);
        spyAuthService.signUp_returnValue = new ResponseEntity<>(signUpResponseDTO, HttpStatus.CREATED);

        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
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
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234Adfdsf").passwordConfirm("4321Adfdsf").build();
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
        spyAuthService.userRepo_findUserByUsername = Optional.of(User.builder().userId(1L).build());
        SignUpDTO signUpDTO = SignUpDTO.builder().username("양형욱").password("1234Adfdsf").passwordConfirm("1234Adfdsf").build();
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
}