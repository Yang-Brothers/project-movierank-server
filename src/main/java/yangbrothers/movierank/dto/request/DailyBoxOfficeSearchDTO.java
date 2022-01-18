package yangbrothers.movierank.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DailyBoxOfficeSearchDTO {

    @ApiModelProperty(value = "조회하고자 하는 날짜(yyyy-MM-dd): 오늘 날짜 이전으로 입력해주세요.")
    @Past(message = "오늘 날짜 전으로 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty(value = "크기", allowableValues = "1, 10")
    @Max(value = 10, message = "한번에 10개 이상을 조회할 수 없습니다.")
    private Integer len = 10;
}