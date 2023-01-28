package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(length = 960, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String pwd;

    @Column(length = 60, nullable = false)
    private String nickname;

    @Column(name = "QUESTION_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int questionCount = 0;

    @Column(name = "ANSWER_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int answerCount = 0;

    @Column(name = "HASHTAG_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int hashtagCount = 0;

    @Column(name = "COMMENT_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int commentCount = 0;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Hashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<QuComment> quComments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<AnComment> anComments = new ArrayList<>();

    @Builder
    public Member(String email, String pwd, String nickname) {
        this.email = email;
        this.pwd = pwd;
        this.nickname = nickname;
    }

    public Long increaseQuestionCount(){
        setQuestionCount(getQuestionCount() + 1);
        return getId();
    }

    public Long increaseAnswerCount(){
        setAnswerCount(getAnswerCount() + 1);
        return getId();
    }

    public Long increaseHashTagCount(){
        setHashtagCount(getHashtagCount() + 1);
        return getId();
    }

    public Long increaseCommentCount(){
        setCommentCount(getCommentCount() + 1);
        return getId();
    }
}
