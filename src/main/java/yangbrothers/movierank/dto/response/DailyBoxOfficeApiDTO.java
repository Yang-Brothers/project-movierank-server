package yangbrothers.movierank.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class DailyBoxOfficeApiDTO extends CommonResult {

    private HashMap<String, Object> data = new HashMap<>();

    @Data
    @NoArgsConstructor
    public static class DailyBoxOfficeDTO {
        private String rnum;
        private String rank;
        private String rankInten;
        private String rankOldAndNew;
        private String movieCd;
        private String movieNm;
        private String openDt;
        private String salesAmt;
        private String salesShare;
        private String salesInten;
        private String salesChange;
        private String salesAcc;
        private String audiCnt;
        private String audiInten;
        private String audiChange;
        private String audiAcc;
        private String scrnCnt;
        private String showCnt;
    }
}
