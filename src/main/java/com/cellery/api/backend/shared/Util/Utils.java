package com.cellery.api.backend.shared.Util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public Boolean emptyStr(String str) {
        return str == null || str.isEmpty();
    }
}
