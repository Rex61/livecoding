package ru.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PaymentCheckerTest {

    @Mock
    PaymentsHistoryService paymentsHistoryService;
    @Mock
    UserLimitsService userLimitsService;

    @Test
    void allLimitsPassed() {
        Mockito.when(userLimitsService.getByUserId(Mockito.any())).thenReturn(getUserLimit());
        Mockito.when(paymentsHistoryService.getSumInTimeInterval(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ZERO);

        PaymentsChecker checker = new PaymentsChecker(paymentsHistoryService, userLimitsService);
        LimitResult result =  checker.checkPayment(getPaymentWithAmount(1L));

        Assertions.assertTrue(result.getLimitPassed());
    }

    @Test
    void perPaymentLimitExceeded() {
        Mockito.when(userLimitsService.getByUserId(Mockito.any())).thenReturn(getUserLimit());

        PaymentsChecker checker = new PaymentsChecker(paymentsHistoryService, userLimitsService);
        LimitResult result =  checker.checkPayment(getPaymentWithAmount(1000L));

        Assertions.assertFalse(result.getLimitPassed());
    }

    @Test
    void limitPerDayExceeded() {
        Mockito.when(userLimitsService.getByUserId(Mockito.any())).thenReturn(getUserLimit());
        Mockito.when(paymentsHistoryService.getSumInTimeInterval(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.valueOf(9));

        PaymentsChecker checker = new PaymentsChecker(paymentsHistoryService, userLimitsService);
        LimitResult result =  checker.checkPayment(getPaymentWithAmount(2L));

        Assertions.assertFalse(result.getLimitPassed());
    }

    private UserLimit getUserLimit() {
        return new UserLimit(UUID.randomUUID(), BigDecimal.valueOf(2), BigDecimal.TEN);
    }

    private Payment getPaymentWithAmount(Long amount) {
        return new Payment(UUID.randomUUID(), OperationType.WITHDRAWAL, BigDecimal.valueOf(amount), LocalDateTime.now());
    }
}
