package portfolio.project.hashtagqna.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AnCommentServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    AnCommentService anCommentService;
    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionService questionService;
    @Autowired
    MemberService memberService;

    @Test
    public void 댓글등록수정삭제() throws Exception {
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
        Member anCommentWriter = Member.builder()
                .email("anComment@naver.com")
                .pwd("1234")
                .nickname("anCommentWriter")
                .build();
        memberService.signIn(questionWriter);
        memberService.logIn(questionWriter);
        memberService.signIn(answerWriter);
        memberService.logIn(answerWriter);
        memberService.signIn(anCommentWriter);
        memberService.logIn(anCommentWriter);
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
        Long answerId = answerService.addAnswer(questionId, answer, answerWriter);
        AnComment anComment = AnComment.builder()
                .member(anCommentWriter)
                .answer(answer)
                .content("댓글 달겠습니다.")
                .build();
        Long anCommentId = anCommentService.addAnComment(answerId, anComment, anCommentWriter);

        //when & then
        assertThat(answer.getAnCommentCount()).isEqualTo(1);

        AnComment editedAnComment = AnComment.builder()
                .member(anCommentWriter)
                .answer(answer)
                .content("댓글 내용 수정하겠습니다.")
                .build();
        anCommentService.updateAnComment(anComment, editedAnComment, anCommentWriter);
        assertThat(answer.getAnCommentCount()).isEqualTo(1);

        anCommentService.removeAnComment(anComment, anCommentWriter);
        assertThat(answer.getAnCommentCount()).isEqualTo(0);
    }
}