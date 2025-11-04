package com.paypal.api_gateway.security;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import com.paypal.api_gateway.factory.RateLimiterFactory;
import com.paypal.api_gateway.rateLimiter.FixedWindow;
import com.paypal.api_gateway.rateLimiter.RateLimiterStrategy;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterFilter implements GlobalFilter,Ordered {
	private final Map<String, RateLimiterStrategy> limiters = new ConcurrentHashMap<>();
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("Rate limiter filter .....");
		 String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		 System.out.println("clientID"+ clientIp);
		  RateLimiterStrategy limiter = limiters.computeIfAbsent(clientIp, k -> RateLimiterFactory.rateLimiterFactory("fixed", 5, Duration.ofSeconds(30)));
		  if (!limiter.tryAcquire()) {
	            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
	            return exchange.getResponse().setComplete();
	        }

	        return chain.filter(exchange);
	}

}
