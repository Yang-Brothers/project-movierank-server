package yangbrothers.movierank.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangbrothers.movierank.dto.LoginDTO;
import yangbrothers.movierank.dto.SignUpDTO;
import yangbrothers.movierank.dto.SignUpResponseDTO;
import yangbrothers.movierank.dto.TokenDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.ex.LoginEx;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.jwt.JwtFilter;
import yangbrothers.movierank.service.AuthService;
import yangbrothers.movierank.util.ApiUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Api(tags = {"회원가입, 로그인, 인증 오류를 제공하는 Controller"})
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

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

        return new ResponseEntity<>(authService.signUp(signUpDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiImplicitParam(name = "loginDTO", required = true, dataTypeClass = LoginDTO.class, paramType = "body")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패")
    })
    @ApiOperation(value = "로그인을 진행하는 메소드")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {

        List<String> fields = loginDTO.getFields();

        for (String field : fields) {
            String message = checkFiledError(bindingResult, field, null);
            if (message != null) {
                throw new LoginEx(message);
            }
        }

        String jwt = authService.login(loginDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        TokenDTO tokenDTO = new TokenDTO(loginDTO.getUsername(), jwt);
        ApiUtil.makeSuccessResult(tokenDTO, ApiUtil.SUCCESS_CREATED);

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    @ApiResponse(code = 200, message = "로그아웃 성공")
    @ApiOperation(value = "로그아웃을 진행하는 메소드")
    public ResponseEntity<CommonResult> logout(HttpServletRequest request) {

        return new ResponseEntity<>(authService.logout(request), HttpStatus.OK);
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
}