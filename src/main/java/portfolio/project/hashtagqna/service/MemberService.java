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
        Optional<Long> alreadyExist = memberRepository.findByEmailNickname(email, nickname);
        if (alreadyExist.isPresent()) {
            throw new AlreadyExistEmailNicknameException("이메일 혹은 닉네임이 이미 존재합니다.");
        }
        memberRepository.save(member);
        return true;
    }

    public boolean logIn(Member member) {
        Optional<Member> findMember = Optional.ofNullable(memberRepository.findMemberById(member.getId()));
        if (findMember.isEmpty()) {
            // 회원가입 한 적 없으면
            throw new NotMemberException("등록된 회원 정보가 없습니다.");
        } else if (!(Objects.equals(findMember.get().getEmail(), member.getEmail())) && (Objects.equals(findMember.get().getPwd(), member.getPwd()))) {
            // 이메일과 비밀번호가 맞지 않으면
            throw new NotMemberException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return true;
    }

    @Transactional
    public long editMember(Member oldMember, Member editedMember) {
        String email = editedMember.getEmail();
        String nickname = editedMember.getNickname();
        Optional<Long> alreadyExist = memberRepository.findByEmailNickname(email, nickname);
        if (alreadyExist.isPresent()) {
            throw new AlreadyExistEmailNicknameException("이메일 혹은 닉네임이 이미 존재합니다.");
        }
        memberRepository.editMember(oldMember, editedMember);
        questionRepository.updateNickname(oldMember, editedMember);
        answerRepository.updateNickname(oldMember, editedMember);
        anCommentRepository.updateNickname(oldMember, editedMember);
        quCommentRepository.updateNickname(oldMember, editedMember);
        return editedMember.getId();
    }

    /**
     * 회원 탈퇴 후 동일 이메일로는 재가입 불가능함을 알려야 함.
     *
     * @param member
     * @return
     */
    @Transactional
    public long signOut(Member member) {
        return memberRepository.makeInactiveMember(member);
    }

    public MemberInfoDto viewInfo(Member member) {
        return memberRepository.viewMemberInfo(member);
    }
}
