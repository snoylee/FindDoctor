package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class GuaHao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String guahaoHostId;
	private String hdeptId;
	private String hospName;
	private String hospProvinceName;
	private String hospCityName;
	private String depatName;

	private String hospCity;
	private int hospId;

	public static String DEPATNAME = "depatName";
	public static String GUAHAOHOSTID = "guahaoHostId";
	public static String HDEPTID = "hdeptId";
	public static String HOSPNAME = "hospName";
	public static String HOSPPROVINCENAME = "hospProvinceName";
	public static String HOSPCITYNAME = "hospCityName";

	public static String HOSPCITY = "hospCity";
	public static String HOSPID = "hospId";

	public String getHospCity() {
		return hospCity;
	}

	public void setHospCity(String hospCity) {
		this.hospCity = hospCity;
	}

	public int getHospId() {
		return hospId;
	}

	public void setHospId(int hospId) {
		this.hospId = hospId;
	}

	public String getDepatName() {
		return depatName;
	}

	public void setDepatName(String depatName) {
		this.depatName = depatName;
	}

	public String getGuahaoHostId() {
		return guahaoHostId;
	}

	public void setGuahaoHostId(String guahaoHostId) {
		this.guahaoHostId = guahaoHostId;
	}

	public String getHdeptId() {
		return hdeptId;
	}

	public void setHdeptId(String hdeptId) {
		this.hdeptId = hdeptId;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getHospProvinceName() {
		return hospProvinceName;
	}

	public void setHospProvinceName(String hospProvinceName) {
		this.hospProvinceName = hospProvinceName;
	}

	public String getHospCityName() {
		return hospCityName;
	}

	public void setHospCityName(String hospCityName) {
		this.hospCityName = hospCityName;
	}

}
