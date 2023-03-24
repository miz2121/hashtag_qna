package portfolio.project.hashtagqna.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import portfolio.project.hashtagqna.exception.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;

}