package portfolio.project.hashtagqna.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import portfolio.project.hashtagqna.dto.QuestionDto;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;

import java.util.List;

public interface QuestionRepositoryCustom {
    public QuestionDto viewQuestion(Long loginUserId, Long id);

    public List<QuestionListDto> viewFiveQuestions();

    public Page<QuestionListDto> viewQuestionsPagingOrdering(Pageable pageable);

    public Page<QuestionListDto> searchForQuestionWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionListDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionListDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionListDto> searchForTitlePagingOrdering(String text, Pageable pageable);

    public Page<QuestionListDto> searchForContentPagingOrdering(String text, Pageable pageable);

    public Page<QuestionListDto> searchForAllPagingOrdering(String text, Pageable pageable);

    public long removeQuestion(Question rmQuestion);

    public long updateQuestion(Question oldQuestion, Question updatedQuestion);

    public Page<QuestionListDto> viewMyQuestions(Pageable pageable, Member member);

    public Page<QuestionListDto> viewMyComments(Pageable pageable, Member member);

    public Page<QuestionListDto> viewMyAnswers(Pageable pageable, Member member);

    public Page<QuestionListDto> viewMyHashtags(Pageable pageable, Member member);

    public long updateNickname(Long oldMemberId, String nickname);

    public Page<QuestionListDto> viewQuestionsByOneHashtag(Pageable pageable, String hashtagName);
}
