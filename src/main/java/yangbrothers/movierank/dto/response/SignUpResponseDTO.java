package yangbrothers.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDTO extends CommonResult {

    private String username;
    private String nickName;
}