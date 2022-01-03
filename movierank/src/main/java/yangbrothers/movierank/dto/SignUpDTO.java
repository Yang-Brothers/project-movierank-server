package yangbrothers.movierank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import yangbrothers.movierank.dto.common.CommonResult;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SignUpDTO extends CommonResult {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;


    @JsonIgnore
    public List<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");
        list.add("passwordConfirm");

        return list;
    }
}
