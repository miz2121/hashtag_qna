package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.exception.AuthExeption;
import portfolio.project.hashtagqna.repository.AnswerRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    /**
     * @param questionId
     * @param answer
     * @param answerWriter
     * @return answer.getId();
     */
    @Transactional
    public Long addAnswer(Long questionId, Answer answer, Member answerWriter) {
        Question question = questionRepository.findQuestionById(questionId);
        if (question.getQuestionStatus() == QuestionStatus.CLOSED) {
            throw new AuthExeption("채택된 글에는 답변을 더 이상 달 수 없습니다.");
        } else if (question.getMember() == answerWriter) {
            throw new AuthExeption("작성자가 아닌 사람이 답변을 달 수 있습니다.");
        }
        answerRepository.save(answer);
        answer.addQuestionAndMember(question, answerWriter);
        return answer.getId();
    }

    /**
     * @param questionId
     * @param answer
     * @param questionWriter
     * @return answerId
     */
    @Transactional
    public Long makeAnswerSelected(Long questionId, Answer answer, Member questionWriter) {
        Question question = questionRepository.findQuestionById(questionId);
        if (question.getMember() != questionWriter) {
            throw new AuthExeption("질문 작성자가 아닌 사람은 채택할 수 없습니다.");
        }
        Long answerId = answerRepository.makeAnswerSelected(answer);
        question.closeQuestion();
        return answerId;
    }

    /**
     * @param scoreStatus
     * @param questionId
     * @param answer
     * @param questionWriter
     * @return answerId
     */
    @Transactional
    public Long giveAnswerScore(ScoreStatus scoreStatus, Long questionId, Answer answer, Member questionWriter) {
        Question question = questionRepository.findQuestionById(questionId);
        if (question.getMember() != questionWriter) {
            throw new AuthExeption("질문 작성자가 아닌 사람은 채택할 수 없습니다.");
        }
        return answerRepository.giveAnswerScore(answer, scoreStatus);
    }

    @Transactional
    public Long updateAnswer(Answer oldAnswer, Answer editedAnswer, Member answerWriter) {
        if (oldAnswer.getMember() != answerWriter) {
            throw new AuthExeption("답변 작성자만이 답변을 수정할 수 있습니다.");
        }
        return answerRepository.updateAnswer(oldAnswer, editedAnswer);
    }

    @Transactional
    public Long removeAnswer(Long questionId, Answer answer, Member answerWriter) {
        if (answer.getMember() != answerWriter) {
            throw new AuthExeption("답변 작성자만이 답변을 삭제할 수 있습니다.");
        }
        Question question = questionRepository.findQuestionById(questionId);
        question.decreaseAnswerCount();
        answerWriter.decreaseAnswerCount();
        return answerRepository.removeAnswer(answer);
    }
}
