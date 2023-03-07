package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.repository.HashtagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public List<HashtagDto> viewAllHashtags() {
        return hashtagRepository.viewAllHashtags();
    }

    public List<HashtagDto> choiceHashtags(List<HashtagDto> hashtags) {
        return hashtagRepository.findAllSelectedHashtags(hashtags);
    }

    public List<HashtagDto> viewAllMyHashtags(Member member) {
        return hashtagRepository.viewMyAllHashtags(member);
    }

    public List<HashtagDto> viewHashtagsAtQuestion(Long questionId){
        return hashtagRepository.viewHashtagsAtQuestion(questionId);
    }
}
