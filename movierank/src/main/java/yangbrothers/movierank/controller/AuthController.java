package yangbrothers.movierank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.LoginDTO;
import yangbrothers.movierank.dto.SignUpDTO;
import yangbrothers.movierank.dto.TokenDTO;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.ex.AuthenticationEx;
import yangbrothers.movierank.ex.LoginEx;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.JwtFilter;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.repo.UserRepo;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MessageSource messageSource;

    @GetMapping("/error/entrypoint")
    public void errorEntryPoint() {
        throw new AuthenticationEx("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpDTO> signup(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) {
        List<String> fields = signUpDTO.getFields();

        for (String field : fields) {
            String message = checkFiledError(bindingResult, field, null);
            if (message != null) {
                throw new SignUpEx(message);
            }
        }

        if (userRepo.findUserByUsername(signUpDTO.getUsername()).orElse(null) != null) {
            throw new SignUpEx("이미 존재하는 아이디입니다.");
        }
        if (!signUpDTO.getPassword().equals(signUpDTO.getPasswordConfirm())) {
            throw new SignUpEx("비밀번호가 일치하지 않습니다.", signUpDTO);
        }

        User user = new User(signUpDTO, passwordEncoder);
        ApiUtils.makeSuccessResult(signUpDTO, ApiUtils.SIGNUP_SUCCESS);
        return new ResponseEntity<>(signUpDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        List<String> fields = loginDTO.getFields();

        for (String field : fields) {
            String message = checkFiledError(bindingResult, field, null);
            if (message != null) {
                throw new LoginEx(message);
            }
        }

        Authentication authentication = getAuthentication(loginDTO);
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        TokenDTO tokenDTO = new TokenDTO(jwt);
        ApiUtils.makeSuccessResult(tokenDTO, ApiUtils.AUTHORIZATION_SUCCESS);

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    private String checkFiledError(BindingResult bindingResult, String field, Object[] args) {
        if (bindingResult.hasFieldErrors(field)) {
            String[] codes = bindingResult.getFieldError(field).getCodes();
            for (String code : codes) {
                try {
                    String message = messageSource.getMessage(code, args, null);
                    return message;
                } catch (NoSuchMessageException e) {
                    continue;
                }
            }
        }
        return null;
    }

    private Authentication getAuthentication(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}