package portfolio.project.hashtagqna.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.MemberStatus;
import portfolio.project.hashtagqna.exception.AlreadyExistEmailNicknameException;
import portfolio.project.hashtagqna.exception.NotMemberException;
import portfolio.project.hashtagqna.logger.PrintLog;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;

    private final PrintLog printLog = new PrintLog();


    @Test
    public void 회원가입가능해야한다() throws Exception {
        //given
        Member newMember = Member.builder()
                .email("signInTest@naver.com")
                .pwd("signInTest")
                .nickname("signInTest")
                .build();
        Member alreadyExistMember = Member.builder()
                .email("signInTest@naver.com")
                .pwd("signInTest")
                .nickname("signInTest")
                .build();
        //when
        boolean signIn = memberService.signIn(newMember);
//        try {
//            boolean alreadyExistSignIn = memberService.signIn(alreadyExistMember);
//        } catch (AlreadyExistEmailException e) {
//            printLog.printInfoLog("이미 있는 멤버");
//        }
        //then
        assertThat(signIn).isEqualTo(true);
    }

    @Test
    public void 로그인가능해야한다() throws Exception {
        //given
        Member normalMember = Member.builder()
                .email("normal@naver.com")
                .pwd("normalPwd")
                .nickname("normalMember")
                .build();
        boolean b = memberService.signIn(normalMember);
        Member oddMember = Member.builder()
                .email("oddEmail@naver.com")
                .pwd("oddPwd")
                .nickname("oddMember")
                .build();
        Member oddPwdMember = Member.builder()
                .email("normal@naver.com")
                .pwd("oddPwd")
                .nickname("normalMember")
                .build();

        //when
        boolean normalLogin = false;
        boolean oddLogin = false;
        boolean oddPwdLogin = false;

        //then
        try {
            normalLogin = memberService.login(normalMember);
            oddLogin = memberService.login(oddMember);
            oddPwdLogin = memberService.login(oddPwdMember);
        } catch (NotMemberException e) {
            printLog.printInfoLog("비정상 로그인");
        } finally {
            assertThat(normalLogin).isEqualTo(true);
            assertThat(oddLogin).isEqualTo(false);
            assertThat(oddPwdLogin).isEqualTo(false);
        }
    }

    @Test
    public void 수정가능해야한다() throws Exception {
        Member normalMember = Member.builder()
                .email("normal@naver.com")
                .pwd("normalPwd")
                .nickname("normalMember")
                .build();
        memberService.signIn(normalMember);

        Member oddEdit = Member.builder()
                .email("normal@naver.com")
                .pwd("normalPwd")
                .nickname("normalMember")
                .build();
        
        Member editMember;

        try {
            memberService.editMember(normalMember, oddEdit);
        } catch (AlreadyExistEmailNicknameException e) {
            printLog.printInfoLog("이미 존재하여 수정 불가능");
        } finally {
            editMember = Member.builder()
                    .email("edit@naver.com")
                    .pwd("editPwd")
                    .nickname("editMember")
                    .build();
            long l = memberService.editMember(normalMember, editMember);
            Member findMember = memberService.findMemberById(l);
            assertThat(findMember.getEmail()).isEqualTo("edit@naver.com");
            assertThat(findMember.getPwd()).isEqualTo("editPwd");
            assertThat(findMember.getNickname()).isEqualTo("editMember");
        }
    }

    /**
     * 이 테스트는 추가가 필요하다.
     * 작성한 질문, 답변, 댓글 등등도 수정되는지 알 수 있는 테스트 코드를 짜야 한다.
     *
     * @throws Exception
     */
    @Test
    public void 회원탈퇴가능해야한다() throws Exception {
        //given
        Member normalMember = Member.builder()
                .email("normal@naver.com")
                .pwd("normalPwd")
                .nickname("normalMember")
                .build();
        boolean b = memberService.signIn(normalMember);
        //when
        long l = memberService.signOut(normalMember);
        Member findMember = memberService.findMemberById(l);
        //then
        assertThat(findMember.getStatus()).isEqualTo(MemberStatus.INACTIVE);
    }
}