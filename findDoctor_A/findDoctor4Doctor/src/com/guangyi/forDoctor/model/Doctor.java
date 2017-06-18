package com.guangyi.forDoctor.model;

import java.io.Serializable;

public class Doctor implements Serializable{
	//id
	private String  expertId;
	
	private int doctId;
	
	//name
	private String  doctName;
	//擅长 
	private String  doctorPosition;
	private String  doctSpecialty;
	private String  doctIntroduction;
	//职位
	private String  doctPosi;
	
	private String  doctSex;
	
	private String userMoble;
	private String userAge;
	private String docScore;
	
	
	//平均评分
	private String avgComplex;
	//评分人数
	private int complexPeople; 
	
	//科室id
	private String doctDepaid;
	private String doctHospid;
	
	private String doctHospName;
	private String doctDepaName;
	
	
	//医院  listHosp
	private String hospName;
	//科室  listDepa
	private String depaName;
	//简介
	private String docIntroduction;
	//评分
	private String complex;
	
	public static String DOCSCORE="docScore";
	public static String USERAGE="userAge";
	public static String USERMOBLE="userMoble";
	public static String  DOCTINTRODUCTION="doctIntroduction";
	public static String  DOCTSPECIALTY="doctSpecialty";
	public static String DOCTDEPANAME="doctDepaName";
	public static String DOCTHOSPNAME="doctHospName";
	public static String  DOCTSEX="doctSex";
	public static String AVGCOMPLEX="avgComplex";
	public static String HOSPNAME="hospName";
	public static String DEPANAME="depaName";
	public static String DOCINTRODUCTION="doctIntroduction";
	public static String  EXPERTID="expertId";
	public static String  DOCTNAME="doctName";
	public static String  DOCTORPOSITION="doctSpecialty";
	public static String  DOCTPOSI="doctPosi";
	public static String  COMPLEX="score";
	public static String  COMPLEXPEOPLE="complexPeople";
	public static String DOCTDEPAID="doctDepaid";
	public static String DOCTHOSPID="doctHospid";
	public static String  DOCTID="doctId";
	
	
	
	
	
	public String getDocScore() {
		return docScore;
	}
	public void setDocScore(String docScore) {
		this.docScore = docScore;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserMoble() {
		return userMoble;
	}
	public void setUserMoble(String userMoble) {
		this.userMoble = userMoble;
	}
	public String getDoctSpecialty() {
		return doctSpecialty;
	}
	public void setDoctSpecialty(String doctSpecialty) {
		this.doctSpecialty = doctSpecialty;
	}
	public String getDoctIntroduction() {
		return doctIntroduction;
	}
	public void setDoctIntroduction(String doctIntroduction) {
		this.doctIntroduction = doctIntroduction;
	}
	public String getDoctHospName() {
		return doctHospName;
	}
	public void setDoctHospName(String doctHospName) {
		this.doctHospName = doctHospName;
	}
	public String getDoctDepaName() {
		return doctDepaName;
	}
	public void setDoctDepaName(String doctDepaName) {
		this.doctDepaName = doctDepaName;
	}
	public String getDoctSex() {
		return doctSex;
	}
	public void setDoctSex(String doctSex) {
		this.doctSex = doctSex;
	}
	public int getDoctId() {
		return doctId;
	}
	public void setDoctId(int doctId) {
		this.doctId = doctId;
	}
	public String getDoctDepaid() {
		return doctDepaid;
	}
	public void setDoctDepaid(String doctDepaid) {
		this.doctDepaid = doctDepaid;
	}
	public String getDoctHospid() {
		return doctHospid;
	}
	public void setDoctHospid(String doctHospid) {
		this.doctHospid = doctHospid;
	}
	public String getAvgComplex() {
		return avgComplex;
	}
	public void setAvgComplex(String avgComplex) {
		this.avgComplex = avgComplex;
	}
	public String getDepaName() {
		return depaName;
	}
	public void setDepaName(String depaName) {
		this.depaName = depaName;
	}
	public String getHospName() {
		return hospName;
	}
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getDocIntroduction() {
		return docIntroduction;
	}
	public void setDocIntroduction(String docIntroduction) {
		this.docIntroduction = docIntroduction;
	}
	public String getExpertId() {
		return expertId;
	}
	public String getDxpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDoctorPosition() {
		return doctorPosition;
	}
	public void setDoctorPosition(String doctorPosition) {
		this.doctorPosition = doctorPosition;
	}
	public String getDoctPosi() {
		return doctPosi;
	}
	public void setDoctPosi(String doctPosi) {
		this.doctPosi = doctPosi;
	}
	public String getComplex() {
		return complex;
	}
	public void setComplex(String complex) {
		this.complex = complex;
	}
	public int getComplexPeople() {
		return complexPeople;
	}
	public void setComplexPeople(int complexPeople) {
		this.complexPeople = complexPeople;
	}
	
	
}
