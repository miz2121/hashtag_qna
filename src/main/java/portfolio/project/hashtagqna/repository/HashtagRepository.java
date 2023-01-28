package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom{
}
