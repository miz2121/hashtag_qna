package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.entity.QuestionStatus;
import portfolio.project.hashtagqna.exception.AuthorizationExeption;
import portfolio.project.hashtagqna.repository.AnswerRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public Long addAnswer(Long questionId, Answer answer, Member answerWriter){
        Question question = questionRepository.findQuestionById(questionId);
        if(question.getQuestionStatus() == QuestionStatus.CLOSED){
            throw new AuthorizationExeption("채택된 글에는 답변을 더 이상 달 수 없습니다.");
        }
        else if(question.getMember() == answerWriter){
            throw new AuthorizationExeption("작성자가 아닌 사람이 답변을 달 수 있습니다.");
        }
        answer.addQuestionAndMember(question, answerWriter);
        return answer.getId();
    }

    public Long makeAnswerSelected(Long questionId, Answer answer, Member questionWriter){
        Question question = questionRepository.findQuestionById((questionId));
        if(question.getMember() != questionWriter){
            throw new AuthorizationExeption("질문 작성자가 아닌 사람은 채택할 수 없습니다.");
        }
        Long id = answerRepository.makeAnswerSelected(answer);
        question.closeQuestion();
        return id;
    }
}
