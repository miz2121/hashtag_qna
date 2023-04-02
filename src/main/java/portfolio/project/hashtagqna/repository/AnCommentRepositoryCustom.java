package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.entity.AnComment;
import portfolio.project.hashtagqna.entity.Member;

import java.util.List;

public interface AnCommentRepositoryCustom {
    public long removeAnComment(AnComment rmAnComment);
    public List<AnCommentDto> viewAnComments(Long loginUserId, Long questionId);
    public long updateNickname(Long oldMemberId, String nickname);
    public long updateAnComment(AnComment oldAnComment, AnComment editedAnComment);
}
