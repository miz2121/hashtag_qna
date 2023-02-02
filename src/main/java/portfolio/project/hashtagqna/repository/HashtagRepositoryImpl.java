package portfolio.project.hashtagqna.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.dto.QHashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;

import java.util.ArrayList;
import java.util.List;

import static portfolio.project.hashtagqna.entity.QHashtag.hashtag;

public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public HashtagRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<HashtagDto> findAllHashtags() {
        return queryFactory
                .select(new QHashtagDto(
                        hashtag.hashtagName,
                        hashtag.member))
                .from(hashtag)
                .fetch();
    }

    @Override
    public List<HashtagDto> findAllSelectedHashtagsByHashtagNames(String... names){
        List<HashtagDto> hashtagDtos = new ArrayList<>();
        for (String name : names) {
            HashtagDto hashtagDto = queryFactory
                    .select(new QHashtagDto(
                            hashtag.hashtagName,
                            hashtag.member))
                    .from(hashtag)
                    .where(hashtag.hashtagName.eq(name))
                    .fetchFirst();
            hashtagDtos.add(hashtagDto);
        }
        return hashtagDtos;
    }

    @Override
    public List<HashtagDto> findMyAllHashtags(Member member){
        return queryFactory
                .select(new QHashtagDto(
                        hashtag.hashtagName,
                        hashtag.member))
                .from(hashtag)
                .where(hashtag.member.eq(member))
                .fetch();
    }
}
