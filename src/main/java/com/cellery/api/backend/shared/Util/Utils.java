package com.cellery.api.backend.shared.Util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class Utils {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }
    public String getDateFromEpoch(Long epochTime){
        Date date = new Date(epochTime * 1000L);
        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
