package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.dto.HomeDto;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.service.HashtagService;
import portfolio.project.hashtagqna.service.QuestionService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final QuestionService questionService;
    private final HashtagService hashtagService;

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<HomeDto> home() {
        List<QuestionListDto> questionListDtos = questionService.viewFiveQuestions();
        List<HashtagDto> hashtagDtos = hashtagService.viewAllHashtags();

        return new ResponseEntity<>(new HomeDto(questionListDtos, hashtagDtos), HttpStatus.OK);
    }
}
