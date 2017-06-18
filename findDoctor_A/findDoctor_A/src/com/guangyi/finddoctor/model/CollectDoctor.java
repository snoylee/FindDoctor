package com.guangyi.finddoctor.model;

public class CollectDoctor {
	private int docId;//编号
	private String doctName;//名称
	private String doctPosi;//级别
	private String doctHospName;//医院
	private String doctDepName;//科室
	private String doctSpecialty;//擅长
	private int score;//评分
	private int scoreSum;//评分人数
	private String doctIntroduction;//简介
	private int doctShcetype;//预约咨询状态
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getDoctShcetype() {
		return doctShcetype;
	}
	public void setDoctShcetype(int doctShcetype) {
		this.doctShcetype = doctShcetype;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDoctPosi() {
		return doctPosi;
	}
	public void setDoctPosi(String doctPosi) {
		this.doctPosi = doctPosi;
	}
	public String getDoctHospName() {
		return doctHospName;
	}
	public void setDoctHospName(String doctHospName) {
		this.doctHospName = doctHospName;
	}
	public String getDoctDepName() {
		return doctDepName;
	}
	public void setDoctDepName(String doctDepName) {
		this.doctDepName = doctDepName;
	}
	public String getDoctSpecialty() {
		return doctSpecialty;
	}
	public void setDoctSpecialty(String doctSpecialty) {
		this.doctSpecialty = doctSpecialty;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScoreSum() {
		return scoreSum;
	}
	public void setScoreSum(int scoreSum) {
		this.scoreSum = scoreSum;
	}
	public String getDoctIntroduction() {
		return doctIntroduction;
	}
	public void setDoctIntroduction(String doctIntroduction) {
		this.doctIntroduction = doctIntroduction;
	}
	
	
	
	
	

}
