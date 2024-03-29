package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;

import java.util.List;

public interface HashtagRepositoryCustom {
    public List<HashtagDto> viewAllHashtags();

    public List<HashtagDto> findAllSelectedHashtags(List<HashtagDto> hashtags);

    public List<HashtagDto> viewMyAllHashtags(Member member);

    public List<HashtagDto> viewHashtagsAtQuestion(Long questionId);
}
