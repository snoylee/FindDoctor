package com.guangyi.forDoctor.model;

import java.io.Serializable;

public class DoctorDiscuss  implements Serializable{
	

	//id
	private String  expertId;
	//���۱��� listEvalDoc
	private String commentName;
	//����   listEvalDoc
	private String content;
	//����ʱ�� listEvalDoc
	private String createTime;
	//��Դ   listEvalDoc
    private int type;
     //���� listEvalDoc 
    private int  score;
    
    public static String  EXPERTID="expertId";
    public static String COMMENTNAME="commentName";
    public static String CONTENT="content";
    public static String CREATETIME="createTime";
    public static String TYPE="type";
    public static String SCORE="score";
    
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
    
    
    
    
    
    
    

}