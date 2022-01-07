package yangbrothers.movierank.repo.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
        List<MovieApiDTO.MovieDTO> movieList = jpaQueryFactory
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
                .offset(pageRequestDTO.getStart())
                .limit(pageRequestDTO.getLen())
                .fetch();

        return movieList;
    }

    @Override
    public List<MovieApiDTO.MovieDTO> movieSearch(PageRequestDTO pageRequestDTO) {
        List<MovieApiDTO.MovieDTO> movieList = jpaQueryFactory
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
                .where(movie.movieNm.contains(pageRequestDTO.getMovieNm()))
                .offset(pageRequestDTO.getStart())
                .limit(pageRequestDTO.getLen())
                .fetch();

        return movieList;
    }
}
