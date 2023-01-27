package portfolio.project.hashtagqna.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    @Autowired
    EntityManager em;

    @Test
    public void 테이블정상생성확인() throws Exception {
        //given
        Member questionWriter = Member.builder()
                .email("miz1@naver.com")
                .pwd("1234")
                .nickname("질문작성자")
                .build();
        Member answerWriter = Member.builder()
                .email("miz2@naver.com")
                .pwd("1234")
                .nickname("답변작성자")
                .build();
        Member commentWriter = Member.builder()
                .email("miz3@naver.com")
                .pwd("1234")
                .nickname("댓글작성자")
                .build();
        Hashtag createdHashtag = Hashtag.builder()
                .hashtagName("건강")
                .member(questionWriter)
                .build();

        createdHashtag.addMember(answerWriter);
        
        Question createdQuestion = Question.builder()
                .content("질문생성")
                .member(questionWriter)
                .build();
        QuestionHashtag createdQuestionHashtag = QuestionHashtag.builder()
                .question(createdQuestion)
                .hashtag(createdHashtag)
                .build();
        Answer createdAnswer = Answer.builder()
                .content("답변생성")
                .question(createdQuestion)
                .member(answerWriter)
                .build();
        AnComment createdAnComment = AnComment.builder()
                .content("답변에댓글")
                .answer(createdAnswer)
                .member(commentWriter)
                .build();
        QuComment createdQuComment = QuComment.builder()
                .content("질문에댓글")
                .question(createdQuestion)
                .member(commentWriter)
                .build();
        createdAnComment.addAnswerAndMember(createdAnswer, commentWriter);
        createdQuComment.addQuestionAndMember(createdQuestion, commentWriter);

        //when

        //then
    }
}