package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;
import portfolio.project.hashtagqna.logger.PrintLog;
import portfolio.project.hashtagqna.repository.*;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnCommentRepository anCommentRepository;
    private final QuCommentRepository quCommentRepository;

    PrintLog printLog = new PrintLog();

    public Member findMemberById(Long id){
        return memberRepository.findMemberById(id);
    }

    @Transactional
    public boolean signIn(Member member) {
        String email = member.getEmail();
        String nickname = member.getNickname();
        Optional<Long> alreadyExist = Optional.ofNullable(memberRepository.findByEmailNickname(email, nickname));
        if (alreadyExist.isPresent()) {
            throw new RestApiException(MemberErrorCode.INFO_ALREADY_EXISTS);
        }
        memberRepository.save(member);
        return true;
    }

//    public boolean logIn(String email, String pwd) {
//        Optional<MemberStatusDto> findMember = Optional.ofNullable(memberRepository.findMemberStatusDtoByEmailPwd(email, pwd));
//        printLog.printInfoLog("findMember: "+ findMember);
//        printLog.printInfoLog("findMember.(get()).getMemberStatus(): "+ findMember.get().getMemberStatus());
//        if (findMember.isEmpty()) {
//            throw new RestApiException(MemberErrorCode.NOT_MEMBER);
//        } else if(findMember.get().getMemberStatus() == MemberStatus.INACTIVE){
//
//            throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
//        }
//        return true;
//    }

    @Transactional
    public long editNickname(Long oldMemberId, String nickname) {

        Optional<Long> alreadyExist = Optional.ofNullable(memberRepository.findByNickname(nickname));
        if (alreadyExist.isPresent()) {
            throw new RestApiException(MemberErrorCode.INFO_ALREADY_EXISTS);
        }
        memberRepository.editNickname(oldMemberId, nickname);
        questionRepository.updateNickname(oldMemberId, nickname);
        answerRepository.updateNickname(oldMemberId, nickname);
        anCommentRepository.updateNickname(oldMemberId, nickname);
        quCommentRepository.updateNickname(oldMemberId, nickname);
        return oldMemberId;
    }

    /**
     * 회원 탈퇴 후 동일 이메일로는 재가입 불가능함을 알려야 함.
     *
     * @param id
     * @return
     */
    @Transactional
    public long signOut(Long id) {
        Member loginMember = memberRepository.findMemberById(id);
        return memberRepository.makeInactiveMember(loginMember.getId());
    }

    public MemberInfoDto viewInfo(Long id) {
        return memberRepository.viewMemberInfo(id);
    }
}
