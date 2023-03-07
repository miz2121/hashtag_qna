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
import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.service.AnCommentService;

@Controller
@RequiredArgsConstructor
public class AnCommentController {
    private final AnCommentService anCommentService;

    private static HttpHeaders getHttpHeaderWithRedirectAndApplicationJson(Long qid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/questions/" + qid);  // redirect
        return headers;
    }

    @PostMapping("/questions/{qid}/answers/{aid}/comments")
    public ResponseEntity<Object> createAnComment(
            Authentication authentication,
            @RequestBody AnCommentDto anCommentDto,
            @PathVariable Long qid,
            @PathVariable Long aid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        anCommentService.addAnComment(aid, anCommentDto, loginMemberId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }



    @PostMapping("/questions/{qid}/answers/{aid}/comments/remove/{cid}")
    public ResponseEntity<Object> removeComment(
            Authentication authentication,
            @PathVariable Long qid,
            @PathVariable Long aid,
            @PathVariable Long cid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        anCommentService.removeAnComment(cid, loginMemberId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("/questions/{qid}/answers/{aid}/comments/{cid}")
    public ResponseEntity<Object> updateComment(
            Authentication authentication,
            @RequestBody AnCommentDto anCommentDto,
            @PathVariable Long qid,
            @PathVariable Long aid,
            @PathVariable Long cid
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long loginMemberId = principal.getMember().getId();

        anCommentService.updateAnComment(aid, cid, anCommentDto, loginMemberId);

        HttpHeaders headers = getHttpHeaderWithRedirectAndApplicationJson(qid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
