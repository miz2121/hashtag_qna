package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.exception.AuthorizationExeption;
import portfolio.project.hashtagqna.repository.QuCommentRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
public class QuCommentService {
    private final QuestionRepository questionRepository;
    private final QuCommentRepository quCommentRepository;

    @Transactional
    public Long addQuComment(Long questionId, QuComment quComment, Member quCommentWriter) {
        quCommentRepository.save(quComment);
        Question question = questionRepository.findQuestionById(questionId);
        return quComment.addQuestionAndMember(question, quCommentWriter);
    }

    @Transactional
    public Long updateQuComment(QuComment oldQuComment, QuComment editedQuComment, Member quCommentWriter) {
        if (oldQuComment.getMember() != quCommentWriter) {
            throw new AuthorizationExeption("댓글 작성자만이 댓글을 수정할 수 있습니다.");
        }
        return quCommentRepository.updateQuComment(oldQuComment, editedQuComment);
    }

    @Transactional
    public Long removeQuComment(QuComment quComment, Member quCommentWriter) {
        if (quComment.getMember() != quCommentWriter) {
            throw new AuthorizationExeption("댓글 작성자만이 댓글을 삭제할 수 있습니다.");
        }
        quComment.getQuestion().decreaseQuCommentCount();
        quCommentWriter.decreaseCommentCount();
        return quCommentRepository.removeQuComment(quComment);
    }
}
