package portfolio.project.hashtagqna.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.dto.QHashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QHashtag;

import java.util.ArrayList;
import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QHashtag.hashtag;
import static portfolio.project.hashtagqna.entity.QQuestionHashtag.questionHashtag;

public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public HashtagRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<HashtagDto> viewAllHashtags() {
        return queryFactory
                .select(new QHashtagDto(hashtag.hashtagName)).distinct()
                .from(hashtag)
                .fetch();
    }

    @Override
    public List<HashtagDto> findAllSelectedHashtags(List<HashtagDto> hashtags) {
        JPAQuery<HashtagDto> query = queryFactory
                .select(new QHashtagDto(hashtag.hashtagName)).distinct()
                .from(hashtag);

        BooleanBuilder builder = new BooleanBuilder();
        for (HashtagDto ht : hashtags) {
            builder.or(hashtag.hashtagName.eq(ht.getHashtagName()));
        }

        query.where(builder);
        return query.fetch();
    }

    @Override
    public List<HashtagDto> viewMyAllHashtags(Member member) {
        return queryFactory
                .select(new QHashtagDto(hashtag.hashtagName)).distinct()
                .from(hashtag)
                .where(hashtag.member.eq(member))
                .fetch();
    }

    @Override
    public List<HashtagDto> viewHashtagsAtQuestion(Long questionId) {
        return queryFactory.select(
                        new QHashtagDto(hashtag.hashtagName)
                ).distinct()
                .from(hashtag)
                .join(hashtag.questionHashtags, questionHashtag)
                .on(hashtag.id.eq(questionHashtag.hashtag.id))
                .where(questionHashtag.question.id.eq(questionId))
                .fetch();
    }
}
