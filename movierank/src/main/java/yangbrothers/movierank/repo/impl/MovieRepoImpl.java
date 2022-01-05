package yangbrothers.movierank.repo.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import yangbrothers.movierank.dto.MovieApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.dto.QMovieApiDTO_MovieDTO;
import yangbrothers.movierank.repo.custom.MovieRepoCustom;

import java.util.List;

import static yangbrothers.movierank.entity.QMovie.movie;

@RequiredArgsConstructor
public class MovieRepoImpl implements MovieRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MovieApiDTO.MovieDTO> movieList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        List<MovieApiDTO.MovieDTO> movieDTOList = jpaQueryFactory
                .select(new QMovieApiDTO_MovieDTO(
                        movie.movieCd,
                        movie.movieNm,
                        movie.movieNmEn,
                        movie.prdtYear,
                        movie.openDt,
                        movie.typeNm,
                        movie.prdtStatNm,
                        movie.nationAlt,
                        movie.genreAlt,
                        movie.repNationNm,
                        movie.repGenreNm,
                        movie.directors
                ))
                .from(movie)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return movieDTOList;
    }
}
