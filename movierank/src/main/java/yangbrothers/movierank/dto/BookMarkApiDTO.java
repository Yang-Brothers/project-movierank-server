package yangbrothers.movierank.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

import java.util.HashMap;
import java.util.List;

@Data
public class BookMarkApiDTO extends CommonResult {

    private HashMap<String, Object> data = new HashMap<>();

    @Data
    @NoArgsConstructor
    public static class BookMarkDTO {
        private Long bookMarkId;
        private String movieCd;
        private String movieNm;
        private String movieNmEn;
        private String prdtYear;
        private String openDt;
        private String typeNm;
        private String prdtStatNm;
        private String repNationNm;
        private String repGenreNm;
        private String director;

        @QueryProjection
        public BookMarkDTO(Long bookMarkId, String movieCd, String movieNm, String movieNmEn, String prdtYear, String openDt, String typeNm, String prdtStatNm, String repNationNm, String repGenreNm, String director) {
            this.bookMarkId = bookMarkId;
            this.movieCd = movieCd;
            this.movieNm = movieNm;
            this.movieNmEn = movieNmEn;
            this.prdtYear = prdtYear;
            this.openDt = openDt;
            this.typeNm = typeNm;
            this.prdtStatNm = prdtStatNm;
            this.repNationNm = repNationNm;
            this.repGenreNm = repGenreNm;
            this.director = director;
        }
    }
}