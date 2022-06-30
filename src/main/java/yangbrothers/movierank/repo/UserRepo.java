package yangbrothers.movierank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangbrothers.movierank.entity.Member;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Member, Long> {

    Optional<Member> findUserByUsername(String username);

    Optional<Member> findUserByNickName(String nickName);
}