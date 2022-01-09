package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangbrothers.movierank.entity.BookMark;
import yangbrothers.movierank.repo.custom.BookMarkRepoCustom;

public interface BookMarkRepo extends JpaRepository<BookMark, Long>, BookMarkRepoCustom {
}
