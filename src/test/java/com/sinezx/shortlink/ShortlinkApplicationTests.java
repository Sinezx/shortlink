package com.sinezx.shortlink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinezx.shortlink.util.ExpireTypeSelector;
import com.sinezx.shortlink.util.SerialNumberUtil;
import org.apache.curator.framework.CuratorFramework;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShortlinkApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private CuratorFramework curatorFramework;

	@Autowired
	private SerialNumberUtil serialNumberUtil;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() throws Exception {
		//generate short link code
		String content = "asdf";
		String callbackUrl = "http://www.baidu.com";
		JSONObject genJson = new JSONObject();
		genJson.put("content", content);
		genJson.put("callbackUrl", callbackUrl);
		genJson.put("expire", 1);
		genJson.put("expireType", "HOUR");
		ResultActions generate = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/generateshortlink")
						.contentType(MediaType.APPLICATION_JSON)
						.content(genJson.toString())
		);
		generate.andExpect(MockMvcResultMatchers.status().is(200));
		MockHttpServletResponse mockGenerateResponse = generate.andReturn().getResponse();
		String data = mockGenerateResponse.getContentAsString();
		JsonNode generateResp = objectMapper.readTree(data);
		Assertions.assertThat(generateResp.get("code").asText()).isEqualTo("0");
		Assertions.assertThat(generateResp.get("data")).isNotNull();
		String code = generateResp.get("data").get("code").asText();
		//redirect request
		ResultActions redirect = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/psl/" + code)
		);
		redirect.andExpect(MockMvcResultMatchers.status().is(302));
		MockHttpServletResponse mockRedirectResponse = redirect.andReturn().getResponse();
		String redirectUrl = mockRedirectResponse.getRedirectedUrl();
		String createSn = redirectUrl.split("sn=")[1];
		Assertions.assertThat(createSn).isNotBlank();

		//get short link info
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("createSn", createSn);
		ResultActions get = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/getshortlinkinfo")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonObject.toString())
		);
		get.andExpect(MockMvcResultMatchers.status().is(200));
		MockHttpServletResponse mockGetResponse = get.andReturn().getResponse();
		JsonNode getResp = objectMapper.readTree(mockGetResponse.getContentAsString());
		Assertions.assertThat(getResp.get("code").asText()).isEqualTo("0");
		Assertions.assertThat(getResp.get("data")).isNotNull();
		String getContent = getResp.get("data").get("content").asText();
		Assertions.assertThat(getContent).isEqualTo(content);
	}

	@Test
	void serialNumberTest(){
		ExecutorService threadpool = new ThreadPoolExecutor(100, 100,
				0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10000000));
		Map<String, Object> map = new ConcurrentHashMap<>();
		for(int i = 0; i < 1000; i++){
			threadpool.execute(()->{
				for(int j = 0; j < 10000; j++){
					String serialId = serialNumberUtil.concurrencyGenerateSerialNumber();
					if(map.containsKey(serialId)){
						System.out.println(serialId);
					}else{
						map.put(serialId, new Object());
					}
				}

			});
		}
		threadpool.shutdown();
		while(!threadpool.isTerminated()){}
		System.out.println(map.size());
	}

	@Test
	void zkClientTest(){
		ExpireTypeSelector expireTypeSelector = new ExpireTypeSelector(24, "HOUR");
		System.out.println(expireTypeSelector.getExpire() + expireTypeSelector.getExpireType());
	}

}
