package ru.company;

import java.math.BigDecimal;
import java.util.UUID;

public record UserLimit(UUID userId, BigDecimal limitPerPayment, BigDecimal limitPerDay) {

}
