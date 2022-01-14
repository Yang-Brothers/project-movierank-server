package yangbrothers.movierank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "이름을 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordConfirm;


    public List<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");
        list.add("passwordConfirm");

        return list;
    }
}