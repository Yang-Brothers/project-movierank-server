package yangbrothers.movierank.dto.common;

import lombok.Data;

@Data
public class CommonResult {
    private String result;
    private String code;
    private String msg;
    private String username;
}