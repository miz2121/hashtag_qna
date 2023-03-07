package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.dto.QAnCommentDto;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;

import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;

public class AnCommentRepositoryImpl implements AnCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AnCommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<AnCommentDto> viewAnComments(Long questionId) {
        return queryFactory
                .select(new QAnCommentDto(
                        anComment.answer.id,
                        anComment.writer,
                        anComment.date,
                        anComment.content))
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
    public long updateNickname(Long oldMemberId, Member editedMember) {
        long execute = queryFactory
                .update(anComment)
                .set(anComment.writer, editedMember.getNickname())
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
}
