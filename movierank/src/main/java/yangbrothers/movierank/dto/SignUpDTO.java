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

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;


    public List<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");
        list.add("passwordConfirm");

        return list;
    }
}