package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class Doctor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//id
	private String  expertId;
	
	private int doctId;
	
	//name
	private String  doctName;
	//擅长 
	private String  doctorPosition;
	//职位
	private String  doctPosi;
	
	//评分
	private int score;
	//评分人数
	private int sumScore;

	//平均评分
	private String avgComplex;
	//评分人数
	private int complexPeople; 
	
	//科室id
	private String doctDepaid;
	private String doctHospid;
	
	
	
	//医院  listHosp
	private String hospName;
	//科室  listDepa
	private String depaName;
	//简介
	private String docIntroduction;
	//评分
	private String complex;
	//控制是否能预约挂号 如果是4 不能预约
	private int isCanAppoiment;
	//医生能否咨询
	private int isCanCons;			//  等于1的时候,此医生能被在线咨询  0 不可以 
		
	//医生能否电话预约
		private int isCanPhonePay;    //等于1的时候,此医生能电话咨询  0 不可以

	//预约状态
    private	int doctSchetype;
    
    private int  evalCount; //评论总数
    private int  docScoreAvg;// 总分
    private String money;   //电话咨询的 咨询费用
	private int regfee;		//预约挂号的挂号
	
	private int remainNum;
	
	
	private String attachFileByte;

	private int costType;
	public static String ATTACHFILEBYTE="attachFileByte";
	public static String ISCANPHONEPAY="isCanPhonePay"; 
	public static String ISCANCONS="isCanCons";
	public static String MONEY="money";
    public static String REGFEE="regfee";
	public static String AVGCOMPLEX="avgComplex";
	public static String HOSPNAME="doctHospName";
	public static String DEPANAME="doctDepaName";
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
	public static String ISCANAPPOIMENT="isCanAppoiment";
	public static String SCORE = "score";
	public static String DOCTSCHETYPE = "doctSchetype";
	public static String SCORENUM = "sumScore";

	
	
	
	public int getCostType() {
		return costType;
	}
	public void setCostType(int costType) {
		this.costType = costType;
	}
	public int getRemainNum() {
		return remainNum;
	}
	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}
	public String getAttachFileByte() {
		return attachFileByte;
	}
	public void setAttachFileByte(String attachFileByte) {
		this.attachFileByte = attachFileByte;
	}
	public int getIsCanCons() {
		return isCanCons;
	}
	public void setIsCanCons(int isCanCons) {
		this.isCanCons = isCanCons;
	}
	public int getIsCanPhonePay() {
		return isCanPhonePay;
	}
	public void setIsCanPhonePay(int isCanPhonePay) {
		this.isCanPhonePay = isCanPhonePay;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public int getEvalCount() {
		return evalCount;
	}
	public void setEvalCount(int evalCount) {
		this.evalCount = evalCount;
	}
	public int getDocScoreAvg() {
		return docScoreAvg;
	}
	public void setDocScoreAvg(int docScoreAvg) {
		this.docScoreAvg = docScoreAvg;
	}
	public int getRegfee() {
		return regfee;
	}
	public void setRegfee(int regfee) {
		this.regfee = regfee;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
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
	public int getIsCanAppoiment() {
		return isCanAppoiment;
	}
	public void setIsCanAppoiment(int isCanAppoiment) {
		this.isCanAppoiment = isCanAppoiment;
	}
	public int getDoctSchetype() {
		return doctSchetype;
	}
	public void setDoctSchetype(int doctSchetype) {
		this.doctSchetype = doctSchetype;
	}
	public int getSumScore() {
		return sumScore;
	}
	public void setSumScore(int sumScore) {
		this.sumScore = sumScore;
	}

	
}
