package portfolio.project.hashtagqna.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import portfolio.project.hashtagqna.exception.code.CommonErrorCode;
import portfolio.project.hashtagqna.exception.code.ErrorCode;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;
import portfolio.project.hashtagqna.logger.PrintLog;
import portfolio.project.hashtagqna.service.MemberService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component("userAuthFailureHandler")
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
    PrintLog printLog = new PrintLog();
    @Autowired
    MemberService memberService;

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
            status.put("code", "NOT_MEMBER_OR_INACTIVE");
            status.put("message", "Member needs join" + "");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(401);
            new ObjectMapper().writeValue(response.getOutputStream(), status);
        }catch (Exception e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}