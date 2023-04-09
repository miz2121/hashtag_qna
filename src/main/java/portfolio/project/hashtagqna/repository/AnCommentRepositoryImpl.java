package portfolio.project.hashtagqna.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.dto.QAnCommentDto;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;

import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QAnswer.answer;

public class AnCommentRepositoryImpl implements AnCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AnCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<AnCommentDto> viewAnComments(Long loginUserId, Long questionId) {
        return queryFactory
                .select(new QAnCommentDto(
                        anComment.id,
                        anComment.answer.id,
                        anComment.writer,
                        anComment.date,
                        anComment.content,
                        new CaseBuilder()
                                .when(anCommentWriterIdEq(loginUserId))
                                .then(true)
                                .otherwise(false)
                ))
                .from(anComment)
                .where(anComment.answer.question.id.eq(questionId))
                .fetch();
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

    @Override
    @Transactional
    public long updateNickname(Long oldMemberId, String nickname) {
        long execute = queryFactory
                .update(anComment)
                .set(anComment.writer, nickname)
                .where(anComment.member.id.eq(oldMemberId))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    @Transactional
    public long updateAnComment(AnComment oldAnComment, AnComment editedAnComment) {
        long execute = queryFactory
                .update(anComment)
                .set(anComment.content, editedAnComment.getContent())
                .set(anComment.date, editedAnComment.getDate())
                .where(anComment.eq(oldAnComment))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }
    private BooleanExpression anCommentWriterIdEq(Long loginUserIdCond){
        return loginUserIdCond != null ? anComment.member.id.eq(loginUserIdCond) : null;
    }
}
