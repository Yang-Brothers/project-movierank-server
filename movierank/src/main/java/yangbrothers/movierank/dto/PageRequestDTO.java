package yangbrothers.movierank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageRequestDTO {
    @ApiModelProperty(value = "시작")
    private int start = 0;

    @ApiModelProperty(value = "크기")
    private int len = 10;

    @ApiModelProperty(value = "영화 검색 이름")
    private String movieNm = "";
}