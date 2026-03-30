package ru.company;

import java.util.Optional;

public class LimitResult {

    private final boolean limitPassed;
    private final Optional<RejectionReason> rejectionReason;

    public LimitResult(boolean limitPassed, RejectionReason rejectionReason) {
        this.limitPassed = limitPassed;
        this.rejectionReason = rejectionReason == null ? Optional.empty() : Optional.of(rejectionReason);
    }

    public boolean getLimitPassed() {
        return limitPassed;
    }


    enum RejectionReason{
        PER_DAY_LIMIT_EXCEEDED("Лимит на день превышен"),
        ONE_OPERATION_LIMIT_EXCEEDED("Лимит на одну операцию превышен");

        private final String message;

        RejectionReason(String message) {
            this.message = message;
        }
    }
}
