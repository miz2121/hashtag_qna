package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.QuCommentDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.QuComment;

import java.util.List;

public interface QuCommentRepositoryCustom {
    public List<QuCommentDto> viewQuComments(Long questionId);
    public long removeQuComment(QuComment rmQuComment);
    public long updateNickname(Member oldMember, Member editedMember);
    public Long updateQuComment(QuComment oldQuComment, QuComment editedQuComment);
}
