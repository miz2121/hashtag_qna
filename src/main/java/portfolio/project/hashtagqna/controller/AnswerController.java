package portfolio.project.hashtagqna.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import portfolio.project.hashtagqna.config.auth.PrincipalDetails;
import portfolio.project.hashtagqna.dto.CreateAnswerDto;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.repository.MemberRepository;
import portfolio.project.hashtagqna.service.AnswerService;
import portfolio.project.hashtagqna.service.MemberService;
import portfolio.project.hashtagqna.service.QuestionService;

import java.io.IOException;
import java.net.URI;

@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final MemberService memberService;
    private final QuestionService questionService;

    @PostMapping("/questions/{questionid}/answers")
    public ResponseEntity<Object> createAnswer(
            Authentication authentication,
            @PathVariable Long questionid,
            @RequestBody CreateAnswerDto createAnswerDto) {
        Question question = questionService.findQuestionById(questionid);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member answerWriter = memberService.findMemberById(principal.getMember().getId());

        Answer answer = Answer.builder()
                .question(question)
                .content(createAnswerDto.getContent())
                .member(answerWriter)
                .build();
        answerService.addAnswer(questionid, answer, answerWriter);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/questions/{questionid}");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}

