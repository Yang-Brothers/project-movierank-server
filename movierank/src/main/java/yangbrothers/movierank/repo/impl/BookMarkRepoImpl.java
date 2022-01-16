package yangbrothers.movierank.repo.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import yangbrothers.movierank.dto.request.MovieSearchDTO;
import yangbrothers.movierank.dto.request.PageRequestDTO;
import yangbrothers.movierank.dto.response.BookMarkApiDTO;
import yangbrothers.movierank.dto.response.QBookMarkApiDTO_BookMarkDTO;
import yangbrothers.movierank.repo.custom.BookMarkRepoCustom;

import java.util.List;

import static yangbrothers.movierank.entity.QBookMark.bookMark;
import static yangbrothers.movierank.entity.QUser.user;

@RequiredArgsConstructor
public class BookMarkRepoImpl implements BookMarkRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BookMarkApiDTO.BookMarkDTO> bookMarkList(Long userId, PageRequestDTO pageRequestDTO) {

        List<BookMarkApiDTO.BookMarkDTO> bookMarkDTOList = jpaQueryFactory
                .select(new QBookMarkApiDTO_BookMarkDTO(
                        bookMark.DOCID,
                        bookMark.movieSeq,
                        bookMark.title,
                        bookMark.titleEng,
                        bookMark.prodYear,
                        bookMark.directors,
                        bookMark.actors,
                        bookMark.nation,
                        bookMark.plot,
                        bookMark.runtime,
                        bookMark.rating,
                        bookMark.genre,
                        bookMark.type,
                        bookMark.repRlsDate,
                        bookMark.posters
                ))
                .from(bookMark)
                .leftJoin(bookMark.user, user)
                .where(user.userId.eq(userId))
                .offset(pageRequestDTO.getStart())
                .limit(pageRequestDTO.getLen())
                .fetch();

        return bookMarkDTOList;
    }
}