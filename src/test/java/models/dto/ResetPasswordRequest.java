package models.dto;

public record ResetPasswordRequest(String token, String password) {
}
