package portfolio.project.hashtagqna.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

public abstract class Log {
    abstract public void printInfoLog(String text);

    abstract public void printInfoLog(String handleIllegalArgument, MethodArgumentNotValidException e);

    abstract public void printInfoLog(String handleIllegalArgument, Exception ex);
}

