package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;

import java.util.List;

public interface QuCommentRepositoryCustom {
    public List<QuCommentDto> viewQuComments(Long loginUserId, Long questionId);
    public long removeQuComment(QuComment rmQuComment);
    public long updateNickname(Long oldMemberId, String nickname);
    public Long updateQuComment(QuComment oldQuComment, QuComment editedQuComment);
}
