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
public class Question extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "QUESTION_ID")
    private Long id;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 60, nullable = false)
    private String writer;

    @Column(name = "QU_COMMENT_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int quCommentCount = 0;

    @Column(name = "ANSWER_COUNT", nullable = false)
    @ColumnDefault("0")
    @Setter
    private int answerCount = 0;

    @Enumerated(EnumType.STRING)
    @Setter
    private QuestionStatus questionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<QuestionHashtag> questionHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<QuComment> quComments = new ArrayList<>();

    /**
     * addMember(questionWriter) 해 줘야 함.
     * @param content
     * @param member
     */
    @Builder
    public Question(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
        this.writer = member.getNickname();
        this.member = member;
        this.questionStatus = QuestionStatus.OPEN;
    }

    public Long closeQuestion(){
        setQuestionStatus(QuestionStatus.CLOSED);
        return getId();
    }

    public Long increaseQuCommentCount(){
        setQuCommentCount(getQuCommentCount() + 1);
        return getId();
    }

    public Long increaseAnswerCount(){
        setAnswerCount(getAnswerCount() + 1);
        return getId();
    }

    public void addMember(Member questionWriter){
        if(this.member != null){
            this.member.getQuestions().remove(this);
        }
        this.member = questionWriter;
        member.getQuestions().add(this);
        member.increaseQuestionCount();
    }
}
