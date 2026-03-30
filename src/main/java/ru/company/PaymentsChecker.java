package ru.company;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PaymentsChecker {
    private final PaymentsHistoryService paymentsHistoryService;
    private final UserLimitsService userLimitsService;

    PaymentsChecker(PaymentsHistoryService paymentsHistoryService, UserLimitsService userLimitsService) {
        this.paymentsHistoryService = paymentsHistoryService;
        this.userLimitsService = userLimitsService;
    }

    public LimitResult checkPayment(Payment payment) {
        assert payment != null;
        assert payment.userId() != null;
        assert payment.amount() != null;

        UserLimit userLimit = userLimitsService.getByUserId(payment.userId());

        if (limitExceeded(payment.amount(), userLimit.limitPerPayment()))
            return new LimitResult(false, LimitResult.RejectionReason.ONE_OPERATION_LIMIT_EXCEEDED);

        LocalDateTime to = payment.paymentTime();
        LocalDateTime from = to.minusDays(1);
        BigDecimal lastDayPaymentsSum = paymentsHistoryService.getSumInTimeInterval(from, to);
        BigDecimal totalSum = lastDayPaymentsSum.add(payment.amount());

        if (limitExceeded(totalSum, userLimit.limitPerDay()))
            return new LimitResult(false, LimitResult.RejectionReason.PER_DAY_LIMIT_EXCEEDED);

        return new LimitResult(true, null);
    }

    private boolean limitExceeded(BigDecimal amount, BigDecimal limit) {
        return amount.compareTo(limit) > 0;
    }

}
