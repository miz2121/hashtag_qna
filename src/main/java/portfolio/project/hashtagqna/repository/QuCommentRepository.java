package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.QuComment;

public interface QuCommentRepository extends JpaRepository<QuComment, Long>, QuCommentRepositoryCustom{
}
