package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class BodyDisease implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String body;
	private String lableName;
	private int id;
	private int disId;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDisId() {
		return disId;
	}

	public void setDisId(int disId) {
		this.disId = disId;
	}

}
