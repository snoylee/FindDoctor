package com.guangyi.finddoctor.model;

public class City {

	private String id;
	private int parentId;
	private int pkId;
	private String regionName;
	private int regionType;
	public static String ID = "regionId";
	public static String PARENTID = "parentid";
	public static String PKID = "pkid";
	public static String REGIONNAME = "regionName";
	public static String REGIONTYPE = "regionType";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getPkId() {
		return pkId;
	}

	public void setPkId(int pkId) {
		this.pkId = pkId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getRegionType() {
		return regionType;
	}

	public void setRegionType(int regionType) {
		this.regionType = regionType;
	}

}
