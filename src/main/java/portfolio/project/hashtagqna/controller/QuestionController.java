package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.project.hashtagqna.dto.CreateQuestionDto;
import portfolio.project.hashtagqna.dto.QuestionDto;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.service.MemberService;
import portfolio.project.hashtagqna.service.QuestionService;

import java.nio.charset.Charset;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @PostMapping("/{memberid}/questions")
    public ResponseEntity<Object> createQuestion(@PathVariable Long memberid, @RequestBody CreateQuestionDto createQuestionDto) {
        Member questionWriter = memberService.findMemberById(memberid);
        Question question = Question.builder()
                .title(createQuestionDto.getTitle())
                .content(createQuestionDto.getContent())
                .member(questionWriter)
                .build();
        questionService.writeQuestion(question, questionWriter);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/questions/{questionid}")
    public ResponseEntity<QuestionDto> viewQuestion(@PathVariable Long questionid){
        QuestionDto questionDto = questionService.viewQuestion(questionid);
        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionListDto>> viewQuestions(@PageableDefault(
            size = 10
    ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsPagingOrdering(pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchQuestionWriter")
    public ResponseEntity<Page<QuestionListDto>> searchQuestionWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForQuestionWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAnswerWriter")
    public ResponseEntity<Page<QuestionListDto>> searchAnswerWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAnswerWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchCommentWriter")
    public ResponseEntity<Page<QuestionListDto>> searchCommentWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForCommentWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchTitle")
    public ResponseEntity<Page<QuestionListDto>> searchTitle(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForTitlePagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    /**
     * (질문 + 답변 + 댓글)
     *
     * @param text
     * @param pageable
     * @return
     */
    @GetMapping("/questions/searchContent")
    public ResponseEntity<Page<QuestionListDto>> searchContent(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForContentPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAll")
    public ResponseEntity<Page<QuestionListDto>> searchAll(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAllPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/myQuestions/{memberid}")
    public ResponseEntity<Page<QuestionListDto>> viewMyQuestions(
            @PathVariable Long memberid,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Member member = memberService.findMemberById(memberid);
        Page<QuestionListDto> questionListDtos = questionService.viewMyQuestions(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/MyComments/{memberid}")
    public ResponseEntity<Page<QuestionListDto>> viewMyComments(
            @PathVariable Long memberid,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Member member = memberService.findMemberById(memberid);
        Page<QuestionListDto> questionListDtos = questionService.viewMyComments(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/MyAnswers/{memberid}")
    public ResponseEntity<Page<QuestionListDto>> viewMyAnswers(
            @PathVariable Long memberid,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Member member = memberService.findMemberById(memberid);
        Page<QuestionListDto> questionListDtos = questionService.viewMyAnswers(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/MyHashtags/{memberid}")
    public ResponseEntity<Page<QuestionListDto>> viewMyHashtags(
            @PathVariable Long memberid,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Member member = memberService.findMemberById(memberid);
        Page<QuestionListDto> questionListDtos = questionService.viewMyHashtags(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }
}
