package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class DoctorDiscuss  implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//id
	private String  expertId;
	//评价标题 listEvalDoc
	private String commentName;
	//内容   listEvalDoc
	private String content;
	//评价时间 listEvalDoc
	private String createTime;
	//来源   listEvalDoc
    private int type;
     //评分 listEvalDoc 
    private int  score;
    //标题
    private String evalDocTitle;
    private int doctorId;
    
    private  String doctName;
    

    public static  String DOCTNAME="doctName";
    
	public static String  EXPERTID="expertId";
    public static String COMMENTNAME="commentName";
    public static String CONTENT="content";
    public static String CREATETIME="createTime";
    public static String TYPE="type";
    public static String SCORE="score";
    public static String EVALDOCTITLE ="evalDocTitle";
    public static String DOCTORID = "doctorId";
    
    
    
    
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getCommentName() {
		return commentName;
	}
	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
    
    
    public String getEvalDocTitle() {
		return evalDocTitle;
	}
	public void setEvalDocTitle(String evalDocTitle) {
		this.evalDocTitle = evalDocTitle;
	}
    
    
    
    

}
