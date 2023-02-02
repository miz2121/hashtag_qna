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
public class Answer extends BaseEntity{
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

    public Long selectAnswer(){
        setAnswerStatus(AnswerStatus.SELECTED);
        return getId();
    }

    public Long giveScore(int score){
        setRating(score);
        return getId();
    }

    public Long increaseAnCommentCount(){
        setAnCommentCount(getAnCommentCount() + 1);
        return getId();
    }

    /**
     * 연관관계 편의 메소드
     * @param question
     * @param answerWriter
     */
    public void addQuestionAndMember(Question question, Member answerWriter){
        if(this.question != null){
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
        question.increaseAnswerCount();

        if(this.member != null){
            this.member.getAnswers().remove(this);
        }
        this.member = answerWriter;
        member.getAnswers().add(this);
        member.increaseAnswerCount();
    }
}
