package com.sinezx.shortlink;

import com.sinezx.shortlink.util.MillisecondIncr;
import com.sinezx.shortlink.util.SerialNumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class ShortlinkApplicationTests {

	@Autowired
	private SerialNumberUtil serialNumberUtil;

	@Autowired
	private MillisecondIncr millsecondIncr;

	@Test
	void contextLoads() throws InterruptedException {
		Map<String, String> map = new ConcurrentHashMap<>();
		DateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSSS");
		int current = 1000;
		ExecutorService executor = Executors.newFixedThreadPool(current);
		AtomicInteger flag = new AtomicInteger(0);
		for(int i = 0; i< current; i++) {
			executor.execute(() -> {
				for (int j = 0; j < 10000; j++) {
					String tmp = serialNumberUtil.concurrencyGenerateSerialNumber();
					String dateTmp = dateFormat.format(new Date());
//					String tmp = millsecondIncrTask.getLongValueStr();
					if (map.containsKey(tmp)) {
						System.out.println(tmp + " " + map.get(tmp));
						System.out.println(tmp + " " + Thread.currentThread().getName() + " " + dateTmp);

					} else {
						map.put(tmp, Thread.currentThread().getName());
					}
				}
				flag.getAndIncrement();
			});
		}
		while(flag.get() < current){}
		System.out.println(flag.get());
		System.out.println(map.size());
	}

}
