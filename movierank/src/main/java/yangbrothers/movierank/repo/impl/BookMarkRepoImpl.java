package yangbrothers.movierank.repo.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import yangbrothers.movierank.dto.BookMarkApiDTO;
import yangbrothers.movierank.dto.PageRequestDTO;
import yangbrothers.movierank.dto.QBookMarkApiDTO_BookMarkDTO;
import yangbrothers.movierank.repo.custom.BookMarkRepoCustom;

import java.util.List;

import static yangbrothers.movierank.entity.QBookMark.*;
import static yangbrothers.movierank.entity.QUser.*;

@RequiredArgsConstructor
public class BookMarkRepoImpl implements BookMarkRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BookMarkApiDTO.BookMarkDTO> bookMarkList(Long userId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();

        List<BookMarkApiDTO.BookMarkDTO> bookMarkDTOList = jpaQueryFactory
                .select(new QBookMarkApiDTO_BookMarkDTO(
                        bookMark.movieCd,
                        bookMark.movieNm,
                        bookMark.movieNmEn,
                        bookMark.prdtYear,
                        bookMark.openDt,
                        bookMark.typeNm,
                        bookMark.prdtStatNm,
                        bookMark.repNationNm,
                        bookMark.repGenreNm,
                        bookMark.director
                ))
                .from(bookMark)
                .leftJoin(bookMark.user, user)
                .where(user.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return bookMarkDTOList;
    }
}
