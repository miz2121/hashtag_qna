package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.AnComment;

public interface AnCommentRepository extends JpaRepository<AnComment, Long>, AnCommentRepositoryCustom{
    public AnComment findAnCommentById(Long id);
}
