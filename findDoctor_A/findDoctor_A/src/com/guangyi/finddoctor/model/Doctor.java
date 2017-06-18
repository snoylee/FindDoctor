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
	//�ó� 
	private String  doctorPosition;
	//ְλ
	private String  doctPosi;
	
	//����
	private int score;
	//��������
	private int sumScore;

	//ƽ������
	private String avgComplex;
	//��������
	private int complexPeople; 
	
	//����id
	private String doctDepaid;
	private String doctHospid;
	
	
	
	//ҽԺ  listHosp
	private String hospName;
	//����  listDepa
	private String depaName;
	//���
	private String docIntroduction;
	//����
	private String complex;
	//�����Ƿ���ԤԼ�Һ� �����4 ����ԤԼ
	private int isCanAppoiment;
	//ҽ���ܷ���ѯ
	private int isCanCons;			//  ����1��ʱ��,��ҽ���ܱ�������ѯ  0 ������ 
		
	//ҽ���ܷ�绰ԤԼ
		private int isCanPhonePay;    //����1��ʱ��,��ҽ���ܵ绰��ѯ  0 ������

	//ԤԼ״̬
    private	int doctSchetype;
    
    private int  evalCount; //��������
    private int  docScoreAvg;// �ܷ�
    private String money;   //�绰��ѯ�� ��ѯ����
	private int regfee;		//ԤԼ�ҺŵĹҺ�
	
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
