package portfolio.project.hashtagqna.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintLog extends Log {
    public PrintLog() {
    }

    @Override
    public void printInfoLog(String text) {
        log.info(text);
    }
}
