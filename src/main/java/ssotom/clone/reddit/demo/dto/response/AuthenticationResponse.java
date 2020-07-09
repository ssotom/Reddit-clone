package ssotom.clone.reddit.demo.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String type;
    private String token;
    private String refreshToken;
    private String username;
    private Instant expiresAt;
}
