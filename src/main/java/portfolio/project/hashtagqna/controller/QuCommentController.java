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
import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.service.MemberService;
import portfolio.project.hashtagqna.service.QuCommentService;
import portfolio.project.hashtagqna.service.QuestionService;

@Controller
@RequiredArgsConstructor
public class QuCommentController {
    private final QuCommentService quCommentService;

    private static HttpHeaders getHttpHeaderWithRedirectAndApplicationJson(Long qid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/" + qid);  // redirect
        return headers;
    }

    @PostMapping("/questions/{qid}/comments")
    public ResponseEntity<Object> createQuComment(
            Authentication authentication,
            @RequestBody QuCommentDto quCommentDto,
            @PathVariable Long qid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        quCommentService.addQuComment(qid, loginMemberId, quCommentDto);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/questions/{qid}/comments/remove/{cid}")
    public ResponseEntity<Object> removeComment(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long cid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        quCommentService.removeQuComment(cid, loginMemberId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/comments/{cid}")
    public ResponseEntity<Object> updateComment(
            Authentication authentication,
            @RequestBody QuCommentDto quCommentDto,
            @PathVariable Long qid,
            @PathVariable Long cid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        quCommentService.updateQuComment(qid, cid, quCommentDto, loginMemberId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
