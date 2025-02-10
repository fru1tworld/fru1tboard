package fru1t.fru1tboard.user.response;

import fru1t.fru1tboard.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long userId;
    private String username;
    private String email;

    public static UserResponse from(User user){
        UserResponse response = new UserResponse();
        response.userId = user.getUserId();
        response.email = user.getUsername();
        response.username = user.getUsername();
        return response;
    }
}
