package portfolio.project.hashtagqna.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.logger.PrintLog;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AnswerServiceTest {
    @Autowired
    EntityManager em;
    private final PrintLog printLog = new PrintLog();
}