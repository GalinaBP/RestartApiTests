package models.dto;

import java.util.List;

public record EligibilityCheckRequest(
        String debtRange,
        List<String> debtTypes,
        String paymentStatus,
        String loanAge,
        String knewCouldNotRepay,
        String newLoansAfterTrouble,
        List<String> property,
        String businessStatus,
        String soldOrGiftedAssets,
        List<String> dealAssets,
        String maritalStatus,
        Boolean hasForeignAssets,
        String hasLawsuits,
        String hasEnforcementProceedings,
        String unusualMoneyOperations,
        List<String> moneyOperations,
        List<String> specialCircumstances,
        String costsReadiness,
        String preferredPath) {
}
