package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom{
}
