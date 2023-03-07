package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnswerDto;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.exception.AuthExeption;
import portfolio.project.hashtagqna.repository.AnswerRepository;
import portfolio.project.hashtagqna.repository.MemberRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    /**
     * @param questionId
     * @param answer
     * @param loginUser
     * @return answer.getId();
     */
    @Transactional
    public Long addAnswer(Long questionId, Answer answer, Member loginUser) {
        Question question = questionRepository.findQuestionById(questionId);
        if (question.getQuestionStatus() == QuestionStatus.CLOSED) {
            throw new AuthExeption("채택된 글에는 답변을 더 이상 달 수 없습니다.");
        } else if (question.getMember() == loginUser) {
            throw new AuthExeption("작성자가 아닌 사람이 답변을 달 수 있습니다.");
        }
        answerRepository.save(answer);
        answer.addQuestionAndMember(question, loginUser);
        return answer.getId();
    }

    /**
     * @param scoreString, "1" ~ "5"
     * @param questionId
     * @param answerId
     * @param loginUserId
     * @return
     */
    @Transactional
    public Long makeAnswerSelectedAndGiveScore(String scoreString, Long questionId, Long answerId, Long loginUserId) {
        Question question = questionRepository.findQuestionById(questionId);
        Answer answer = answerRepository.findAnswerById(answerId);
        if (!Objects.equals(question.getMember().getId(), loginUserId)) {
            throw new AuthExeption("질문 작성자가 아닌 사람은 채택할 수 없습니다.");
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new AuthExeption("닫힌 질문 글은 더 이상 수정할 수 없습니다.");
        }
        answer.selectAnswer();
        answer.giveScore(scoreString);
        question.closeQuestion();
        return answerId;
    }

    @Transactional
    public Long updateAnswer(Answer oldAnswer, Answer editedAnswer, Member loginUser, Question question) {
        if (oldAnswer.getMember() != loginUser) {
            throw new AuthExeption("답변 작성자만이 답변을 수정할 수 있습니다.");
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new AuthExeption("닫힌 질문 글은 더 이상 수정할 수 없습니다.");
        }
        return answerRepository.updateAnswer(oldAnswer, editedAnswer);
    }

    @Transactional
    public Long removeAnswer(Long questionId, Long answerId, Long loginUserId) {
        Question question = questionRepository.findQuestionById(questionId);
        Answer answer = answerRepository.findAnswerById(answerId);
        Member loginUser = memberRepository.findMemberById(loginUserId);

        if (!Objects.equals(answer.getMember().getId(), loginUserId)) {
            throw new AuthExeption("답변 작성자만이 답변을 삭제할 수 있습니다.");
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new AuthExeption("닫힌 질문 글은 더 이상 수정 및 삭제할 수 없습니다.");
        }

        question.decreaseAnswerCount();
        loginUser.decreaseAnswerCount();
        return answerRepository.removeAnswer(answer);
    }

    public List<AnswerDto> viewAnswers(Long questionId){
        return answerRepository.viewAnswers(questionId);
    }

    public Answer findAnswerById(Long answerId) {
        return answerRepository.findAnswerById(answerId);
    }
}
