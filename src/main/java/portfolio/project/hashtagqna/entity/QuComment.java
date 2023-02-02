package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class QuComment extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "QU_COMMENT_ID")
    private Long id;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 60, nullable = false)
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /**
     * addQuestionAndMember(createdQuestion, commentWriter) 해 줘야 함
     * @param content
     * @param question
     * @param member
     */
    @Builder
    public QuComment(String content, Question question, Member member) {
        this.content = content;
        this.date = LocalDateTime.now();
        this.writer = member.getNickname();
        this.question = question;
        this.member = member;
    }

    /**
     * 연관관계 편의 메소드
     * @param question
     * @param commentWriter
     */
    public void addQuestionAndMember(Question question, Member commentWriter){
        if(this.question != null){
            this.question.getQuComments().remove(this);
        }
        this.question = question;
        question.getQuComments().add(this);
        question.increaseQuCommentCount();

        if(this.member != null){
            this.member.getAnComments().remove(this);
        }
        this.member = commentWriter;
        member.getQuComments().add(this);
        member.increaseCommentCount();
    }
}
