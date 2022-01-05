package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yangbrothers.movierank.entity.BookMark;
import yangbrothers.movierank.repo.custom.BookMarkRepoCustom;

public interface BookMarkRepo extends JpaRepository<BookMark, Long>, BookMarkRepoCustom {
}
