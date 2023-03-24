package portfolio.project.hashtagqna.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode{
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "Member is inactive"),
    NOT_MEMBER(HttpStatus.UNAUTHORIZED, "Member needs join"),
    INFO_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email or nickname already exists");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
