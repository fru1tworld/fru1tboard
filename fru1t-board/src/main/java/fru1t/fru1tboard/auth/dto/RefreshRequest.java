package fru1t.fru1tboard.auth.dto;

import lombok.Getter;

@Getter
public class RefreshRequest {
    private String refreshToken;
    private String username;
}
