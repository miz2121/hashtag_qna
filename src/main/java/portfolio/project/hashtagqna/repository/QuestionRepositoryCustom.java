package portfolio.project.hashtagqna.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import portfolio.project.hashtagqna.dto.QQuestionDto;
import portfolio.project.hashtagqna.dto.QuestionDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;

import java.util.List;

public interface QuestionRepositoryCustom {
    public List<QuestionDto> showFiveQuestions();

    public Page<QuestionDto> showQuestionPagingOrdering(Pageable pageable);

    public Page<QuestionDto> searchForQuestionWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable);

    public Page<QuestionDto> searchForTitlePagingOrdering(String text, Pageable pageable);

    public Page<QuestionDto> searchForContentPagingOrdering(String text, Pageable pageable);

    public Page<QuestionDto> searchForAllPagingOrdering(String text, Pageable pageable);

    public long removeQuestion(Question rmQuestion);

    public Page<QuestionDto> findMyQuestions(Pageable pageable, Member member);

    public Page<QuestionDto> findMyComments(Pageable pageable, Member member);

    public Page<QuestionDto> findMyAnswers(Pageable pageable, Member member);

    public Page<QuestionDto> findMyHashtags(Pageable pageable, Member member);
}
