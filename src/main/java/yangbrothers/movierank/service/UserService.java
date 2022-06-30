package yangbrothers.movierank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yangbrothers.movierank.entity.Member;
import yangbrothers.movierank.repo.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
        Member member = userRepo.findUserByNickName(nickName).orElse(null);

        if (member == null) {
            throw new UsernameNotFoundException("데이터베이스에서 찾을수 없습니다.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getNickName())
                .password(member.getPassword())
                .roles(member.getRole().getRole())
                .build();
    }
}