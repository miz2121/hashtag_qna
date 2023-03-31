package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnswerDto;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.AuthErrorCode;
import portfolio.project.hashtagqna.exception.code.CommonErrorCode;
import portfolio.project.hashtagqna.repository.AnswerRepository;
import portfolio.project.hashtagqna.repository.MemberRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Arrays;
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
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        } else if (question.getMember() == loginUser) {
            throw new RestApiException(AuthErrorCode.ANSWER_AUTH);
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
        List<String> scoreList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));
        scoreString = scoreString.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", "").replaceAll(" ", "");
        if (!scoreList.contains(scoreString)){
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }

        Question question = questionRepository.findQuestionById(questionId);
        Answer answer = answerRepository.findAnswerById(answerId);
        if (!Objects.equals(question.getMember().getId(), loginUserId)) {
            throw new RestApiException(AuthErrorCode.SELECT_AUTH);
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        }
        answer.selectAnswer();
        answer.giveScore(scoreString);
        question.closeQuestion();
        return answerId;
    }

    @Transactional
    public Long updateAnswer(Answer oldAnswer, Answer editedAnswer, Member loginUser, Question question) {
        if (oldAnswer.getMember() != loginUser) {
            throw new RestApiException(AuthErrorCode.EDIT_ANSWER_AUTH);
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        }
        return answerRepository.updateAnswer(oldAnswer, editedAnswer);
    }

    @Transactional
    public Long removeAnswer(Long questionId, Long answerId, Long loginUserId) {
        Question question = questionRepository.findQuestionById(questionId);
        Answer answer = answerRepository.findAnswerById(answerId);
        Member loginUser = memberRepository.findMemberById(loginUserId);

        if (!Objects.equals(answer.getMember().getId(), loginUserId)) {
            throw new RestApiException(AuthErrorCode.EDIT_ANSWER_AUTH);
        }
        if (question.getQuestionStatus().equals(QuestionStatus.CLOSED)){
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        }

        question.decreaseAnswerCount();
        loginUser.decreaseAnswerCount();
        return answerRepository.removeAnswer(answer);
    }

    public List<AnswerDto> viewAnswers(Long loginUserId, Long questionId){
        return answerRepository.viewAnswers(loginUserId, questionId);
    }

    public Answer findAnswerById(Long answerId) {
        return answerRepository.findAnswerById(answerId);
    }
}
