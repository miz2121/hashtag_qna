package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.AuthErrorCode;
import portfolio.project.hashtagqna.repository.AnCommentRepository;
import portfolio.project.hashtagqna.repository.AnswerRepository;
import portfolio.project.hashtagqna.repository.MemberRepository;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AnCommentService {
    private final AnCommentRepository anCommentRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public List<AnCommentDto> viewAnComments(Long loginUserId, Long questionId) {
        return anCommentRepository.viewAnComments(loginUserId, questionId);
    }

    @Transactional
    public Long addAnComment(Long answerId, AnCommentDto anCommentDto, Long loginMemberId) {
        Answer answer = answerRepository.findAnswerById(answerId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        AnComment anComment = AnComment.builder()
                .answer(answer)
                .member(loginMember)
                .content(anCommentDto.getContent())
                .build();
        anCommentRepository.save(anComment);
        return anComment.addAnswerAndMember(answer, loginMember);
    }

    @Transactional
    public Long updateAnComment(Long answerId, Long anCommentId, AnCommentDto anCommentDto, Long loginMemberId) {
        AnComment oldAnComment = anCommentRepository.findAnCommentById(anCommentId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        if (!Objects.equals(oldAnComment.getMember().getId(), loginMemberId)) {
            throw new RestApiException(AuthErrorCode.EDIT_COMMENT_AUTH);
        }
        Answer answer = answerRepository.findAnswerById(answerId);
        AnComment editedAnComment = AnComment.builder()
                .answer(answer)
                .member(loginMember)
                .content(anCommentDto.getContent())
                .build();
        return anCommentRepository.updateAnComment(oldAnComment, editedAnComment);
    }

    @Transactional
    public Long removeAnComment(Long anCommentId, Long loginMemberId) {
        AnComment anComment = anCommentRepository.findAnCommentById(anCommentId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        if (!Objects.equals(anComment.getMember().getId(), loginMemberId)) {
            throw new RestApiException(AuthErrorCode.EDIT_COMMENT_AUTH);
        }
        anComment.getAnswer().decreaseAnCommentCount();
        loginMember.decreaseCommentCount();
        return anCommentRepository.removeAnComment(anComment);
    }
}
