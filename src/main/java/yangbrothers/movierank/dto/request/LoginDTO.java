package yangbrothers.movierank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "[^\\s]+", message = "닉네임 형식을 지켜주세요.")
    private String nickName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[A-Z]).{8,15}", message = "비밀번호 형식을 지켜주세요.")
    private String password;
}