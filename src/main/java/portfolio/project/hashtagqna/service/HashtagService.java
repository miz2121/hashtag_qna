package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.entity.QuestionHashtag;
import portfolio.project.hashtagqna.repository.HashtagRepository;
import portfolio.project.hashtagqna.repository.QuestionHashtagRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final QuestionRepository questionRepository;
    private final QuestionHashtagRepository questionHashtagRepository;


}
