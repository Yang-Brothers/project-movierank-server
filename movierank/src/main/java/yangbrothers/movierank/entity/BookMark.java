package yangbrothers.movierank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yangbrothers.movierank.dto.BookMarkApiDTO;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "bookmark_id")
    private Long id;

    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String openDt;
    private String typeNm;
    private String prdtStatNm;
    private String repNationNm;
    private String repGenreNm;
    private String director;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public BookMark(BookMarkApiDTO.BookMarkDTO bookMarkDTO, User user) {
        this.movieCd = bookMarkDTO.getMovieCd();
        this.movieNm = bookMarkDTO.getMovieNm();
        this.movieNmEn = bookMarkDTO.getMovieNmEn();
        this.prdtYear = bookMarkDTO.getPrdtYear();
        this.openDt = bookMarkDTO.getOpenDt();
        this.typeNm = bookMarkDTO.getTypeNm();
        this.prdtStatNm = bookMarkDTO.getPrdtStatNm();
        this.repNationNm = bookMarkDTO.getRepNationNm();
        this.repGenreNm = bookMarkDTO.getRepGenreNm();
        this.user = user;
        user.getBookMarkList().add(this);
    }
}
