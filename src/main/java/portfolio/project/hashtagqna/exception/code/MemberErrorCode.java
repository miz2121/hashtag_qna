package portfolio.project.hashtagqna.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode{
//    NOT_MEMBER_OR_INACTIVE(HttpStatus.UNAUTHORIZED, "Member status is inactive or member needs join"),  // userAuthFailureHandler에서 처리함.
    INFO_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email or nickname already exists");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
