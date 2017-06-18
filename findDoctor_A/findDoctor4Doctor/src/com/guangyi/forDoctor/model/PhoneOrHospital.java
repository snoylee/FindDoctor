package com.guangyi.forDoctor.model;

import java.io.Serializable;

public class PhoneOrHospital  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String timesection;
	private String appoTime;
	private String sex;
	private String age;
	private String isFrist;
	private String returnflag;
	private String disease;
	private String symptom;
	private int appoState;
	
	public static String TIMESECTION="timesection";
	public static String APPOTIME="appoTime";
	public static  String SEX="sex";
	public static  String AGE="age";
	public static  String ISFRIST="isFrist";
	public static  String RETURNFLAG="returnflag";
	public static  String DISEASE="disease";
	public static  String SYMPTOM="symptom";
	public static String APPOSTATE="appoState";
	
	
	
	public int getAppoState() {
		return appoState;
	}
	public void setAppoState(int appoState) {
		this.appoState = appoState;
	}
	public String getTimesection() {
		return timesection;
	}
	public void setTimesection(String timesection) {
		this.timesection = timesection;
	}
	public String getAppoTime() {
		return appoTime;
	}
	public void setAppoTime(String appoTime) {
		this.appoTime = appoTime;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getIsFrist() {
		return isFrist;
	}
	public void setIsFrist(String isFrist) {
		this.isFrist = isFrist;
	}
	public String getReturnflag() {
		return returnflag;
	}
	public void setReturnflag(String returnflag) {
		this.returnflag = returnflag;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getSymptom() {
		return symptom;
	}
	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}
	
	

}
