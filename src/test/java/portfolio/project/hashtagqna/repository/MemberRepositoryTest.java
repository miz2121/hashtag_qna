//package portfolio.project.hashtagqna.repository;
//
//import jakarta.persistence.EntityManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import portfolio.project.hashtagqna.entity.*;
//import portfolio.project.hashtagqna.logger.PrintLog;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
//class MemberRepositoryTest {
//
//    @Autowired
//    EntityManager em;
//    @Autowired
//    MemberRepository memberRepository;
//
//    private final PrintLog printLog = new PrintLog();
//
//    /**
//     * @param em
//     * @return questionWriter.getId()
//     */
//    static long createData(EntityManager em){
//        Member questionWriter = Member.builder()
//                .email("rmiz1@naver.com")
//                .pwd("r1234")
//                .nickname("r질문작성자")
//                .build();
//        em.persist(questionWriter);
//        Member answerWriter = Member.builder()
//                .email("rmiz2@naver.com")
//                .pwd("r1234")
//                .nickname("r답변작성자")
//                .build();
//        em.persist(answerWriter);
//        Member commentWriter = Member.builder()
//                .email("rmiz3@naver.com")
//                .pwd("r1234")
//                .nickname("r댓글작성자")
//                .build();
//        em.persist(commentWriter);
//        Hashtag createdHashtag1 = Hashtag.builder()
//                .hashtagName("r건강")
//                .member(questionWriter)
//                .build();
//        em.persist(createdHashtag1);
//
//        createdHashtag1.addMember(questionWriter);
//
//        Question createdQuestion = Question.builder()
//                .title("r질문제목")
//                .content("r질문생성")
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
//                .content("r답변생성")
//                .question(createdQuestion)
//                .member(answerWriter)
//                .build();
//        em.persist(createdAnswer);
//
//        createdAnswer.addQuestionAndMember(createdQuestion, answerWriter);
//
//        AnComment createdAnComment = AnComment.builder()
//                .content("r답변에댓글")
//                .answer(createdAnswer)
//                .member(commentWriter)
//                .build();
//        em.persist(createdAnComment);
//        QuComment createdQuComment = QuComment.builder()
//                .content("r질문에댓글")
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
//                .hashtagName("r베이킹")
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
//                .hashtagName("r청소")
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
//        return questionWriter.getId();
//    }
//
////    @Test
////    public void 멤버를inactive로만들어보자() throws Exception {
////        //given
////        long questionWriterId = createData(em);
////        //when
////        printLog.printInfoLog("여기서부터 멤버를 INACTIVE로 만듦");
////        long inactivatedId = memberRepository.makeInactiveMember(questionWriterId);
////        printLog.printInfoLog("INACTIVE가 된 멤버를 찾아 옴");
////        Member inactivatedMember = memberRepository.findMemberById(inactivatedId);
////        //then
////        printLog.printInfoLog("INACTIVE가 된 멤버의 상태는 INACTIVE여야 한다.");
////        assertThat(inactivatedMember.getStatus()).isEqualTo(MemberStatus.INACTIVE);
////        printLog.printInfoLog("INACTIVE가 된 질문작성자와 관련된 모든 질문들은 탈퇴원회원이라는 정보를 담아야 한다.");
////        List<Question> questions = inactivatedMember.getQuestions();
////        for (Question question : questions) {
////            assertThat(question.getContent()).isEqualTo(inactivatedMember.getInactiveMessage());
////            assertThat(question.getTitle()).isEqualTo(inactivatedMember.getInactiveMessage());
////            assertThat(question.getWriter()).isEqualTo(inactivatedMember.getInactiveMessage());
////        }
////    }
//}