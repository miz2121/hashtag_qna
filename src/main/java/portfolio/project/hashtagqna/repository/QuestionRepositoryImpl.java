package portfolio.project.hashtagqna.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QQuestionDto;
import portfolio.project.hashtagqna.dto.QuestionDto;
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
    public List<QuestionDto> showFiveQuestions() {
        return queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
                        question.answerCount,
                        question.date))
                .from(question)
                .offset(0)
                .limit(5)
                .orderBy(question.date.desc())
                .fetch();
    }

    @Override
    public Page<QuestionDto> showQuestionPagingOrdering(Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForQuestionWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForTitlePagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForContentPagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> searchForAllPagingOrdering(String text, Pageable pageable) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> findMyQuestions(Pageable pageable, Member member) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> findMyComments(Pageable pageable, Member member) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
                        question.answerCount,
                        question.date))
                .from(question)
                .join(question.quComments, quComment).fetchJoin()
                .join(question.answers, answer).fetchJoin()
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
    public Page<QuestionDto> findMyAnswers(Pageable pageable, Member member) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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
    public Page<QuestionDto> findMyHashtags(Pageable pageable, Member member) {
        List<QuestionDto> content = queryFactory
                .select(new QQuestionDto(
                        question.writer,
                        question.closed,
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