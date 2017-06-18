package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class DoctorConsult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ��ѯID
	private int consId;
	// id
	private int consDoctorId;

     private String expertId;
	// ר������
	private String expertName;

	// ��ѯ���� listCons
	private String consTitle;
	// ������ listCons
	private String consProblem;
	// �ش����� listCons
	private String consReplyProblem;
	// �ش���� listCons
	private String consReplyTitle;
	// ��ѯʱ�� listCons
	private String consTime;
	  //ҽ������
	private  String doctName;
	
	// ���� listCons
	private String score;
	
	private String consUserReply;
	
	private String attachFileByte;

	public static String ATTACHFILEBYTE="attachFileByte";
    
	
	public static String CONSUSERREPLY="consUserReply";
	public static String CONSID = "consId";
	public static String EXPERTNAME = "expertName";
	public static String EXPERTID = "expertId";
	public static String CONSTITLE = "consTitle";
	public static String CONSPROBLEM = "consProblem";
	public static String CONSREPLYPROBLEM = "consReplyProblem";
	public static String CONSREPLYTITLE = "consReplyTitle";
	public static String CONSTIME = "consTime";
	public static String SCORE = "score";
	public static String CONSDOCTORID = "consDoctorId";
	public static String DOCTNAME="doctName";
	
	
	
	
	
	public String getAttachFileByte() {
		return attachFileByte;
	}

	public void setAttachFileByte(String attachFileByte) {
		this.attachFileByte = attachFileByte;
	}

	public String getConsUserReply() {
		return consUserReply;
	}

	public void setConsUserReply(String consUserReply) {
		this.consUserReply = consUserReply;
	}

	public String getDoctName() {
		return doctName;
	}

	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public static String getCONSDOCTORID() {
		return CONSDOCTORID;
	}

	public static void setCONSDOCTORID(String cONSDOCTORID) {
		CONSDOCTORID = cONSDOCTORID;
	}

	public int getConsId() {
		return consId;
	}

	public void setConsId(int consId) {
		this.consId = consId;
	}
	public int getConsDoctorId() {
		return consDoctorId;
	}

	public void setConsDoctorId(int consDoctorId) {
		this.consDoctorId = consDoctorId;
	}
	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}


	public String getConsTitle() {
		return consTitle;
	}

	public void setConsTitle(String consTitle) {
		this.consTitle = consTitle;
	}

	public String getConsProblem() {
		return consProblem;
	}

	public void setConsProblem(String consProblem) {
		this.consProblem = consProblem;
	}

	public String getConsReplyProblem() {
		return consReplyProblem;
	}

	public void setConsReplyProblem(String consReplyProblem) {
		this.consReplyProblem = consReplyProblem;
	}

	public String getConsReplyTitle() {
		return consReplyTitle;
	}

	public void setConsReplyTitle(String consReplyTitle) {
		this.consReplyTitle = consReplyTitle;
	}

	public String getConsTime() {
		return consTime;
	}

	public void setConsTime(String consTime) {
		this.consTime = consTime;
	}

}
