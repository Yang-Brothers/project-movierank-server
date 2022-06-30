package yangbrothers.movierank.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SequenceGenerator(
        name = "BOOKMARK_SEQ_GENERATOR",
        sequenceName = "BOOKMARK_SEQ",
        allocationSize = 1
)
public class BookMark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOKMARK_SEQ_GENERATOR")
    @Column(name = "bookmark_id")
    private Long id;

    private String DOCID;
    private String movieSeq;
    private String title;
    private String titleEng;
    private String prodYear;
    @Lob
    private String directors;
    @Lob
    private String actors;
    private String nation;
    @Lob
    private String plot;
    private String runtime;
    private String rating;
    private String genre;
    private String type;
    private String repRlsDate;
    @Lob
    private String posters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void createMember(Member member) {
        this.member = member;
        member.getBookMarkList().add(this);
    }
}
