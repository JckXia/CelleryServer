package com.cellery.api.backend.shared.Util;

import java.util.UUID;

public class Utils {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }
}
