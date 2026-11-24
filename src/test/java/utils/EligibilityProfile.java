package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.dto.EligibilityCheckRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EligibilityProfile {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Map<String, Object> fields = new LinkedHashMap<>();

    private EligibilityProfile() {
        fields.put("debtRange", "FROM_500K_TO_1M");
        fields.put("debtTypes", List.of("CREDITS"));
        fields.put("paymentStatus", "NOT_PAYING_FOR_MONTHS");
        fields.put("loanAge", "OVER_YEAR_AGO");
        fields.put("knewCouldNotRepay", "NO");
        fields.put("newLoansAfterTrouble", "NO");
        fields.put("property", List.of("NOTHING"));
        fields.put("businessStatus", "NONE");
        fields.put("soldOrGiftedAssets", "NO");
        fields.put("dealAssets", List.of());
        fields.put("maritalStatus", "NO");
        fields.put("hasForeignAssets", false);
        fields.put("hasLawsuits", "NO");
        fields.put("hasEnforcementProceedings", "NO");
        fields.put("unusualMoneyOperations", "NO");
        fields.put("moneyOperations", List.of());
        fields.put("specialCircumstances", List.of("NONE"));
        fields.put("costsReadiness", "READY");
        fields.put("preferredPath", "SELF_SERVICE");
    }

    public static EligibilityProfile baseline() {
        return new EligibilityProfile();
    }

    public EligibilityProfile with(String field, Object value) {
        requireKnown(field);
        fields.put(field, value);
        return this;
    }

    public EligibilityProfile without(String field) {
        requireKnown(field);
        fields.remove(field);
        return this;
    }

    public EligibilityProfile withExtra(String field, Object value) {
        if (fields.containsKey(field)) {
            throw new IllegalArgumentException("'" + field + "' is a real field: use with()");
        }
        fields.put(field, value);
        return this;
    }

    public Map<String, Object> toMap() {
        return new LinkedHashMap<>(fields);
    }

    public EligibilityCheckRequest toRequest() {
        return MAPPER.convertValue(fields, EligibilityCheckRequest.class);
    }

    private static void requireKnown(String field) {
        if (!new EligibilityProfile().fields.containsKey(field)) {
            throw new IllegalArgumentException("Unknown eligibility field '" + field
                    + "': the server would silently ignore it. Use withExtra() if that is the point.");
        }
    }
}
