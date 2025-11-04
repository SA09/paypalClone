package com.paypal.api_gateway.rateLimiter;

public interface RateLimiterStrategy {
	boolean tryAcquire();

}
