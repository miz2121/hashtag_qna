package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.project.hashtagqna.config.auth.PrincipalDetails;
import portfolio.project.hashtagqna.dto.*;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.repository.QuestionRepository;
import portfolio.project.hashtagqna.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;
    private final HashtagService hashtagService;
    private final AnswerService answerService;
    private final QuCommentService quCommentService;
    private final AnCommentService anCommentService;
    private final QuestionRepository questionRepository;

    @PostMapping("/questions")
    public ResponseEntity<Object> createQuestion(
            Authentication authentication,
            @RequestBody CreateQuestionDto createQuestionDto) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member questionWriter = memberService.findMemberById((principal.getMember().getId()));

        Question question = Question.builder()
                .title(createQuestionDto.getTitle())
                .content(createQuestionDto.getContent())
                .member(questionWriter)
                .build();

        List<HashtagDto> existHashtagDtos = createQuestionDto.getExistHashtagDtos();
        List<HashtagDto> newHashtagDtos = createQuestionDto.getNewHashtagDtos();

        Long qid = questionService.writeQuestion(question, questionWriter, existHashtagDtos, newHashtagDtos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/" + qid);  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/questions/{qid}")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsDto> viewQuestion(
            Authentication authentication,
            @PathVariable Long qid) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginUserId = principal.getMember().getId();

        QuestionDto questionDto = questionService.viewQuestion(loginUserId, qid);
        List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(qid);
        List<AnswerDto> answerDtos = answerService.viewAnswers(loginUserId, qid);
        List<QuCommentDto> quCommentDtos = quCommentService.viewQuComments(loginUserId, qid);
        List<AnCommentDto> anCommentDtos = anCommentService.viewAnComments(loginUserId, qid);

        QuestionWithHashtagsDto questionWithHashtagsDto = QuestionWithHashtagsDto.builder()
                .questionDto(questionDto)
                .hashtagDtos(hashtagDtos)
                .answerDtos(answerDtos)
                .quCommentDtos(quCommentDtos)
                .anCommentDtos(anCommentDtos)
                .build();
        return new ResponseEntity<>(questionWithHashtagsDto, HttpStatus.OK);
    }

    @GetMapping("/questions")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewQuestions(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsPagingOrdering(pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchQuestionWriter")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> searchQuestionWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForQuestionWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAnswerWriter")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> searchAnswerWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAnswerWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchCommentWriter")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> searchCommentWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForCommentWriterPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchTitle")
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> searchContent(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForContentPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAll")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> searchAll(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAllPagingOrdering(text, pageable);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/myQuestions")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewMyQuestions(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyQuestions(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/myComments")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewMyComments(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyComments(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/myAnswers")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewMyAnswers(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyAnswers(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @GetMapping("/questions/myHashtags")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewMyHashtags(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyHashtags(pageable, member);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }

    @PostMapping("/questions/remove/{qid}")
    public ResponseEntity<Object> removeQuestion(
            Authentication authentication,
            @PathVariable Long qid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long loginUserId = principalDetails.getMember().getId();

        long l = questionService.removeQuestion(qid, loginUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}")
    public ResponseEntity<Object> updateQuestion(
            Authentication authentication,
            @RequestBody QuestionDto questionDto,
            @PathVariable Long qid
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = memberService.findMemberById(principalDetails.getMember().getId());
        Question oldQuestion = questionService.findQuestionById(qid);
        Question editedQuestion = Question.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getContent())
                .member(loginMember)
                .build();

        long l = questionService.updateQuestion(oldQuestion, editedQuestion, loginMember);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/" + qid);  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    /**
     *
     */
    @GetMapping("/questions/hashtag")
    @ResponseBody
    public ResponseEntity<Page<QuestionListDto>> viewQuestionsByOneHashtag(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "text", required = false, defaultValue = "") String text) {
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsByOneHashtag(pageable, text);
        return new ResponseEntity<>(questionListDtos, HttpStatus.OK);
    }
}
