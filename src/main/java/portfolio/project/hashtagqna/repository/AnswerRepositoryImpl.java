package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.Answer;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QAnswer.answer;

public class AnswerRepositoryImpl implements AnswerRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AnswerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    @Transactional
    public long removeAnswer(Answer rmAnswer) {
        // cascade = CascadeType.REMOVE, orphanRemoval = true 을 줬으므로 주석처리
//        queryFactory
//                .delete(anComment)
//                .where(anComment.answer.eq(rmAnswer))
//                .execute();
//        em.flush();
//        em.clear();
        long execute = queryFactory
                .delete(answer)
                .where(answer.eq(rmAnswer))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }
}
