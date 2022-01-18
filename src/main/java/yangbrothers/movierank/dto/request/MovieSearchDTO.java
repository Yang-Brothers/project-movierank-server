package yangbrothers.movierank.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MovieSearchDTO {

    @ApiModelProperty(value = "시작")
    private Integer start = 0;

    @ApiModelProperty(value = "크기")
    @Max(value = 100, message = "한번에 100개 이상을 조회할 수 없습니다.")
    private Integer len = 10;

    @ApiModelProperty(value = "영화 검색 이름")
    @NotBlank(message = "영화 이름을 입력해주세요.")
    private String movieNm;
}