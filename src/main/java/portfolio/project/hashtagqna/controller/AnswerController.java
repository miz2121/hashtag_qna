package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import portfolio.project.hashtagqna.dto.CreateAnswerDto;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.service.AnswerService;
import portfolio.project.hashtagqna.service.QuestionService;

@Controller
@RequiredArgsConstructor
public class AnswerController {
//    private final AnswerService answerService;
//    private final QuestionService questionService;
//
//    @PostMapping("/questions/{questionid}/answers")
//    public ResponseEntity<Object> createAnswer( @PathVariable Long questionid, @RequestBody CreateAnswerDto createAnswerDto) {
//        Question question = questionService.findQuestionById(questionid);
////        Answer answer = Answer.builder()
////                .question(question)
////                .content(createAnswerDto.getContent())
////                .member()
////                .build();
////        answerService.addAnswer(questionid, answer, )
//    }
}
