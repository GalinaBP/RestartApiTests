package models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record EligibilityCheckResponse(String decision, List<String> reasons) {
}
