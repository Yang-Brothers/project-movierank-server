package yangbrothers.movierank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @JsonIgnore
    public List<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");

        return list;
    }
}