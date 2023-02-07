package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.AnswerDto;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.Member;

import java.util.List;

public interface AnswerRepositoryCustom {
    public List<AnswerDto> viewAnswers(Long questionId);
    public long removeAnswer(Answer rmAnswer);
    public long updateNickname(Member oldMember, Member editedMember);
}
