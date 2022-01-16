package yangbrothers.movierank.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity implements Persistable<Long> {

    @Id
    @Column(name = "movie_id")
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

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    public void createMovie(StringJoiner directorJoiner, StringJoiner actorJoiner, String plotJoiner) {
        this.directors = directorJoiner.toString();
        this.actors = actorJoiner.toString();
        this.plot = plotJoiner;
    }

    public void setId(long id) {
        this.id = id;
    }
}