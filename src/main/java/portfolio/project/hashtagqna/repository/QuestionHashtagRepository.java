package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.QuestionHashtag;

public interface QuestionHashtagRepository extends JpaRepository<QuestionHashtag, Long> {
}
