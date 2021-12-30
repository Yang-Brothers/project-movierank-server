package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangbrothers.movierank.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}