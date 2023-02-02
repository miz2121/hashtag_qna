package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.Hashtag;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom {
    public Hashtag findHashtagById(Long id);

    public long deleteHashtagById(Long id);
}
