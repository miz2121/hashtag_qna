package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "ANSWER_ID")
    private Long id;

    @Column(length = 60, nullable = false)
    private String writer;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 1500, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Setter
    private AnswerStatus answerStatus;

    @Column(name = "AN_COMMENT_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int anCommentCount = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Setter
    private int rating = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<AnComment> anComments = new ArrayList<>();

    /**
     * addQuestionAndMember(createdQuestion, answerWriter) 해 줘야 함.
     *
     * @param content
     * @param question
     * @param member
     */
    @Builder
    public Answer(String content, Question question, Member member) {
        this.writer = member.getNickname();
        this.date = LocalDateTime.now();
        this.content = content;
        this.question = question;
        this.member = member;
        this.answerStatus = AnswerStatus.UNSELECTED;
    }

    public Long selectAnswer() {
        setAnswerStatus(AnswerStatus.SELECTED);
        return getId();
    }

    public Long giveScore(String scoreString) {
        if (Objects.equals(scoreString, "0")) {
            setRating(0);
        } else if (Objects.equals(scoreString, "1")) {
            setRating(1);
        } else if (Objects.equals(scoreString, "2")) {
            setRating(2);
        } else if (Objects.equals(scoreString, "3")) {
            setRating(3);
        } else if (Objects.equals(scoreString, "4")) {
            setRating(4);
        } else if (Objects.equals(scoreString, "5")) {
            setRating(5);
        }
        return getId();
    }

    public Long increaseAnCommentCount() {
        setAnCommentCount(getAnCommentCount() + 1);
        return getId();
    }

    public Long decreaseAnCommentCount() {
        if (getAnCommentCount() - 1 < 0) {
            setAnCommentCount(0);
        } else {
            setAnCommentCount(getAnCommentCount() - 1);
        }
        return getId();
    }

    /**
     * 연관관계 편의 메소드
     *
     * @param question
     * @param answerWriter
     */
    public Long addQuestionAndMember(Question question, Member answerWriter) {
        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
        question.increaseAnswerCount();

        if (this.member != null) {
            this.member.getAnswers().remove(this);
        }
        this.member = answerWriter;
        member.getAnswers().add(this);
        member.increaseAnswerCount();
        return getId();
    }
}
