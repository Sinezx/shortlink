package com.sinezx.shortlink.schedule;

import com.sinezx.shortlink.mapper.CallbackMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@EnableScheduling
@Component
@Slf4j
public class ExpireDataClear {
    @Autowired
    private CallbackMapper callbackMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Scheduled(cron = "0 0 4 * * ?")
    public void clearExpireShortLink(){
        transactionTemplate.executeWithoutResult(status -> {
            try {
                int count = callbackMapper.deleteExpireShortlink();
                log.info(String.format("delete exprie shortlink count: %d", count));
            }catch (Exception e){
                status.setRollbackOnly();
                log.error(e.getMessage());
            }
        });
    }
}
