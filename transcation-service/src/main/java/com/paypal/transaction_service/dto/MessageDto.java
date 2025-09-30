package com.paypal.transaction_service.dto;

public class MessageDto {
	
	private String id;
	private String content;
	private long timestamp;
	 
	 
	public MessageDto(String id, String content, long timestamp) {
		this.id = id;
		this.content = content;
		this.timestamp = timestamp;
	}
	
		
	public MessageDto() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	 
	 

	

}
