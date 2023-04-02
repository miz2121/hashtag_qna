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
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.logger.PrintLog;
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
    public ResponseEntity<QuestionDetailDto> viewQuestion(
            Authentication authentication,
            @PathVariable Long qid) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginUserId = principal.getMember().getId();

        QuestionDto questionDto = questionService.viewQuestion(loginUserId, qid);
        List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(qid);
        List<AnswerDto> answerDtos = answerService.viewAnswers(loginUserId, qid);
        List<QuCommentDto> quCommentDtos = quCommentService.viewQuComments(loginUserId, qid);
        List<AnCommentDto> anCommentDtos = anCommentService.viewAnComments(loginUserId, qid);

        QuestionDetailDto questionDetailDto = QuestionDetailDto.builder()
                .questionDto(questionDto)
                .hashtagDtos(hashtagDtos)
                .answerDtos(answerDtos)
                .quCommentDtos(quCommentDtos)
                .anCommentDtos(anCommentDtos)
                .build();
        return new ResponseEntity<>(questionDetailDto, HttpStatus.OK);
    }

    @GetMapping("/questions")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> viewQuestions(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsPagingOrdering(pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/searchQuestionWriter")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> searchQuestionWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForQuestionWriterPagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAnswerWriter")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> searchAnswerWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAnswerWriterPagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/searchCommentWriter")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> searchCommentWriter(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForCommentWriterPagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/searchTitle")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> searchTitle(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForTitlePagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
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
    public ResponseEntity<QuestionWithHashtagsListDto> searchContent(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForContentPagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/searchAll")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> searchAll(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @PageableDefault(
                    size = 10
            ) Pageable pageable) {
        Page<QuestionListDto> questionListDtos = questionService.searchForAllPagingOrdering(text, pageable);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/myQuestions")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> viewMyQuestions(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyQuestions(pageable, member);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/myComments")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> viewMyComments(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());
        Page<QuestionListDto> questionListDtos = questionService.viewMyComments(pageable, member);

        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/myAnswers")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> viewMyAnswers(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyAnswers(pageable, member);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }

    @GetMapping("/questions/myHashtags")
    @ResponseBody
    public ResponseEntity<QuestionWithHashtagsListDto> viewMyHashtags(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberService.findMemberById(principal.getMember().getId());

        Page<QuestionListDto> questionListDtos = questionService.viewMyHashtags(pageable, member);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
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
    public ResponseEntity<QuestionWithHashtagsListDto> viewQuestionsByOneHashtag(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "text", required = false, defaultValue = "") String text) {
        Page<QuestionListDto> questionListDtos = questionService.viewQuestionsByOneHashtag(pageable, text);
        List<HashtagListDto> hashtagListDtoList = new ArrayList<>();
        for (QuestionListDto q : questionListDtos) {
            List<HashtagDto> hashtagDtos = hashtagService.viewHashtagsAtQuestion(q.getId());
            HashtagListDto hashtagListDto = new HashtagListDto(hashtagDtos);
            hashtagListDtoList.add(hashtagListDto);
        }
        QuestionWithHashtagsListDto questionWithHashtagsListDto = new QuestionWithHashtagsListDto(questionListDtos, hashtagListDtoList);
        return new ResponseEntity<>(questionWithHashtagsListDto, HttpStatus.OK);
    }
}
