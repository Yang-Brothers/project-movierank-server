package yangbrothers.movierank.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public List<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");

        return list;
    }
}