package portfolio.project.hashtagqna.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.AnswerDto;
import portfolio.project.hashtagqna.dto.QAnCommentDto;
import portfolio.project.hashtagqna.dto.QAnswerDto;
import portfolio.project.hashtagqna.entity.Answer;
import portfolio.project.hashtagqna.entity.AnswerStatus;
import portfolio.project.hashtagqna.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QAnswer.answer;
import static portfolio.project.hashtagqna.entity.QMember.member;

public class AnswerRepositoryImpl implements AnswerRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AnswerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    // https://www.notion.so/V2-DTO-bcd7e97b3935458c910de08bd5a44bf2 참고
    @Override
    public List<AnswerDto> viewAnswers(Long loginUserId, Long questionId) {
        return queryFactory
                .select(new QAnswerDto(
                        answer.id,
                        answer.writer,
                        answer.date,
                        answer.content,
                        answer.answerStatus,
                        answer.anCommentCount,
                        answer.rating,
                        answerWriterIdEq(loginUserId)))
                .from(answer)
                .where(answer.question.id.eq(questionId))
                .fetch();
    }

    @Override
    @Transactional
    public long removeAnswer(Answer rmAnswer) {
        long execute = queryFactory
                .delete(answer)
                .where(answer.eq(rmAnswer))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    @Transactional
    public long updateNickname(Long oldMemberId, Member editedMember) {
        long execute = queryFactory
                .update(answer)
                .set(answer.writer, editedMember.getNickname())
                .where(answer.member.id.eq(oldMemberId))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    @Transactional
    public long updateAnswer(Answer oldAnswer, Answer editedAnswer) {
        long execute = queryFactory
                .update(answer)
                .set(answer.content, editedAnswer.getContent())
                .set(answer.date, editedAnswer.getDate())
                .where(answer.eq(oldAnswer))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    private BooleanExpression answerWriterIdEq(Long loginUserIdCond){
        return loginUserIdCond != null ? answer.member.id.eq(loginUserIdCond) : null;
    }
}
