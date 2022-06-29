package yangbrothers.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import yangbrothers.movierank.dto.common.CommonResult;

@Data
@AllArgsConstructor
public class TokenDTO extends CommonResult {

    private String username;
    private String token;
}