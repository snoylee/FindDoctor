package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class Hospital implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hospClass;
	private int hospId;
	private String hospName;
	private String hospTel;// 医院电话
	private String hospAddr;// 医院地址
	private String hospIntroduction;
	private String guahaoHostId;// 挂号医院id
	private int mapType;// 是否为本地医院 0 fou 1 shi
	private String x, y;
	private String distance;
	private int guanghaoStatus;

	public static String HOSPCLASS = "hospClass";
	public static String HOSPID = "hospId";
	public static String HOSPNAME = "hospName";
	public static String HOSPTEL = "hospTel";// 医院电话
	public static String HOSPADDR = "hospAddress";// 医院地址
	public static String HOSPINTRODUCTION = "hospIntroduction";
	public static String GUAHAOHOSPID = "guahaoHostId";
	public static String MAPTYPE = "mapType";
	public static String X = "x";
	public static String Y = "y";
	public static String DISTANCE = "distance";
	public static String GUANGHAOSTATUS="guanghaoStatus";

	
	
	
	
	public int getGuanghaoStatus() {
		return guanghaoStatus;
	}

	public void setGuanghaoStatus(int guanghaoStatus) {
		this.guanghaoStatus = guanghaoStatus;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public int getMapType() {
		return mapType;
	}

	public void setMapType(int mapType) {
		this.mapType = mapType;
	}

	public String getHospIntroduction() {
		return hospIntroduction;
	}

	public void setHospIntroduction(String hospIntroduction) {
		this.hospIntroduction = hospIntroduction;
	}

	public int getHospClass() {
		return hospClass;
	}

	public void setHospClass(int hospClass) {
		this.hospClass = hospClass;
	}

	public int getHospId() {
		return hospId;
	}

	public void setHospId(int hospId) {
		this.hospId = hospId;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getHospTel() {
		return hospTel;
	}

	public void setHospTel(String hospTel) {
		this.hospTel = hospTel;
	}

	public String getHospAddr() {
		return hospAddr;
	}

	public void setHospAddr(String hospAddr) {
		this.hospAddr = hospAddr;
	}

	public String getGuahaoHostId() {
		return guahaoHostId;
	}

	public void setGuahaoHostId(String guahaoHostId) {
		this.guahaoHostId = guahaoHostId;
	}

}
