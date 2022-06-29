package yangbrothers.movierank.repo.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import yangbrothers.movierank.dto.request.MovieSearchDTO;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.MovieApiDTO;
import yangbrothers.movierank.dto.response.QMovieApiDTO_MovieDTO;
import yangbrothers.movierank.repo.custom.MovieRepoCustom;

import java.util.List;

import static yangbrothers.movierank.entity.QMovie.movie;

@RequiredArgsConstructor
public class MovieRepoImpl implements MovieRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MovieApiDTO.MovieDTO> movieList(PageRequestDTO pageRequestDTO) {
        List<MovieApiDTO.MovieDTO> movieList = jpaQueryFactory
                .select(new QMovieApiDTO_MovieDTO(
                        movie.DOCID,
                        movie.movieSeq,
                        movie.title,
                        movie.titleEng,
                        movie.prodYear,
                        movie.directors,
                        movie.actors,
                        movie.nation,
                        movie.plot,
                        movie.runtime,
                        movie.rating,
                        movie.genre,
                        movie.type,
                        movie.repRlsDate,
                        movie.posters
                ))
                .from(movie)
                .offset(pageRequestDTO.getStart())
                .limit(pageRequestDTO.getLen())
                .fetch();

        return movieList;
    }

    @Override
    public List<MovieApiDTO.MovieDTO> movieSearch(MovieSearchDTO movieSearchDTO) {
        List<MovieApiDTO.MovieDTO> movieList = jpaQueryFactory
                .select(new QMovieApiDTO_MovieDTO(
                        movie.DOCID,
                        movie.movieSeq,
                        movie.title,
                        movie.titleEng,
                        movie.prodYear,
                        movie.directors,
                        movie.actors,
                        movie.nation,
                        movie.plot,
                        movie.runtime,
                        movie.rating,
                        movie.genre,
                        movie.type,
                        movie.repRlsDate,
                        movie.posters
                ))
                .from(movie)
                .where(movie.title.contains(movieSearchDTO.getMovieNm()))
                .offset(movieSearchDTO.getStart())
                .limit(movieSearchDTO.getLen())
                .fetch();

        return movieList;
    }
}
