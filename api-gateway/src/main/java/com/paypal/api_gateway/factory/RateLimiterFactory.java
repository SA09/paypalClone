package com.paypal.api_gateway.factory;

import java.time.Duration;

import com.paypal.api_gateway.rateLimiter.FixedWindow;
import com.paypal.api_gateway.rateLimiter.RateLimiterStrategy;

public class RateLimiterFactory {
	
	public static RateLimiterStrategy rateLimiterFactory(String strategyType, int maxRequests, Duration window) {
		switch (strategyType.toLowerCase()) {
        default:
            return new FixedWindow(maxRequests,window);
		}
	}

}
