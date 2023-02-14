package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.AuthorizationExeption;
import portfolio.project.hashtagqna.repository.AnCommentRepository;
import portfolio.project.hashtagqna.repository.AnswerRepository;

@Service
@RequiredArgsConstructor
public class AnCommentService {
    private final AnCommentRepository anCommentRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public Long addAnComment(Long answerId, AnComment anComment, Member anCommentWriter){
        anCommentRepository.save(anComment);
        Answer answer = answerRepository.findAnswerById(answerId);
        return anComment.addAnswerAndMember(answer, anCommentWriter);
    }

    @Transactional
    public Long updateAnComment(AnComment oldAnComment, AnComment editedAnComment, Member anCommentWriter) {
        if (oldAnComment.getMember() != anCommentWriter) {
            throw new AuthorizationExeption("댓글 작성자만이 댓글을 수정할 수 있습니다.");
        }
        return anCommentRepository.updateAnComment(oldAnComment, editedAnComment);
    }

    @Transactional
    public Long removeAnComment(AnComment anComment, Member anCommentWriter) {
        if (anComment.getMember() != anCommentWriter) {
            throw new AuthorizationExeption("댓글 작성자만이 댓글을 삭제할 수 있습니다.");
        }
        anComment.getAnswer().decreaseAnCommentCount();
        anCommentWriter.decreaseCommentCount();
        return anCommentRepository.removeAnComment(anComment);
    }
}
