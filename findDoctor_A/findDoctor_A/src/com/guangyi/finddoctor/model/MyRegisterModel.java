package com.guangyi.finddoctor.model;

public class MyRegisterModel {
	private int doctId;
	private int appoId;
	private int appoState;
	private int appoMan;
	private String appoTime;
	private int appoType;
	private String doctorName;
	private String doctorHospName;
	private String doctorDepName;
	private String doctorPosi;
	private String appConsId;
	private int appoDoctor;
	private String timeSpace;
	private String appoConfigTime;
	private int evaStatus; // 0 Î´ÆÀ¼Û 1 ÒÑÆÀ¼Û
	private String space;
	private String orderId;
	private String money;
	
	
	private String attachFileByte;
public static String ATTACHFILEBYTE="attachFileByte";
	
	
	
	public static String ISDOCTORSTATE="isDoctorState";
	public static String SPACE = "space";


	
	
	public String getAttachFileByte() {
		return attachFileByte;
	}

	public void setAttachFileByte(String attachFileByte) {
		this.attachFileByte = attachFileByte;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getDoctId() {
		return doctId;
	}

	public void setDoctId(int doctId) {
		this.doctId = doctId;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getAppoConfigTime() {
		return appoConfigTime;
	}

	public void setAppoConfigTime(String appoConfigTime) {
		this.appoConfigTime = appoConfigTime;
	}

	public String getTimeSpace() {
		return timeSpace;
	}

	public void setTimeSpace(String timeSpace) {
		this.timeSpace = timeSpace;
	}

	public int getAppoDoctor() {
		return appoDoctor;
	}

	public void setAppoDoctor(int appoDoctor) {
		this.appoDoctor = appoDoctor;
	}

	public String getAppConsId() {
		return appConsId;
	}

	public void setAppConsId(String appConsId) {
		this.appConsId = appConsId;
	}

	public String getDoctorPosi() {
		return doctorPosi;
	}

	public void setDoctorPosi(String doctorPosi) {
		this.doctorPosi = doctorPosi;
	}

	public int getAppoId() {
		return appoId;
	}

	public void setAppoId(int appoId) {
		this.appoId = appoId;
	}

	public int getAppoState() {
		return appoState;
	}

	public void setAppoState(int appoState) {
		this.appoState = appoState;
	}

	public int getAppoMan() {
		return appoMan;
	}

	public void setAppoMan(int appoMan) {
		this.appoMan = appoMan;
	}

	public String getAppoTime() {
		return appoTime;
	}

	public void setAppoTime(String appoTime) {
		this.appoTime = appoTime;
	}

	public int getAppoType() {
		return appoType;
	}

	public void setAppoType(int appoType) {
		this.appoType = appoType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorHospName() {
		return doctorHospName;
	}

	public void setDoctorHospName(String doctorHospName) {
		this.doctorHospName = doctorHospName;
	}

	public String getDoctorDepName() {
		return doctorDepName;
	}

	public void setDoctorDepName(String doctorDepName) {
		this.doctorDepName = doctorDepName;
	}

	public int getEvaStatus() {
		return evaStatus;
	}

	public void setEvaStatus(int evaStatus) {
		this.evaStatus = evaStatus;
	}

}
