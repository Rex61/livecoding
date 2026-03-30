package ru.company;

import java.util.UUID;

public interface UserLimitsService {
    UserLimit getByUserId(UUID userId);
}
