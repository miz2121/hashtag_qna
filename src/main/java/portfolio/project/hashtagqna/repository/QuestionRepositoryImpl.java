package portfolio.project.hashtagqna.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.*;
import portfolio.project.hashtagqna.entity.Hashtag;
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
    public QuestionDto viewQuestion(Long id) {
        return queryFactory
                .select(new QQuestionDto(
                        question.title,
                        question.writer,
                        question.date,
                        question.content,
                        question.questionStatus,
                        question.quCommentCount,
                        question.answerCount))
                .from(question)
                .where(question.id.eq(id))
                .fetchFirst();
    }

    @Override
    public List<QuestionListDto> viewFiveQuestions() {
        return queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .where(questionWriterEq(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.answers, answer).fetchJoin()
                .where(answerWriterEq(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.quComments, quComment).fetchJoin()
                .join(question.answers, answer).fetchJoin()
                .join(answer.anComments, anComment).fetchJoin()
                .where(quCommentWriterEq(text).
                        or(anCommentWriterEq(text)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForTitlePagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .where(titleCt(text))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForContentPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.quComments, quComment).fetchJoin()
                .join(question.answers, answer).fetchJoin()
                .join(answer.anComments, anComment).fetchJoin()
                .where(questionContentCt(text)
                        .or(answerContentCt(text))
                        .or(quCommentContentCt(text))
                        .or(anCommentContentCt(text)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionListDto> searchForAllPagingOrdering(String text, Pageable pageable) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.quComments, quComment).fetchJoin()
                .join(question.answers, answer).fetchJoin()
                .join(answer.anComments, anComment).fetchJoin()
                .where(
                        questionWriterEq(text)
                                .or(answerWriterEq(text))
                                .or(quCommentWriterEq(text))
                                .or(anCommentWriterEq(text))
                                .or(titleCt(text))
                                .or(questionContentCt(text))
                                .or(answerContentCt(text))
                                .or(quCommentContentCt(text))
                                .or(anCommentContentCt(text)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public long removeQuestion(Question rmQuestion) {
        // cascade = CascadeType.REMOVE, orphanRemoval = true을 줬으므로 주석처리
//        queryFactory
//                .delete(anComment)
//                .where(anComment.answer.question.eq(rmQuestion))
//                .execute();
//        em.flush();
//        em.clear();
//        queryFactory
//                .delete(answer)
//                .where(answer.question.eq(rmQuestion))
//                .execute();
//        em.flush();
//        em.clear();
//        queryFactory
//                .delete(quComment)
//                .where(quComment.question.eq(rmQuestion))
//                .execute();
//        em.flush();
//        em.clear();
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .where(question.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.quComments, quComment).fetchJoin()
                .join(answer.anComments, anComment).fetchJoin()
                .where(quComment.member.eq(member)
                        .or(anComment.member.eq(member)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.answers, answer).fetchJoin()
                .where(answer.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.questionHashtags, questionHashtag).fetchJoin()
                .join(questionHashtag.hashtag, hashtag).fetchJoin()
                .where(hashtag.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public long updateNickname(Member oldMember, Member editedMember) {
        long execute = queryFactory
                .update(question)
                .set(question.writer, editedMember.getNickname())
                .where(question.member.eq(oldMember))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    public Page<QuestionListDto> viewQuestionsByOneHashtag(Pageable pageable, Hashtag ht) {
        List<QuestionListDto> content = queryFactory
                .select(new QQuestionListDto(
                        question.writer,
                        question.questionStatus,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.questionHashtags, questionHashtag).fetchJoin()
                .join(questionHashtag.hashtag, hashtag).fetchJoin()
                .where(hashtag.eq(ht))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Integer total = Math.toIntExact(queryFactory
                .select(question.count())
                .from(question)
                .fetchOne());
        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression titleCt(String text) {
        return text != null ? question.title.contains(text) : null;
    }

    private BooleanExpression questionContentCt(String text) {
        return text != null ? question.content.contains(text) : null;
    }

    private BooleanExpression answerContentCt(String text) {
        return text != null ? answer.content.contains(text) : null;
    }

    private BooleanExpression quCommentContentCt(String text) {
        return text != null ? quComment.content.contains(text) : null;
    }

    private BooleanExpression anCommentContentCt(String text) {
        return text != null ? anComment.content.contains(text) : null;
    }

    private BooleanExpression questionWriterEq(String writer) {
        return writer != null ? question.writer.eq(writer) : null;
    }

    private BooleanExpression answerWriterEq(String writer) {
        return writer != null ? answer.writer.eq(writer) : null;
    }

    private BooleanExpression quCommentWriterEq(String writer) {
        return writer != null ? quComment.writer.eq(writer) : null;
    }

    private BooleanExpression anCommentWriterEq(String writer) {
        return writer != null ? anComment.writer.eq(writer) : null;
    }

}