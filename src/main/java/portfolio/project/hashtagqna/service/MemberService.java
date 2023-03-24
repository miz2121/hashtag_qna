package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;
import portfolio.project.hashtagqna.logger.Log;
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

    public boolean logIn(String email, String pwd) {
        boolean result = false;
        Long findMember = memberRepository.findMemberByEmailPwd(email, pwd);

        if (findMember == null) {
            throw new RestApiException(MemberErrorCode.NOT_MEMBER);
        } else{
            result = true;
        }
        return result;
    }

    @Transactional
    public long editMember(Long oldMemberId, Member editedMember) {
        String nickname = editedMember.getNickname();
        Optional<Long> alreadyExist = Optional.ofNullable(memberRepository.findByNickname(nickname));
        if (alreadyExist.isPresent()) {
            throw new RestApiException(MemberErrorCode.INFO_ALREADY_EXISTS);
        }
        memberRepository.editMember(oldMemberId, editedMember);
        questionRepository.updateNickname(oldMemberId, editedMember);
        answerRepository.updateNickname(oldMemberId, editedMember);
        anCommentRepository.updateNickname(oldMemberId, editedMember);
        quCommentRepository.updateNickname(oldMemberId, editedMember);
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
