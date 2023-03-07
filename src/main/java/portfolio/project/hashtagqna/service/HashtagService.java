package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.repository.HashtagRepository;
import portfolio.project.hashtagqna.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;

    public List<HashtagDto> viewAllHashtags() {
        return hashtagRepository.viewAllHashtags();
    }

    public List<HashtagDto> choiceHashtags(List<HashtagDto> hashtags) {
        return hashtagRepository.findAllSelectedHashtags(hashtags);
    }

    public List<HashtagDto> viewAllMyHashtags(Long loginMemberId) {
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        return hashtagRepository.viewMyAllHashtags(loginMember);
    }

    public List<HashtagDto> viewHashtagsAtQuestion(Long questionId){
        return hashtagRepository.viewHashtagsAtQuestion(questionId);
    }
}
