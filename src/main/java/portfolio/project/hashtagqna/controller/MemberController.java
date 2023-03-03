package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portfolio.project.hashtagqna.config.auth.PrincipalDetails;
import portfolio.project.hashtagqna.dto.HomeDto;
import portfolio.project.hashtagqna.dto.MemberDto;
import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.dto.MemberStatusDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.service.MemberService;

import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody MemberDto memberDto) {

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .pwd(bCryptPasswordEncoder.encode(memberDto.getPwd()))
                .nickname(memberDto.getNickname())
                .build();
        memberService.signIn(member);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/login");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        memberService.logIn(principal.getUsername(), principal.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/home");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    @ResponseBody
    public ResponseEntity<MemberInfoDto> viewInfo(
            Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        MemberInfoDto memberInfoDto = new MemberInfoDto();

        memberInfoDto.setNickname(principal.getMember().getNickname());
        memberInfoDto.setEmail(principal.getMember().getEmail());
        memberInfoDto.setQuestionCount(principal.getMember().getQuestionCount());
        memberInfoDto.setAnswerCount(principal.getMember().getAnswerCount());
        memberInfoDto.setCommentCount(principal.getMember().getCommentCount());
        memberInfoDto.setHashtagCount(principal.getMember().getHashtagCount());

        return new ResponseEntity<>(memberInfoDto, HttpStatus.OK);
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody MemberDto memberDto) {
        Member editedMember = Member.builder()
                .email(memberDto.getEmail())
                .pwd(bCryptPasswordEncoder.encode(memberDto.getPwd()))
                .nickname(memberDto.getNickname())
                .build();
        memberService.editMember(id, editedMember);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Location", "/home");  // redirect
        headers.add("Email", memberDto.getEmail());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/members/inactive/{id}")
    public ResponseEntity<Object> signOut(@PathVariable Long id) {
        memberService.signOut(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/home");  // redirect
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
