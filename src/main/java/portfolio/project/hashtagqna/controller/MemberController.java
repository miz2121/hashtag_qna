package portfolio.project.hashtagqna.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .pwd(memberDto.getPwd())
                .nickname(memberDto.getNickname())
                .build();
        memberService.signIn(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody String email, @RequestBody String pwd) {
        memberService.logIn(email, pwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    @ResponseBody
    public ResponseEntity<MemberInfoDto> viewInfo(@PathVariable Long id) {
        MemberInfoDto memberInfoDto = memberService.viewInfo(id);

//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
//        return new ResponseEntity<>(memberInfoDto, header, HttpStatus.OK);
        return new ResponseEntity<>(memberInfoDto, HttpStatus.OK);
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody MemberDto memberDto) {
        Member editedMember = Member.builder()
                .email(memberDto.getEmail())
                .pwd(memberDto.getPwd())
                .nickname(memberDto.getNickname())
                .build();
        memberService.editMember(id, editedMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/members/inactive/{id}")
    public ResponseEntity<Object> signOut(@PathVariable Long id) {
        memberService.signOut(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
