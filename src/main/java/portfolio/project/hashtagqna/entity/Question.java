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
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "QUESTION_ID", updatable = false, nullable = false)
    private Long id;

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

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '0'", length = 1)
    @Setter
    private String closed;

    @Column(name = "WAITING_SELECTION", nullable = false, columnDefinition = "CHAR(1) DEFAULT '0'", length = 1)
    @Setter
    private String waitingSelection;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private List<QuestionHashtag> questionHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private List<QuComment> quComments = new ArrayList<>();

    @Builder
    public Question(String content, Member member) {
        this.content = content;
        this.date = LocalDateTime.now();
        this.writer = member.getNickname();
        this.member = member;
    }

    public Long increaseQuCommentCount(){
        setQuCommentCount(getQuCommentCount() + 1);
        member.setCommentCount(member.getCommentCount() + 1);
        return getId();
    }

    public Long increaseAnswerCount(){
        setAnswerCount(getAnswerCount() + 1);
        member.setAnswerCount(member.getAnswerCount() + 1);
        return getId();
    }
}
