package models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiError(
        String timestamp,
        Integer status,
        String code,
        String message,
        String path,
        String correlationId,
        List<ApiFieldError> fieldErrors) {
}
