package yangbrothers.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import yangbrothers.movierank.dto.common.CommonResult;

@Data
@AllArgsConstructor
@Builder
public class TokenDTO extends CommonResult {

    private String nickName;
    private String token;
}