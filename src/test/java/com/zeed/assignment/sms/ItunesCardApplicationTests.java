package com.zeed.assignment.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Encoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItunesCardApplicationTests {

	@Test
	public void contextLoads() {

		BASE64Encoder base64Encoder = new BASE64Encoder();
		System.out.print("base 64 is " + base64Encoder.encode("user-management-service:secret".getBytes()));

	}

}
