package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AnComment {
    @Id
    @GeneratedValue
    @Column(name = "AN_COMMENT_ID")
    private Long id;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 60, nullable = false)
    private String writer;

    @ManyToOne
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /**
     * addAnswerAndMember(createdAnswer, commentWriter) 해 줘야 함.
     * @param content
     * @param answer
     * @param member
     */
    @Builder
    public AnComment(String content, Answer answer, Member member) {
        this.content = content;
        this.date = LocalDateTime.now();
        this.writer = member.getNickname();
        this.answer = answer;
        this.member = member;
    }

    /**
     * 연관관계 편의 메소드
     * @param answer
     * @param commentWriter
     */
    public void addAnswerAndMember(Answer answer, Member commentWriter){
        if(this.answer != null){
            this.answer.getAnComments().remove(this);
        }
        this.answer = answer;
        answer.getAnComments().add(this);
        answer.increaseAnCommentCount();

        if(this.member != null){
            this.member.getAnComments().remove(this);
        }
        this.member = commentWriter;
        member.getAnComments().add(this);
        member.increaseCommentCount();
    }
}
