package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangbrothers.movierank.entity.Movie;

public interface MovieRepo extends JpaRepository<Movie, Long> {
}
