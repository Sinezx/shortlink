package com.sinezx.shortlink.util;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(SerialNumberUtil.class);

    @Value("${register.path.serverId:/shortlink/ids}")
    private static String registerPath;

    @Autowired
    public MillisecondIncr millisecondIncr;

    @Autowired
    public RegisterUtil registerUtil;

    private String serverId;

    @PostConstruct
    public void init(){
        serverId = registerUtil.getServerId(registerPath);
        log.info(String.format("serverId: %s", serverId));
    }

    /**
     * default generate fun
     * format: MMddHHmmssSSS
     * @return serial number
     */
    public String generateSerialNumber(){
        DateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSSS");
        return dateFormat.format(new Date());
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
