package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class CommonPatient implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int sex;
	private int age;
	private String userCard;
	private String userMoble;
	private String userAddress;
	private int id;
	private String dieaseName;

	public String getUserCard() {
		return userCard;
	}

	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}

	public String getUserMoble() {
		return userMoble;
	}

	public void setUserMoble(String userMoble) {
		this.userMoble = userMoble;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDieaseName() {
		return dieaseName;
	}

	public void setDieaseName(String dieaseName) {
		this.dieaseName = dieaseName;
	}

}
