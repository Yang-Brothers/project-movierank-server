package yangbrothers.movierank.dto;

import lombok.Data;
import yangbrothers.movierank.dto.common.CommonResult;

import java.util.List;

@Data
public class MovieApiDTO extends CommonResult {

    List<MovieDTO> movieDTOList;

    @Data
    static class MovieDTO {
        private String movieCd;
        private String movieNm;
        private String movieNmEn;
        private String prdtYear;
        private String openDt;
        private String typeNm;
        private String prdtStatNm;
        private String nationAlt;
        private String genreAlt;
        private String repNationNm;
        private String repGenreNm;
        private String directors;
    }
}