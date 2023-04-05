package portfolio.project.hashtagqna.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
public class PrintLog extends Log {
    public PrintLog() {
    }

    @Override
    public void printInfoLog(String text) {
        log.info("log.info: " + text);
    }

    @Override
    public void printInfoLog(String handleIllegalArgument, MethodArgumentNotValidException e) {
        log.info("log.handleIllegalArgument: " + handleIllegalArgument, ", log.MethodArgumentNotValidException: " + e);
    }

    @Override
    public void printInfoLog(String handleIllegalArgument, Exception ex) {
        log.info("log.handleIllegalArgument: " + handleIllegalArgument, "log.exception: " + ex);
    }
}
