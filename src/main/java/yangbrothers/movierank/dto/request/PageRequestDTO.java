package yangbrothers.movierank.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
public class PageRequestDTO {

    @ApiModelProperty(value = "시작")
    private Integer start = 0;

    @ApiModelProperty(value = "크기: [1, 100]")
    @Max(value = 100, message = "한번에 100개 이상을 조회할 수 없습니다.")
    private Integer len = 10;
}