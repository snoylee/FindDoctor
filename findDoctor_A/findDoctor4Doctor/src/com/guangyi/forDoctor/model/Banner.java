package com.guangyi.forDoctor.model;

import java.io.Serializable;

public class Banner implements Serializable{
	private String  id;
	private String picture;
	private String title;
	private String content;
	private String time ;
	private String contentUrl;
	
	
	
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String string) {
		this.picture = string;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	

}
