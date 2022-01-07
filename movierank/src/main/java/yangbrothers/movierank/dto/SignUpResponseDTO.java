package yangbrothers.movierank.dto;

import lombok.Data;
import yangbrothers.movierank.dto.common.CommonResult;

@Data
public class SignUpResponseDTO extends CommonResult {
    private String username;

    public SignUpResponseDTO(String username) {
        this.username = username;
    }
}
