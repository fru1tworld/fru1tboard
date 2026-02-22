package fru1t.fru1tboard.user;

import fru1t.fru1tboard.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void userSaveTest(){
        User user = User.create(1L, "test@gmail.com", "testUser", "password123");
        User save = userRepository.save(user);
        System.out.println("save.getUserId() = " + save.getUserId());
    }
    @Test
    void userDeleteTest(){
        User user = User.create(1L, "test@gmail.com", "testUser", "password123");
        Optional<User> result1 = userRepository.findByUsername(user.getUsername());
        if(result1.isEmpty()) System.out.println("result = " + result1);
        else System.out.println("save.getUserId() = " + result1.get().getUserId());

        userRepository.delete(user);
        Optional<User> result2 = userRepository.findByUsername(user.getUsername());
        if(result1.isEmpty()) System.out.println("result = " + result1);

    }
}