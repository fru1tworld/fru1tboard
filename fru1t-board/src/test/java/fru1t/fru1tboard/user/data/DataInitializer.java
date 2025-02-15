package fru1t.fru1tboard.user.data;

import fru1t.fru1tboard.comment.entity.Comment;
import fru1t.fru1tboard.common.Snowflake;
import fru1t.fru1tboard.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DataInitializer {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    TransactionTemplate transactionTemplate;
    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 5000;


    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 1; i < EXECUTE_COUNT; i++) {
            int finalI = i;
            executorService.submit(() -> {
                insert(finalI);
                latch.countDown();
                System.out.println("latch.getCount() = " + latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert(int userId) {
        transactionTemplate.executeWithoutResult(status -> {
            for(int i = 0; i < BULK_INSERT_SIZE; i++) {
                User user = User.create(
                        snowflake.nextId(),
                        "user" + i + "@example.com",
                        "user"+ (i +  (BULK_INSERT_SIZE * userId)),
                        "password123"
                );
                entityManager.persist(user);
            }
        });
    }
}
