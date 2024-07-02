package com.arthur.webchat.api;

import java.util.UUID;

public class RandomIdGenerator {
    public static String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 4);
    }
}
