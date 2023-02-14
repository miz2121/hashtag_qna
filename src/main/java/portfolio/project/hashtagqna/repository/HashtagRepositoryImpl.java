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

import java.util.ArrayList;
import java.util.List;

import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QHashtag.hashtag;

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
                .select(new QHashtagDto(
                        hashtag.hashtagName,
                        hashtag.member.nickname))
                .from(hashtag)
                .fetch();
    }

    @Override
    public List<HashtagDto> findAllSelectedHashtags(List<Hashtag> hashtags) {
        JPAQuery<HashtagDto> query = queryFactory
                .select(new QHashtagDto(
                        hashtag.hashtagName,
                        hashtag.member.nickname))
                .from(hashtag);
        BooleanBuilder builder = new BooleanBuilder();
        for (Hashtag ht : hashtags) {
            builder.or(hashtag.eq(ht));
        }
        query.where(builder);
        return query.fetch();
    }

    @Override
    public List<HashtagDto> viewMyAllHashtags(Member member) {
        return queryFactory
                .select(new QHashtagDto(
                        hashtag.hashtagName,
                        hashtag.member.nickname))
                .from(hashtag)
                .where(hashtag.member.eq(member))
                .fetch();
    }
}
