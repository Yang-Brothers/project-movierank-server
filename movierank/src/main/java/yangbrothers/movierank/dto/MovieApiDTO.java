package yangbrothers.movierank.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.entity.Movie;

import java.util.List;

@Data
public class MovieApiDTO extends CommonResult {

    private int page;
    private List<MovieDTO> movieDTOList;

    @Data
    public static class MovieDTO {
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

        @QueryProjection
        public MovieDTO(String movieCd, String movieNm, String movieNmEn, String prdtYear, String openDt, String typeNm, String prdtStatNm, String nationAlt, String genreAlt, String repNationNm, String repGenreNm, String directors) {
            this.movieCd = movieCd;
            this.movieNm = movieNm;
            this.movieNmEn = movieNmEn;
            this.prdtYear = prdtYear;
            this.openDt = openDt;
            this.typeNm = typeNm;
            this.prdtStatNm = prdtStatNm;
            this.nationAlt = nationAlt;
            this.genreAlt = genreAlt;
            this.repNationNm = repNationNm;
            this.repGenreNm = repGenreNm;
            this.directors = directors;
        }

        public MovieDTO(Movie movie) {
            this.movieCd = movie.getDirectors();
            this.movieNm = movie.getMovieNm();
            this.movieNmEn = movie.getMovieNmEn();
            this.prdtYear = movie.getPrdtYear();
            this.openDt = movie.getOpenDt();
            this.typeNm = movie.getTypeNm();
            this.prdtStatNm = movie.getPrdtStatNm();
            this.nationAlt = movie.getNationAlt();
            this.genreAlt = movie.getGenreAlt();
            this.repNationNm = movie.getRepNationNm();
            this.repGenreNm = movie.getRepGenreNm();
            this.directors = movie.getDirectors();
        }
    }
}