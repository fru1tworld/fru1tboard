package fru1t.fru1tboard.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @JsonProperty("accessToken")
    private String accessToken;

    public static AccessToken create(String accessToken) {
        AccessToken response = new AccessToken();
        response.accessToken = "Bearer " + accessToken;
        return response;
    }
}
