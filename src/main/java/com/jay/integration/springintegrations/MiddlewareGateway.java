package com.jay.integration.springintegrations;

import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.Message;

public interface MiddlewareGateway {
	public CompletableFuture<Message<String>> compute(Message<?> message);
}
