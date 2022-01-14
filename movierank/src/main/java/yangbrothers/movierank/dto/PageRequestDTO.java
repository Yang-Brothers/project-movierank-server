package yangbrothers.movierank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
public class PageRequestDTO {

    @ApiModelProperty(value = "시작")
    private Integer start = 0;

    @ApiModelProperty(value = "크기")
    @Max(value = 100, message = "한번에 100개 이상을 조회할 수 없습니다.")
    private Integer len = 0;

    @ApiModelProperty(value = "영화 검색 이름")
    private String movieNm;
}