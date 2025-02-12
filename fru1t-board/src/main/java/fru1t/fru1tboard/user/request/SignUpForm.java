package fru1t.fru1tboard.user.request;

import lombok.Getter;

@Getter
public class SignUpForm {
    private String email;
    private String username;
    private String password;
}