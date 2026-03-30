package ru.company;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Payment (UUID userId, OperationType operationType, BigDecimal amount, LocalDateTime paymentTime){

}
