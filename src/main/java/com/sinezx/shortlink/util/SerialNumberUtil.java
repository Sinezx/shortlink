package com.sinezx.shortlink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * this util is used to generate serial number
 */
@Component
public class SerialNumberUtil {

    @Autowired
    private MillisecondIncr millisecondIncr;

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
        String ms = serialNumberStr.substring((10));
        sb.append(millisecondIncr.getAndIncrValueStr(ms));
        return sb.toString();
    }

}
