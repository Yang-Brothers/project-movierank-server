package yangbrothers.movierank.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.common.CommonResult;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class MovieApiDTO extends CommonResult {

    private HashMap<String, Object> data = new HashMap<>();

    @Data
    @NoArgsConstructor
    public static class MovieDTO {
        private String DOCID;
        private String movieSeq;
        private String title;
        private String titleEng;
        private String prodYear;
        private String directors;
        private String actors;
        private String nation;
        private String plot;
        private String runtime;
        private String rating;
        private String genre;
        private String type;
        private String repRlsDate;
        private String posters;

        @QueryProjection
        public MovieDTO(String DOCID, String movieSeq, String title, String titleEng, String prodYear, String directors, String actors, String nation, String plot, String runtime, String rating, String genre, String type, String repRlsDate, String posters) {
            this.DOCID = DOCID;
            this.movieSeq = movieSeq;
            this.title = title;
            this.titleEng = titleEng;
            this.prodYear = prodYear;
            this.directors = directors;
            this.actors = actors;
            this.nation = nation;
            this.plot = plot;
            this.runtime = runtime;
            this.rating = rating;
            this.genre = genre;
            this.type = type;
            this.repRlsDate = repRlsDate;
            this.posters = posters;
        }
    }
}