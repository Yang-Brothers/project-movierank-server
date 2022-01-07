package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.LoginDTO;
import yangbrothers.movierank.dto.SignUpDTO;
import yangbrothers.movierank.dto.SignUpResponseDTO;
import yangbrothers.movierank.dto.TokenDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.entity.User;
import yangbrothers.movierank.ex.AuthenticationEx;
import yangbrothers.movierank.ex.LoginEx;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.JwtFilter;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.repo.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Api(tags = {"회원가입, 로그인, 인증 오류를 제공하는 Controller"})
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MessageSource messageSource;
    private final StringRedisTemplate redisTemplate;


    @PostMapping("/signup")
    @ApiImplicitParam(name = "signUpDTO", required = true, dataTypeClass = SignUpDTO.class, paramType = "body")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원 가입 성공"),
            @ApiResponse(code = 400, message = "회원 가입 실패")
    })
    @ApiOperation(value = "회원가입을 진행하는 메소드")
    public ResponseEntity<SignUpResponseDTO> signup(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) {
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

        userRepo.save(new User(signUpDTO, passwordEncoder));

        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(signUpDTO.getUsername());

        ApiUtils.makeSuccessResult(signUpResponseDTO, ApiUtils.SUCCESS_CREATED);
        return new ResponseEntity<>(signUpResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiImplicitParam(name = "loginDTO", required = true, dataTypeClass = LoginDTO.class, paramType = "body")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패")
    })
    @ApiOperation(value = "로그인을 진행하는 메소드")
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
        TokenDTO tokenDTO = new TokenDTO(loginDTO.getUsername(), jwt);
        ApiUtils.makeSuccessResult(tokenDTO, ApiUtils.SUCCESS_CREATED);

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @ApiResponse(code = 200, message = "로그아웃 성공")
    @ApiOperation(value = "로그아웃을 진행하는 메소드")
    public ResponseEntity<CommonResult> logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        Date expirationDate = tokenProvider.getExpirationDate(token);
        redisTemplate.opsForValue().set(token, "logout", expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        CommonResult successResult = ApiUtils.getSuccessResult(ApiUtils.SUCCESS_OK);

        return new ResponseEntity<>(successResult, HttpStatus.OK);
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