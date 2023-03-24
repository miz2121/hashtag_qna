package portfolio.project.hashtagqna.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QQuCommentDto;
import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;

import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnswer.answer;
import static portfolio.project.hashtagqna.entity.QQuComment.quComment;

public class QuCommentRepositoryImpl implements QuCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<QuCommentDto> viewQuComments(Long loginUserId, Long questionId) {
        return queryFactory
                .select(new QQuCommentDto(
                        quComment.id,
                        quComment.writer,
                        quComment.date,
                        quComment.content,
                        quCommentWriterIdEq(loginUserId)))
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
    public long updateNickname(Long oldMemberId, Member editedMember) {
        long execute = queryFactory
                .update(quComment)
                .set(quComment.writer, editedMember.getNickname())
                .where(quComment.member.id.eq(oldMemberId))
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

    private BooleanExpression quCommentWriterIdEq(Long loginUserIdCond){
        return loginUserIdCond != null ? quComment.member.id.eq(loginUserIdCond) : null;
    }
}
