package yangbrothers.movierank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;
}