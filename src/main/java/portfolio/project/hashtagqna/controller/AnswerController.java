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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/{questionid}");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/answers/{answerid}")
    public ResponseEntity<Object> updateAnswer(
            Authentication authentication,
            @RequestBody AnswerDto answerDto,
            @PathVariable Long qid,
            @PathVariable Long answerid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = memberService.findMemberById(principalDetails.getMember().getId());
        Question question = questionService.findQuestionById(qid);
        Answer oldAnswer = answerService.findAnswerById(answerid);
        Answer editedAnswer = Answer.builder()
                .content(answerDto.getContent())
                .question(question)
                .member(oldAnswer.getMember())
                .build();
        answerService.updateAnswer(oldAnswer, editedAnswer, loginMember, question);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/{qid}");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("questions/{qid}/answers/remove/{answerid}")
    public ResponseEntity<Object> removeAnswer(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long answerid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Member loginUser = memberService.findMemberById(principalDetails.getMember().getId());
        Question question = questionService.findQuestionById(qid);
        Answer answer = answerService.findAnswerById(answerid);

        Long aLong = answerService.removeAnswer(question.getId(), answer, loginUser);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/{qid}");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/answers/select/{answerid}")
    public ResponseEntity<Object> selectAnswerAndGiveScore(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long answerid,
            @RequestBody String scoreString
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginUser = memberService.findMemberById(principalDetails.getMember().getId());

        answerService.makeAnswerSelectedAndGiveScore(scoreString, qid, answerid, loginUser);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/{qid}");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}

