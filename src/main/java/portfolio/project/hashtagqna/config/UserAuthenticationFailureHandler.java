package portfolio.project.hashtagqna.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import portfolio.project.hashtagqna.exception.ErrorResponse;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.ErrorCode;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;
import portfolio.project.hashtagqna.logger.PrintLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component("userAuthFailureHandler")
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
    PrintLog printLog = new PrintLog();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        printLog.printInfoLog("userAuthFailureHandler called");
        try {
            Map<String, String> status = new HashMap<>();
            status.put("status", HttpStatus.UNAUTHORIZED.value() + "");
            status.put("code", "NOT_MEMBER");
            status.put("message", "Member needs join" + "");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(), status);
        }catch (Exception e) {
            throw new RestApiException(MemberErrorCode.NOT_MEMBER);
        }
//        try {
//            Map<String, String> status = new HashMap<>();
//            status.put("status", HttpStatus.UNAUTHORIZED.toString());
//            status.put("value", HttpStatus.UNAUTHORIZED.value() + "");
//            status.put("reason", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            status.put("error", exception.getMessage());
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            new ObjectMapper().writeValue(response.getOutputStream(), status);
//        }catch (Exception e) {
//            throw e;
//        }
    }
}