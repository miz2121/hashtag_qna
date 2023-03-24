package portfolio.project.hashtagqna.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    CLOSED_QUESTION_AUTH(HttpStatus.UNAUTHORIZED, "Can not add answer to closed question"),
    EDIT_QUESTION_AUTH(HttpStatus.UNAUTHORIZED, "Only question writer can delete or update the question"),
    EDIT_COMMENT_AUTH(HttpStatus.UNAUTHORIZED, "Only comment writer can delete or update the comment"),
    ANSWER_AUTH(HttpStatus.UNAUTHORIZED, "Question writer can not add an answer"),
    SELECT_AUTH(HttpStatus.UNAUTHORIZED, "Only question writer can select the answer"),
;
    private final HttpStatus httpStatus;
    private final String message;
}
