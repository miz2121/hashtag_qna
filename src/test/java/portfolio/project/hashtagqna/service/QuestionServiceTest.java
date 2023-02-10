package portfolio.project.hashtagqna.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.logger.PrintLog;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionServiceTest {
    @Autowired
    EntityManager em;
    private final PrintLog printLog = new PrintLog();
    @Autowired
    QuestionService questionService;
    @Autowired
    MemberService memberService;
    @Autowired
    AnswerService answerService;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void 질문글5개보여줘보자() throws Exception {
        //given
        createSixQuestions(createMember());
        //when
        List<QuestionListDto> questionListDtos = questionService.viewFiveQuestions();
        //then
        for (QuestionListDto questionListDto : questionListDtos) {
            printLog.printInfoLog("questionListDto: ");
            System.out.println("questionListDto = " + questionListDto);
        }
    }

    private Member createMember(){
        Member member = Member.builder()
                .email("miz2121@naver.com")
                .nickname("minsoo")
                .pwd("123")
                .build();
        memberService.signIn(member);
        return member;
    }
    private List<Question> createSixQuestions(Member member) {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Question question = Question.builder()
                    .title(i + "번째 질문입니다.")
                    .content(i + "번째 질문의 내용입니다.")
                    .member(member)
                    .build();
            questions.add(question);
            Hashtag hashtag = Hashtag.builder().hashtagName("건강")
                    .member(member)
                    .build();
            questionService.writeQuestion(question, member, hashtag);
        }
        return questions;
    }

    @Test
    public void 전체질문글보여줘보자() throws Exception {
        //given
        createSixQuestions(createMember());
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsPagingOrdering(pageable);
        //then
        for (QuestionListDto questionListDto : questionListDtos) {
            printLog.printInfoLog("questionListDto: ");
            System.out.println("questionListDto = " + questionListDto);
        }
    }

    @Test
    public void 검색기능써보자() throws Exception {
        //given
        createSixQuestions(createMember());
        PageRequest pageable = PageRequest.of(0, 10);

        //when
        Page<QuestionListDto> questionListDtos = questionService.searchForTitlePagingOrdering("4번째", pageable);
        Page<QuestionListDto> questionListDtos2 = questionService.searchForQuestionWriterPagingOrdering("minsoo", pageable);
        //...

        //then
        for (QuestionListDto questionListDto : questionListDtos) {
            printLog.printInfoLog("questionListDto: ");
            System.out.println("questionListDto = " + questionListDto);
        }
        for (QuestionListDto questionListDto : questionListDtos2) {
            printLog.printInfoLog("questionListDto: ");
            System.out.println("questionListDto = " + questionListDto);
        }
    }
    @Test
    public void 답변채택기능() throws Exception {
        //given
        Member questionWriter = Member.builder()
                .email("miz2121@naver.com")
                .nickname("questionWriter")
                .pwd("123")
                .build();
        memberService.signIn(questionWriter);
        Member answerWriter = Member.builder()
                .email("miz1212@naver.com")
                .nickname("answerWriter")
                .pwd("123")
                .build();
        memberService.signIn(answerWriter);
        Question question = Question.builder()
                .title("질문입니다.")
                .content("질문의 내용입니다.")
                .member(questionWriter)
                .build();
        Hashtag hashtag = Hashtag.builder().hashtagName("건강")
                .member(questionWriter)
                .build();
        questionService.writeQuestion(question, questionWriter, hashtag);
        Answer answer = Answer.builder()
                .content("답변달겠습니다.")
                .question(question)
                .member(answerWriter)
                .build();
        //when
//        answerService.addAnswer(question.getId(), answer, questionWriter);  // 예외가 떠야한다.
        answerService.addAnswer(question.getId(), answer, answerWriter);
        answerService.makeAnswerSelected(question.getId(), answer, questionWriter);
        //then
        QuestionStatus questionStatus = question.getQuestionStatus();
        assertThat(questionStatus).isEqualTo(QuestionStatus.CLOSED);
        AnswerStatus answerStatus = answer.getAnswerStatus();
        assertThat(answerStatus).isEqualTo(AnswerStatus.SELECTED);
    }

    @Test
    public void 질문글수정삭제해보자() throws Exception {
        //given
        List<Question> sixQuestions = createSixQuestions(createMember());
        //when
        Question question = sixQuestions.get(0);

        Question editedQuestion = Question.builder()
                .title("수정된질문")
                .content("수정된내용")
                .member(question.getMember())
                .build();
        editedQuestion.addMember(question.getMember());
        long l = questionService.updateQuestion(question, editedQuestion, question.getMember());
//        Member notQuestionWriter = Member.builder()
//                .email("notQuestionWriter@naver.com")
//                .nickname("질문글작성자가아닙니다")
//                .pwd("123")
//                .build();
//        questionService.updateQuestion(question, editedQuestion, notQuestionWriter);  // 예외가 떠야 한다.
        //then
        Question questionById = questionRepository.findQuestionById(l);
        assertThat(questionById.getTitle()).isEqualTo("수정된질문");
        questionRepository.delete(sixQuestions.get(1));
        PageRequest pageable = PageRequest.of(0, 10);
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsPagingOrdering(pageable);
        for (QuestionListDto questionListDto : questionListDtos) {
            System.out.println("questionListDto = " + questionListDto);
        }
    }
    @Test
    public void 내가작성한질문이담긴질문글목록을볼수있어야한다() throws Exception {
        Member questionWriter = createMember();
        List<Question> sixQuestions = createSixQuestions(questionWriter);
        Member memberById = memberService.findMemberById(questionWriter.getId());

        PageRequest pageable = PageRequest.of(0, 10);
        printLog.printInfoLog("-----------------------");
        Page<QuestionListDto> questionListDtos = questionService.viewMyQuestions(pageable, memberById);
        for (QuestionListDto questionListDto : questionListDtos) {
            System.out.println("questionListDto = " + questionListDto);
        }
        printLog.printInfoLog("-----------------------");
        Member answerWriter = Member.builder()
                .email("answerWriter@naver.com")
                .nickname("answerWriter")
                .pwd("123")
                .build();
        memberService.signIn(answerWriter);
        Answer answer = Answer.builder()
                .content("답변달겠습니다.")
                .question(sixQuestions.get(2))
                .member(answerWriter)
                .build();
        answerService.addAnswer(sixQuestions.get(2).getId(), answer, answerWriter);
        printLog.printInfoLog("-----------------------");
        Page<QuestionListDto> answerListDtos = questionService.viewMyAnswers(pageable, answerWriter);
        for (QuestionListDto answerListDto : answerListDtos) {
            System.out.println("answerListDto = " + answerListDto);
        }
        printLog.printInfoLog("-----------------------");
    }
    @Test
    public void 해시태그를해시태그이름으로선택할수있어야한다() throws Exception {
        //given
        Member questionWriter = Member.builder()
                .email("miz2121@naver.com")
                .nickname("questionWriter")
                .pwd("123")
                .build();
        memberService.signIn(questionWriter);
        Member answerWriter = Member.builder()
                .email("miz1212@naver.com")
                .nickname("answerWriter")
                .pwd("123")
                .build();
        memberService.signIn(answerWriter);
        Question question = Question.builder()
                .title("질문입니다.")
                .content("질문의 내용입니다.")
                .member(questionWriter)
                .build();
        Hashtag hashtag = Hashtag.builder().hashtagName("건강")
                .member(questionWriter)
                .build();
        questionService.writeQuestion(question, questionWriter, hashtag);
        Answer answer = Answer.builder()
                .content("답변달겠습니다.")
                .question(question)
                .member(answerWriter)
                .build();
        answerService.addAnswer(question.getId(), answer, answerWriter);
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        Page<QuestionListDto> questionListDtosbyHashtag = questionService.viewQuestionsByOneHashtag(pageable, hashtag);
        //then
        for (QuestionListDto questionListDtobyHashtag : questionListDtosbyHashtag) {
            System.out.println("questionListDtobyHashtag = " + questionListDtobyHashtag);
        }
    }
}