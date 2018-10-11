package com.chaaelsoft.forgetcardeamil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	MailSender mailSender;

	@Test
	public void contextLoads() {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("liuxg@channelsoft.com");
//		message.setTo("liuxg@channelsoft.com");
//		message.setSubject("xxx");
//		message.setText("xxxxxx");
//		mailSender.send(message);
	}

}
