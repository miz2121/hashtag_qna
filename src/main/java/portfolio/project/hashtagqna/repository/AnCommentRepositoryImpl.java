package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.AnComment;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;

public class AnCommentRepositoryImpl implements AnCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AnCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    @Transactional
    public long removeAnComment(AnComment rmAnComment) {
        long execute = queryFactory
                .delete(anComment)
                .where(anComment.eq(rmAnComment))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }
}
