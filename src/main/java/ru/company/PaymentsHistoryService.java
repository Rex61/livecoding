package ru.company;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentsHistoryService {
    BigDecimal getSumInTimeInterval(LocalDateTime from, LocalDateTime to);
}
