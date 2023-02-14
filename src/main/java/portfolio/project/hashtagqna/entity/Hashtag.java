package portfolio.project.hashtagqna.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Hashtag extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "HASHTAG_ID")
    private Long id;

    @Column(length = 60, nullable = false)
    private String hashtagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<QuestionHashtag> questionHashtags = new ArrayList<>();

    /**
     * addMember(questionWriter) 해줘야 함
     * @param hashtagName
     * @param member
     */
    @Builder
    public Hashtag(String hashtagName, Member member) {
        this.hashtagName = hashtagName;
        this.member = member;
    }

    /**
     * 연관관계 편의 메소드
     * @param member
     */
    public Long addMember(Member member){
        if(this.member != null){
            this.member.getHashtags().remove(this);
        }
        this.member = member;
        member.getHashtags().add(this);
        member.increaseHashTagCount();
        return getId();
    }
}
