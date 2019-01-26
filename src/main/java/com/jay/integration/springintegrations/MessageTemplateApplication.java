package com.jay.integration.springintegrations;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@SpringBootApplication
@ImportResource("integration-context.xml")
public class MessageTemplateApplication implements ApplicationRunner {
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageTemplateApplication.class);
	
	@Autowired
	private MiddlewareGateway gateway;
	
	@Bean
	@Qualifier("customComparator")
	public Comparator<Message<String>> customComparator() {
		return Comparator.comparingInt(message -> (int)message.getHeaders().get("messageNumber"));
	}
		

	public static void main(String[] args) {
		SpringApplication.run(MessageTemplateApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<CompletableFuture<Message<String>>> futures = IntStream.range(0, 10)
														.peek(num -> {System.out.println("Sending Message " + num);})
														.mapToObj(this::transformToFutureMessage)
														.collect(Collectors.toList());
		futures.forEach(this::fetchComputedMessage);
	}

	private CompletableFuture<Message<String>> transformToFutureMessage(int num) {
		//Message<String> message = MessageBuilder.withPayload("Computing value for input payload of " + num).setPriority(num).setHeader("messageNumber", num).build();
		Message<String> message = 
				MessageBuilder.withPayload("Computing value for input payload of " + num)
				.setHeader("messageNumber", num).build();
		return this.gateway.compute(message);		
	}
	
	public void fetchComputedMessage(CompletableFuture<Message<String>> future ) {
		future.thenAccept(message -> {
			LOG.info(message.getPayload());
		});
	}

}

