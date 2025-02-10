package fru1t.fru1tboard.user.entity;

import fru1t.fru1tboard.user.request.UserUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@Entity
@Getter
@ToString
@NoArgsConstructor
public class User{
    @Id
    private Long userId;
    private String email;
    private String username;
    private String password;

    public static User create(Long userId, String email, String username, String password) {
        User user = new User();
        user.userId = userId;
        user.email = email;
        user.password = password;
        user.username = username;
        return user;
    }

    public void update(UserUpdateRequest request){
        this.email = request.getEmail();
    }
}
