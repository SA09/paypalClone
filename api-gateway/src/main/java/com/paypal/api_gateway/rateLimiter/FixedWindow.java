package com.paypal.api_gateway.rateLimiter;

import java.time.Duration;

public class FixedWindow implements RateLimiterStrategy{
	        private final int maxRequests;
	        private final Duration window;
	        private int count;
	        private long windowStart;

	        public FixedWindow(int maxRequests, Duration window) {
	            this.maxRequests = maxRequests;
	            this.window = window;
	            this.windowStart = System.currentTimeMillis();
	        }

		    @Override
			public synchronized boolean tryAcquire() {
				  long now = System.currentTimeMillis();
		            if (now - windowStart > window.toMillis()) {
		                count = 0;
		                windowStart = now;
		            }
		            if (count < maxRequests) {
		                count++;
		                return true;
		            }
		            return false;
			}
	    
}
