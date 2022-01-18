package yangbrothers.movierank.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

@Data
@NoArgsConstructor
public class SignUpResponseDTO extends CommonResult {
    private String username;

    public SignUpResponseDTO(String username) {
        this.username = username;
    }
}