package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QQuCommentDto;
import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;

import java.util.List;

import static portfolio.project.hashtagqna.entity.QQuComment.quComment;

public class QuCommentRepositoryImpl implements QuCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<QuCommentDto> viewQuComments(Long questionId) {
        return queryFactory
                .select(new QQuCommentDto(
                        quComment.writer,
                        quComment.date,
                        quComment.content))
                .from(quComment)
                .where(quComment.question.id.eq(questionId))
                .fetch();
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

    @Override
    @Transactional
    public long updateNickname(Member oldMember, Member editedMember) {
        long execute = queryFactory
                .update(quComment)
                .set(quComment.writer, editedMember.getNickname())
                .where(quComment.member.eq(oldMember))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    @Transactional
    public Long updateQuComment(QuComment oldQuComment, QuComment editedQuComment) {
        long execute = queryFactory
                .update(quComment)
                .set(quComment.content, editedQuComment.getContent())
                .set(quComment.date, editedQuComment.getDate())
                .where(quComment.eq(oldQuComment))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }
}
