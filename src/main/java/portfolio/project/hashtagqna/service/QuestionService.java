package portfolio.project.hashtagqna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.HashtagDto;
import portfolio.project.hashtagqna.dto.QuestionDto;
import portfolio.project.hashtagqna.dto.QuestionListDto;
import portfolio.project.hashtagqna.entity.*;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.AuthErrorCode;
import portfolio.project.hashtagqna.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final HashtagRepository hashtagRepository;
    private final QuestionHashtagRepository questionHashtagRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;


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

    /**
     * @param question
     * @param questionWriter
     * @param existHashtagDtos
     * @param newHashtagDtos
     * @return
     */
    @Transactional
    public Long writeQuestion(Question question, Member questionWriter, List<HashtagDto> existHashtagDtos, List<HashtagDto> newHashtagDtos) {
        Question save = questionRepository.save(question);
        save.addMember(questionWriter);

        if (!existHashtagDtos.isEmpty()) {
//            hashtagRepository.findAllSelectedHashtags(existHashtagDtos);  // 얘는 다른 api로 빼자.
            for (HashtagDto existHashtagDto : existHashtagDtos) {
                Hashtag ht = Hashtag.builder()
                        .hashtagName(existHashtagDto.getHashtagName())
                        .member(questionWriter)
                        .build();
                ht.addMember(questionWriter);
                QuestionHashtag createdQuestionHashtag = QuestionHashtag.builder()
                        .question(question)
                        .hashtag(ht)
                        .build();
                questionHashtagRepository.save(createdQuestionHashtag);
                createdQuestionHashtag.addQuestionAndHashtag(question, ht);
            }
        }
        if (!newHashtagDtos.isEmpty()) {
            for (HashtagDto newHashtagDto : newHashtagDtos) {
                Hashtag ht = Hashtag.builder()
                        .hashtagName(newHashtagDto.getHashtagName())
                        .member(questionWriter)
                        .build();
                hashtagRepository.save(ht);
                ht.addMember(questionWriter);
                questionWriter.increaseHashTagCount();
                QuestionHashtag createdQuestionHashtag = QuestionHashtag.builder()
                        .question(question)
                        .hashtag(ht)
                        .build();
                questionHashtagRepository.save(createdQuestionHashtag);
                createdQuestionHashtag.addQuestionAndHashtag(question, ht);
            }
        }
        return save.getId();
    }

    @Transactional
    public long updateQuestion(Question oldQuestion, Question editedQuestion, Member questionWriter) {
        if (oldQuestion.getQuestionStatus() == QuestionStatus.CLOSED) {
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        }
        if (!Objects.equals(oldQuestion.getMember().getId(), questionWriter.getId())) {
            throw new RestApiException(AuthErrorCode.EDIT_QUESTION_AUTH);
        }
        return questionRepository.updateQuestion(oldQuestion, editedQuestion);
    }

    @Transactional
    public long removeQuestion(Long questionId, Long loginMemberId) {
        Question question = questionRepository.findQuestionById(questionId);
        Member loginMember = memberRepository.findMemberById(loginMemberId);
        if (question.getQuestionStatus() == QuestionStatus.CLOSED) {
            throw new RestApiException(AuthErrorCode.CLOSED_QUESTION_AUTH);
        }
        if (!Objects.equals(question.getMember().getId(), loginMemberId)) {
            throw new RestApiException(AuthErrorCode.EDIT_QUESTION_AUTH);
        }

        for (int i = 0; i < question.getQuestionHashtags().size(); i++) {
            loginMember.decreaseHashTagCount();
        }
        for (Answer answer : question.getAnswers()) {
            Answer answerById = answerRepository.findAnswerById(answer.getId());
            answerById.getMember().decreaseAnswerCount();
        }
        loginMember.decreaseQuestionCount();
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

    public QuestionDto viewQuestion(Long loginUserId, Long id) {
        return questionRepository.viewQuestion(loginUserId, id);
    }

    public Page<QuestionListDto> viewQuestionsByOneHashtag(Pageable pageable, String hashtagName) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);  // page는 index처럼 0부터 시작
        pageable = PageRequest.of(page, pageable.getPageSize());
        return questionRepository.viewQuestionsByOneHashtag(pageable, hashtagName);
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findQuestionById(id);
    }

    public boolean isEditable(Member member, Long questionId){
        return Objects.equals(member.getId(), questionId);
    }
}
