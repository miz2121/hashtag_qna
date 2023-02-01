package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;

import java.util.List;

public interface HashtagRepositoryCustom {
    public List<HashtagDto> findAllHashtags();

    public List<HashtagDto> findAllSelectedHashtagsByHashtagNames(String... hashtagNames);

    public List<HashtagDto> findMyAllHashtags(Member member);
}
