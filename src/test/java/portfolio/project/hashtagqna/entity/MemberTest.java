//package portfolio.project.hashtagqna.entity;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
//class MemberTest {
//    @Autowired
//    EntityManager em;
//
//    @Test
//    public void 테이블정상생성확인() throws Exception {
//        //given
//        Member questionWriter = Member.builder()
//                .email("miz1@naver.com")
//                .pwd("1234")
//                .nickname("질문작성자")
//                .build();
//        em.persist(questionWriter);
//        Member answerWriter = Member.builder()
//                .email("miz2@naver.com")
//                .pwd("1234")
//                .nickname("답변작성자")
//                .build();
//        em.persist(answerWriter);
//        Member commentWriter = Member.builder()
//                .email("miz3@naver.com")
//                .pwd("1234")
//                .nickname("댓글작성자")
//                .build();
//        em.persist(commentWriter);
//        Hashtag createdHashtag1 = Hashtag.builder()
//                .hashtagName("건강")
//                .member(questionWriter)
//                .build();
//        em.persist(createdHashtag1);
//
//        createdHashtag1.addMember(questionWriter);
//
//        Question createdQuestion = Question.builder()
//                .title("질문제목")
//                .content("질문생성")
//                .member(questionWriter)
//                .build();
//        em.persist(createdQuestion);
//
//        createdQuestion.addMember(questionWriter);
//
//        QuestionHashtag createdQuestionHashtag1 = QuestionHashtag.builder()
//                .question(createdQuestion)
//                .hashtag(createdHashtag1)
//                .build();
//        em.persist(createdQuestionHashtag1);
//        Answer createdAnswer = Answer.builder()
//                .content("답변생성")
//                .question(createdQuestion)
//                .member(answerWriter)
//                .build();
//        em.persist(createdAnswer);
//
//        createdAnswer.addQuestionAndMember(createdQuestion, answerWriter);
//
//        AnComment createdAnComment = AnComment.builder()
//                .content("답변에댓글")
//                .answer(createdAnswer)
//                .member(commentWriter)
//                .build();
//        em.persist(createdAnComment);
//        QuComment createdQuComment = QuComment.builder()
//                .content("질문에댓글")
//                .question(createdQuestion)
//                .member(commentWriter)
//                .build();
//        em.persist(createdQuComment);
//        createdAnComment.addAnswerAndMember(createdAnswer, commentWriter);
//        createdQuComment.addQuestionAndMember(createdQuestion, commentWriter);
//        createdQuestionHashtag1.addQuestionAndHashtag(createdQuestion, createdHashtag1);
//        //when
//        //해시태그를 여러개 만든 상황이라면?
//        // 두 번째 해시태그 생성
//        Hashtag createdHashtag2 = Hashtag.builder()
//                .hashtagName("베이킹")
//                .member(questionWriter)
//                .build();
//        em.persist(createdHashtag2);
//        QuestionHashtag createdQuestionHashtag2 = QuestionHashtag.builder()
//                .question(createdQuestion)
//                .hashtag(createdHashtag2)
//                .build();
//        em.persist(createdQuestionHashtag2);
//        // 세 번째 해시태그 생성
//        Hashtag createdHashtag3 = Hashtag.builder()
//                .hashtagName("청소")
//                .member(questionWriter)
//                .build();
//        em.persist(createdHashtag3);
//        QuestionHashtag createdQuestionHashtag3 = QuestionHashtag.builder()
//                .question(createdQuestion)
//                .hashtag(createdHashtag3)
//                .build();
//        em.persist(createdQuestionHashtag3);
//        // 두 번째 해시태그 연관관계 편의 메서드 적용
//        createdHashtag2.addMember(questionWriter);
//        createdQuestionHashtag2.addQuestionAndHashtag(createdQuestion, createdHashtag2);
//        // 세 번째 해시태그 연관관계 편의 메서드 적용
//        createdHashtag3.addMember(questionWriter);
//        createdQuestionHashtag3.addQuestionAndHashtag(createdQuestion, createdHashtag3);
//
//        //then
//
//    }
//}