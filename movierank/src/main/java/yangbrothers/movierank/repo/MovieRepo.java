package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.repo.custom.MovieRepoCustom;

import java.util.Optional;

public interface MovieRepo extends JpaRepository<Movie, Long>, MovieRepoCustom {

    public Optional<Movie> findMovieByIndex(int index);
}
