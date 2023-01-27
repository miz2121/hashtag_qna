package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class QuestionHashtag {
    @Id
    @GeneratedValue
    @Column(name = "QUESTION_ID", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "HASHTAG_ID")
    private Hashtag hashtag;

    @Builder
    public QuestionHashtag(Question question, Hashtag hashtag) {
        this.question = question;
        this.hashtag = hashtag;
    }

    /**
     * 연관관계 편의 메소드
     * @param question
     */
    public void addQuestion(Question question){
        if(this.question != null){
            this.question.getQuestionHashtags().remove(this);
        }
        this.question = question;
        question.getQuestionHashtags().add(this);
    }

    /**
     * 연관관계 편의 메소드
     * @param question
     * @param hashtag
     */
    public void addQuestionAndHashtag(Question question, Hashtag hashtag){
        if(this.question != null){
            this.question.getQuestionHashtags().remove(this);
        }
        this.question = question;
        question.getQuestionHashtags().add(this);

        if(this.hashtag != null){
            this.hashtag.getQuestionHashtags().remove(this);
        }
        this.hashtag = hashtag;
        hashtag.getQuestionHashtags().add(this);
    }
}
