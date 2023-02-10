package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.QuestionDto;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.entity.Hashtag;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.Question;
import portfolio.project.hashtagqna.entity.QuestionHashtag;
import portfolio.project.hashtagqna.exception.AuthorizationExeption;
import portfolio.project.hashtagqna.repository.HashtagRepository;
import portfolio.project.hashtagqna.repository.QuestionHashtagRepository;
import portfolio.project.hashtagqna.repository.QuestionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final HashtagRepository hashtagRepository;
    private final QuestionHashtagRepository questionHashtagRepository;

    public List<QuestionListDto> viewFiveQuestions() {
        return questionRepository.viewFiveQuestions();
    }

    public Page<QuestionListDto> viewQuestionsPagingOrdering(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewQuestionsPagingOrdering(pageable);
    }

    public Page<QuestionListDto> searchForQuestionWriterPagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForQuestionWriterPagingOrdering(text, pageable);
    }

    public Page<QuestionListDto> searchForAnswerWriterPagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForAnswerWriterPagingOrdering(text, pageable);
    }

    public Page<QuestionListDto> searchForCommentWriterPagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForCommentWriterPagingOrdering(text, pageable);
    }

    public Page<QuestionListDto> searchForTitlePagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForTitlePagingOrdering(text, pageable);
    }

    public Page<QuestionListDto> searchForContentPagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForContentPagingOrdering(text, pageable);
    }

    public Page<QuestionListDto> searchForAllPagingOrdering(String text, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.searchForAllPagingOrdering(text, pageable);
    }

    @Transactional
    public Long writeQuestion(Question question, Member questionWriter, Hashtag... hashtags) {
        Question save = questionRepository.save(question);
        save.addMember(questionWriter);
        for (Hashtag ht : hashtags) {
            hashtagRepository.save(ht);
            ht.addMember(questionWriter);
            QuestionHashtag createdQuestionHashtag = QuestionHashtag.builder()
                    .question(question)
                    .hashtag(ht)
                    .build();
            questionHashtagRepository.save(createdQuestionHashtag);
            createdQuestionHashtag.addQuestionAndHashtag(question, ht);
        }
        return save.getId();
    }

    @Transactional
    public long updateQuestion(Question oldQuestion, Question editedQuestion, Member questionWriter) {
        if (oldQuestion.getMember() != questionWriter) {
            throw new AuthorizationExeption("질문 작성자만이 질문을 수정할 수 있습니다.");
        }
        return questionRepository.updateQuestion(oldQuestion, editedQuestion);
    }

    @Transactional
    public long removeQuestion(Question question, Member questionWriter) {
        if (question.getMember() != questionWriter) {
            throw new AuthorizationExeption("질문 작성자만이 질문을 삭제할 수 있습니다.");
        }
        return questionRepository.removeQuestion(question);
    }

    public Page<QuestionListDto> viewMyQuestions(Pageable pageable, Member member) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewMyQuestions(pageable, member);
    }

    public Page<QuestionListDto> viewMyComments(Pageable pageable, Member member) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewMyComments(pageable, member);
    }

    public Page<QuestionListDto> viewMyAnswers(Pageable pageable, Member member) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewMyAnswers(pageable, member);
    }

    public Page<QuestionListDto> viewMyHashtags(Pageable pageable, Member member) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewMyHashtags(pageable, member);
    }

    public QuestionDto viewQuestion(Long id) {
        return questionRepository.viewQuestion(id);
    }

    public Page<QuestionListDto> viewQuestionsByOneHashtag(Pageable pageable, Hashtag ht) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewQuestionsByOneHashtag(pageable, ht);
    }

    public QuestionDto showQuestionById(Long id) {
        return viewQuestion(id);
    }
}
