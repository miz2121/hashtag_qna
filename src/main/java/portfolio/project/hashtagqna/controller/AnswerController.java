package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import portfolio.project.hashtagqna.config.auth.PrincipalDetails;
import portfolio.project.hashtagqna.dto.AnswerDto;
import portfolio.project.hashtagqna.dto.CreateAnswerDto;
import portfolio.project.hashtagqna.dto.ScoreStringDto;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.service.AnswerService;
import portfolio.project.hashtagqna.service.MemberService;
import portfolio.project.hashtagqna.service.QuestionService;


@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final MemberService memberService;
    private final QuestionService questionService;

    private static HttpHeaders getHttpHeaderWithRedirectAndApplicationJson(Long qid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/" + qid);  // redirect
        return headers;
    }

    @PostMapping("/questions/{qid}/answers")
    public ResponseEntity<Object> createAnswer(
            Authentication authentication,
            @PathVariable Long qid,
            @RequestBody CreateAnswerDto createAnswerDto) {
        Question question = questionService.findQuestionById(qid);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member answerWriter = memberService.findMemberById(principal.getMember().getId());

        Answer answer = Answer.builder()
                .question(question)
                .content(createAnswerDto.getContent())
                .member(answerWriter)
                .build();
        answerService.addAnswer(qid, answer, answerWriter);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/answers/{aid}")
    public ResponseEntity<Object> updateAnswer(
            Authentication authentication,
            @RequestBody AnswerDto answerDto,
            @PathVariable Long qid,
            @PathVariable Long aid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = memberService.findMemberById(principalDetails.getMember().getId());
        Question question = questionService.findQuestionById(qid);
        Answer oldAnswer = answerService.findAnswerById(aid);

        Answer editedAnswer = Answer.builder()
                .content(answerDto.getContent())
                .question(question)
                .member(oldAnswer.getMember())
                .build();
        answerService.updateAnswer(oldAnswer, editedAnswer, loginMember, question);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("questions/{qid}/answers/remove/{aid}")
    public ResponseEntity<Object> removeAnswer(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long aid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long loginUserId = principalDetails.getMember().getId();

        Long aLong = answerService.removeAnswer(qid, aid, loginUserId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/answers/select/{aid}")
    public ResponseEntity<Object> selectAnswerAndGiveScore(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long aid,
            @RequestBody ScoreStringDto scoreStringDto
            ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long loginUserId = principalDetails.getMember().getId();

        String scoreString = scoreStringDto.getScoreString();

        answerService.makeAnswerSelectedAndGiveScore(scoreString, qid, aid, loginUserId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}

