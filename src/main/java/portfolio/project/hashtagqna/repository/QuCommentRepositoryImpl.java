package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.QuComment;

import static portfolio.project.hashtagqna.entity.QQuComment.quComment;

public class QuCommentRepositoryImpl implements QuCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    @Transactional
    public long removeQuComment(QuComment rmQuComment) {
        long execute = queryFactory
                .delete(quComment)
                .where(quComment.eq(rmQuComment))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }
}
