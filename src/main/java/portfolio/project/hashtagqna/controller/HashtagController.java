package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.service.HashtagService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/hashtags")
    @ResponseBody
    public ResponseEntity<List<HashtagDto>> hashtags() {
        List<HashtagDto> hashtagDtos = hashtagService.viewAllHashtags();
        return new ResponseEntity<>(hashtagDtos, HttpStatus.OK);
    }
}
