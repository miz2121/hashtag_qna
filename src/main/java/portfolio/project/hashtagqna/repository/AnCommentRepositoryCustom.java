package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.AnCommentDto;
import portfolio.project.hashtagqna.entity.AnComment;

import java.util.List;

public interface AnCommentRepositoryCustom {
    public long removeAnComment(AnComment rmAnComment);

    public List<AnCommentDto> viewAnComments(Long questionId);
}
