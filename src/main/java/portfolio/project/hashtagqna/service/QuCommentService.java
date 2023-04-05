package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.AuthErrorCode;
import portfolio.project.hashtagqna.repository.MemberRepository;
import portfolio.project.hashtagqna.repository.QuCommentRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuCommentService {
    private final QuestionRepository questionRepository;
    private final QuCommentRepository quCommentRepository;
    private final MemberRepository memberRepository;

    public List<QuCommentDto> viewQuComments(Long loginUserId, Long questionId){

        return quCommentRepository.viewQuComments(loginUserId, questionId);
    }

    @Transactional
    public Long addQuComment(Long questionId, Long loginMemberId, QuCommentDto quCommentDto) {
        Question question = questionRepository.findQuestionById(questionId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        QuComment quComment = QuComment.builder()
                .question(question)
                .content(quCommentDto.getContent())
                .member(loginMember)
                .build();
        quCommentRepository.save(quComment);
        return quComment.addQuestionAndMember(question, loginMember);
    }

    @Transactional
    public Long updateQuComment(Long questionId, Long oldQuCommentId, QuCommentDto editedQuCommentDto, Long loginMemberId) {
        QuComment oldQuComment = quCommentRepository.findQuCommentById(oldQuCommentId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        if (!Objects.equals(oldQuComment.getMember().getId(), loginMemberId)) {
            throw new RestApiException(AuthErrorCode.EDIT_COMMENT_AUTH);
        }
        Question question = questionRepository.findQuestionById(questionId);
        QuComment editedQuComment = QuComment.builder()
                .content(editedQuCommentDto.getContent())
                .member(loginMember)
                .question(question)
                .build();
        return quCommentRepository.updateQuComment(oldQuComment, editedQuComment);
    }

    @Transactional
    public Long removeQuComment(Long quCommentId, Long loginMemberId) {
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        QuComment quComment = quCommentRepository.findQuCommentById(quCommentId);
        if (!Objects.equals(quComment.getMember().getId(), loginMemberId)) {
            throw new RestApiException(AuthErrorCode.EDIT_COMMENT_AUTH);
        }
        quComment.getQuestion().decreaseQuCommentCount();
        loginMember.decreaseCommentCount();
        return quCommentRepository.removeQuComment(quComment);
    }
}
