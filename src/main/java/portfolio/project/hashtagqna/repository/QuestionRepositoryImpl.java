package portfolio.project.hashtagqna.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.*;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;

import java.util.List;


import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QAnswer.answer;
import static portfolio.project.hashtagqna.entity.QHashtag.hashtag;
import static portfolio.project.hashtagqna.entity.QQuComment.quComment;
import static portfolio.project.hashtagqna.entity.QQuestion.question;
import static portfolio.project.hashtagqna.entity.QQuestionHashtag.questionHashtag;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public QuestionDto viewQuestion(Long loginUserId, Long id) {
        return queryFactory
                .select(new QQuestionDto(
                        question.id,
                        question.title,
                        question.writer,
                        question.date,
                        question.content,
                        question.questionStatus,
                        question.quCommentCount,
                        question.answerCount,
                        new CaseBuilder()
                                .when(questionWriterIdEq(loginUserId))
                                .then(true)
                                .otherwise(false))
                )
                .from(question)
                .where(question.id.eq(id))
                .fetchFirst();
    }

    @Override
    public List<QuestionListDto> viewFiveQuestions() {
        return queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date
                ))
                .from(question)
                .offset(0)
                .limit(5)
                .orderBy(question.date.desc())
                .fetch();
    }

    @Override
    public Page<QuestionListDto> viewQuestionsPagingOrdering(Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForQuestionWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .where(questionWriterCt(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .where(questionWriterCt(text))
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .leftJoin(question.answers, answer)
                .where(answerWriterCt(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .leftJoin(question.answers, answer)
                .where(answerWriterCt(text))
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(quCommentWriterCt(text).
                        or(anCommentWriterCt(text)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(quCommentWriterCt(text).
                        or(anCommentWriterCt(text)))
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForTitlePagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .where(questionTitleCt(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .where(questionTitleCt(text))
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForContentPagingOrdering(String text, Pageable pageable) {

        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(questionContentCt(text)
                        .or(answerContentCt(text))
                        .or(quCommentContentCt(text))
                        .or(anCommentContentCt(text)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(questionContentCt(text)
                        .or(answerContentCt(text))
                        .or(quCommentContentCt(text))
                        .or(anCommentContentCt(text)))
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForAllPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(
                        questionWriterCt(text)
                                .or(answerWriterCt(text))
                                .or(quCommentWriterCt(text))
                                .or(anCommentWriterCt(text))
                                .or(questionTitleCt(text))
                                .or(questionContentCt(text))
                                .or(answerContentCt(text))
                                .or(quCommentContentCt(text))
                                .or(anCommentContentCt(text))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(
                        questionWriterCt(text)
                                .or(answerWriterCt(text))
                                .or(quCommentWriterCt(text))
                                .or(anCommentWriterCt(text))
                                .or(questionTitleCt(text))
                                .or(questionContentCt(text))
                                .or(answerContentCt(text))
                                .or(quCommentContentCt(text))
                                .or(anCommentContentCt(text))
                )
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public long removeQuestion(Question rmQuestion) {

        queryFactory
                .delete(answer)
                .where(answer.question.eq(rmQuestion))
                .execute();
        em.flush();
        em.clear();
        queryFactory
                .delete(questionHashtag)
                .where(questionHashtag.question.eq(rmQuestion))
                .execute();
        em.flush();
        em.clear();
        long execute = queryFactory
                .delete(question)
                .where(question.eq(rmQuestion))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    @Transactional
    public long updateQuestion(Question oldQuestion, Question updatedQuestion) {
        long execute = queryFactory
                .update(question)
                .set(question.date, updatedQuestion.getDate())
                .set(question.content, updatedQuestion.getContent())
                .set(question.title, updatedQuestion.getTitle())
                .where(question.eq(oldQuestion))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    public Page<QuestionListDto> viewMyQuestions(Pageable pageable, Member member) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .where(question.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> viewMyComments(Pageable pageable, Member member) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .leftJoin(question.quComments, quComment)
                .leftJoin(question.answers, answer)
                .leftJoin(answer.anComments, anComment)
                .where(quComment.member.eq(member)
                        .or(anComment.member.eq(member)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> viewMyAnswers(Pageable pageable, Member member) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .innerJoin(question.answers, answer)
                .where(answer.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> viewMyHashtags(Pageable pageable, Member member) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date)).distinct()
                .from(question)
                .innerJoin(question.questionHashtags, questionHashtag)
                .innerJoin(questionHashtag.hashtag, hashtag)
                .where(hashtag.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public long updateNickname(Long oldMemberId, String nickname) {
        long execute = queryFactory
                .update(question)
                .set(question.writer, nickname)
                .where(question.member.id.eq(oldMemberId))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    public Page<QuestionListDto> viewQuestionsByOneHashtag(Pageable pageable, String hashtagName) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.id,
                        question.writer,
                        question.title,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .innerJoin(question.questionHashtags, questionHashtag)
                .innerJoin(questionHashtag.hashtag)
                .where(hashtag.hashtagName.eq(hashtagName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.date.desc())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression questionTitleCt(String text) {
        return text != null ? question.title.containsIgnoreCase(text) : null;
    }

    private BooleanExpression questionContentCt(String text) {
        return text != null ? question.content.containsIgnoreCase(text) : null;
    }

    private BooleanExpression answerContentCt(String text) {
        return text != null ? answer.content.containsIgnoreCase(text) : null;
    }

    private BooleanExpression quCommentContentCt(String text) {
        return text != null ? quComment.content.containsIgnoreCase(text) : null;
    }

    private BooleanExpression anCommentContentCt(String text) {
        return text != null ? anComment.content.containsIgnoreCase(text) : null;
    }

    private BooleanExpression questionWriterCt(String writer) {
        return writer != null ? question.writer.containsIgnoreCase(writer) : null;
    }

    private BooleanExpression answerWriterCt(String writer) {
        return writer != null ? answer.writer.containsIgnoreCase(writer) : null;
    }

    private BooleanExpression quCommentWriterCt(String writer) {
        return writer != null ? quComment.writer.containsIgnoreCase(writer) : null;
    }

    private BooleanExpression anCommentWriterCt(String writer) {
        return writer != null ? anComment.writer.containsIgnoreCase(writer) : null;
    }

    private BooleanExpression questionWriterIdEq(Long loginUserIdCond) {
        return loginUserIdCond != null ? Expressions.asBoolean(question.member.id.eq(loginUserIdCond)) : null;
    }
}