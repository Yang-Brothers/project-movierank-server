package yangbrothers.movierank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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