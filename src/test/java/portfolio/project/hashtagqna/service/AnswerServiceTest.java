package portfolio.project.hashtagqna.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.logger.PrintLog;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AnswerServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    AnswerService answerService;
    @Autowired
    MemberService memberService;
    @Autowired
    QuestionService questionService;

    private final PrintLog printLog = new PrintLog();

    @Test
    public void 채택기능() throws Exception {
        //given
        Member questionWriter = Member.builder()
                .email("login@naver.com")
                .pwd("1234")
                .nickname("questionWriter")
                .build();
        Member answerWriter = Member.builder()
                .email("logIn2@naver.com")
                .pwd("1234")
                .nickname("answerWriter")
                .build();
        memberService.signIn(questionWriter);
        memberService.logIn(questionWriter);
        memberService.signIn(answerWriter);
        memberService.logIn(answerWriter);
        Question question = Question.builder()
                .member(questionWriter)
                .title("질문있습니다.")
                .content("이런내용입니다.")
                .build();
        Hashtag ht = Hashtag.builder()
                .member(questionWriter)
                .hashtagName("기타")
                .build();
        Long questionId = questionService.writeQuestion(question, questionWriter, ht);
        Answer answer = Answer.builder()
                .question(question)
                .content("답변드리겠습니다.")
                .member(questionWriter)
                .build();
        answerService.addAnswer(questionId, answer, answerWriter);
        //when
        answerService.giveAnswerScore(ScoreStatus.SCORE5, questionId, answer, questionWriter);
        Long answerId = answerService.makeAnswerSelected(questionId, answer, questionWriter);
        //then
        assertThat(answer.getRating()).isEqualTo(5);
        assertThat(answer.getAnswerStatus()).isEqualTo(AnswerStatus.SELECTED);
    }
}