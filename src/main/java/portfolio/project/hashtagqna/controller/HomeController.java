package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portfolio.project.hashtagqna.dto.*;
import portfolio.project.hashtagqna.service.HashtagService;
import portfolio.project.hashtagqna.service.QuestionService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        HomeQuestionWithHashtagsListDto questionWithHashtagsListDto = new HomeQuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);

        List<HashtagDto> hashtagDtoList = hashtagService.viewAllHashtags();
        return new ResponseEntity<>(new HomeDto(questionWithHashtagsListDto, new HomeHashtagListDto(hashtagDtoList)), HttpStatus.OK);
    }
}
