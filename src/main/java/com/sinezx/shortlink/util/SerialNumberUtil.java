package com.sinezx.shortlink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.LongAdder;

/**
 * this util is used to generate serial number
 */
@Component
public class SerialNumberUtil {

    @Autowired
    private MillsecondIncrTask millsecondIncrTask;

    @Value("${server.id}")
    private String serverId;

    /**
     * default generate fun
     * format: MMddHHmmssSSS
     * @return serial number
     */
    public String generateSerialNumber(){
        DateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSSS");
        String dateStr = dateFormat.format(new Date());
        return dateStr;
    }

    /**
     * High concurrency generate fun
     * format: MMddHHmmssSSS
     * @return serial number
     */
    public String concurrencyGenerateSerialNumber(){
        StringBuilder sb = new StringBuilder();
        String serialNumberStr = generateSerialNumber();
        sb.append(serialNumberStr);
        sb.append(serverId);
        sb.append(millsecondIncrTask.getLongValueStr(Integer.parseInt(serialNumberStr.substring(10))));
        return sb.toString();
    }

}
