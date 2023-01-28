package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom{
    public Answer findAnswerById(Long id);
}
