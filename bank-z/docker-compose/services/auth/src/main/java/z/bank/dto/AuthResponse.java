package z.bank.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private String token;
    private String role;

    public AuthResponse(String username, String token, String role) {
        this.username = username;
        this.token = token;
        this.role = role;
    }
}
