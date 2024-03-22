package com.sinezx.shortlink;

import com.sinezx.shortlink.mapper.CallbackMapper;
import com.sinezx.shortlink.pojo.CallbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ShortlinkApplication {

	public static void main(String[] args) {SpringApplication.run(ShortlinkApplication.class, args);}

}
