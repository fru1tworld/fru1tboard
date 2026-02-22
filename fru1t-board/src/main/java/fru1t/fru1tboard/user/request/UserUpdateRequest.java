package fru1t.fru1tboard.user.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private Long userId;
    private String email;
}
