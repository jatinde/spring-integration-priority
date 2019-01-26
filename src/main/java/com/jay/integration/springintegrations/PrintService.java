package com.jay.integration.springintegrations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class PrintService {
	private static final Logger LOG = LoggerFactory.getLogger(PrintService.class);
	
	public Message<?> compute(Message<String> message) {		
		LOG.info(message.getPayload());
		int messageNumber = (int)message.getHeaders().get("messageNumber");
		return MessageBuilder.withPayload("Message Send to output channel By MessageBuilder "+ messageNumber).build();
	}
}
