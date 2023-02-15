package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.AlreadyExistEmailNicknameException;
import portfolio.project.hashtagqna.exception.NotMemberException;
import portfolio.project.hashtagqna.repository.*;

import java.util.Objects;
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

    public Member findMemberById(Long id){
        return memberRepository.findMemberById(id);
    }

    @Transactional
    public boolean signIn(Member member) {
        String email = member.getEmail();
        String nickname = member.getNickname();
        Optional<Long> alreadyExist = Optional.ofNullable(memberRepository.findByEmailNickname(email, nickname));
        if (alreadyExist.isPresent()) {
            throw new AlreadyExistEmailNicknameException("이메일 혹은 닉네임이 이미 존재합니다.");
        }
        memberRepository.save(member);
        return true;
    }

    public boolean logIn(String email, String pwd) {
        Optional<Long> findMember = Optional.ofNullable(memberRepository.findMemberByEmailPwd(email, pwd));
        if (findMember.isEmpty()) {
            throw new NotMemberException("등록된 회원 정보가 없습니다.");
        }
        return true;
    }

    @Transactional
    public long editMember(Long oldMemberId, Member editedMember) {
        String email = editedMember.getEmail();
        String nickname = editedMember.getNickname();
        Optional<Long> alreadyExist = Optional.ofNullable(memberRepository.findByEmailNickname(email, nickname));
        if (alreadyExist.isPresent()) {
            throw new AlreadyExistEmailNicknameException("이메일 혹은 닉네임이 이미 존재합니다.");
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
     * @param member
     * @return
     */
    @Transactional
    public long signOut(Long id) {
        return memberRepository.makeInactiveMember(id);
    }

    public MemberInfoDto viewInfo(Long id) {
        return memberRepository.viewMemberInfo(id);
    }
}
