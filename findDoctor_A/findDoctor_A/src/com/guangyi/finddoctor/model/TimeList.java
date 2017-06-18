package com.guangyi.finddoctor.model;

import java.io.Serializable;
import java.util.List;

public class TimeList implements Serializable {
	private int btnState;
	private int id;
	private String timeList;
	private Long  timeMin;
	private int hint;
	
	private int daysection;  // //1����    2����  3 ���� 4 ȫ��
	private String clinicidname;   // "ר������"/��ͨ����
	private String shiftdate; // "2013-12-17"   �Ű�����
	private String scid;//�Һ����Ű�Id
	private String regfee;
	
	
	private int regtotalAM ;//����ʣ���Դ;
	private int regtotalPM ;//����ʣ���Դ;
	private int regtotalNight ;//����ʣ���Դ;
	private int regtotalAll ;//ȫ��ʣ���Դ;
	
	private boolean isAM;
	private boolean isPM;
	private boolean isNight;
	private boolean isAll;
	
	
	

	public static String REGFEE="regfee" ;//�Һŷ�;
	public static String REGTOTALAM="regtotal" ;//����ʣ���Դ;
	public static String REGTOTALPM="regtotal";//����ʣ���Դ;
	private static String  REGTOTALALL="regtotal";//ȫ��ʣ���Դ;
	private static String REGTOTALNIGHT="regtotal";//����ʣ���Դ
	public static String SCID="scid";
	public static String SHIFTDATE="shiftdate"; // "2013-12-17"   �Ű�����
	public static String CLINICIDNAME="clinicidname";   // "ר������"/��ͨ����
	public static String BTNSTATE = "btnState";
	public static String ID = "id";
	public static String TIMELIST = "timeList";
	public static String DAYSECTION="daysection";  // //1����    2����  3 ���� 4 ȫ��
	

	
	
	
	public boolean isAM() {
		return isAM;
	}

	public void setAM(boolean isAM) {
		this.isAM = isAM;
	}

	public boolean isPM() {
		return isPM;
	}

	public void setPM(boolean isPM) {
		this.isPM = isPM;
	}

	public boolean isNight() {
		return isNight;
	}

	public void setNight(boolean isNight) {
		this.isNight = isNight;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
	
	public int getRegtotalNight() {
		return regtotalNight;
	}

	public void setRegtotalNight(int regtotalNight) {
		this.regtotalNight = regtotalNight;
	}

	public int getRegtotalAll() {
		return regtotalAll;
	}

	public void setRegtotalAll(int regtotalAll) {
		this.regtotalAll = regtotalAll;
	}

	public String getRegfee() {
		return regfee;
	}

	public void setRegfee(String regfee) {
		this.regfee = regfee;
	}

	public int getRegtotalAM() {
		return regtotalAM;
	}

	public void setRegtotalAM(int regtotalAM) {
		this.regtotalAM = regtotalAM;
	}

	public int getRegtotalPM() {
		return regtotalPM;
	}

	public void setRegtotalPM(int regtotalPM) {
		this.regtotalPM = regtotalPM;
	}

	public String getScid() {
		return scid;
	}

	public void setScid(String scid) {
		this.scid = scid;
	}

	public String getClinicidname() {
		return clinicidname;
	}

	public void setClinicidname(String clinicidname) {
		this.clinicidname = clinicidname;
	}

	public String getShiftdate() {
		return shiftdate;
	}

	public void setShiftdate(String shiftdate) {
		this.shiftdate = shiftdate;
	}

	public int getDaysection() {
		return daysection;
	}

	public void setDaysection(int daysection) {
		this.daysection = daysection;
	}

	public int getHint() {
		return hint;
	}

	public void setHint(int hint) {
		this.hint = hint;
	}

	public Long getTimeMin() {
		return timeMin;
	}

	public void setTimeMin(Long timeMin) {
		this.timeMin = timeMin;
	}

	public int getBtnState() {
		return btnState;
	}

	public void setBtnState(int btnState) {
		this.btnState = btnState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTimeList() {
		return timeList;
	}

	public void setTimeList(String timeList) {
		this.timeList = timeList;
	}

}
