package models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenResponse(String accessToken, String refreshToken, String tokenType, Long expiresInSeconds) {
}
