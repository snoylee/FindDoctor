package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class Assay implements Serializable{
	
	/**
	 * 
	 */
	/**������ĸ*/
	public String index;
	private static final long serialVersionUID = 1L;
	private String name;    //����
	private String introduce; //����
	private String normalValue; // ����ֵ
	private String sense;  //  �ٴ�
	private String notes; // ע������
	private String createtime; //����ʱ��
	private String otherassay; //��ؼ���
	private int id;
	
	
	public static String NAME="name";    //����
	public static String INTRODUCE="introduce"; //����
	public static String NORMALVALUE="normalValue"; // ����ֵ
	public static String SENSE="sense";  //  �ٴ�
	public static String NOTES="notes"; // ע������
	public static String CREATETIME="createtime"; //����ʱ��
	public static String OTHERASSAY="otherassay"; //��ؼ���
	public static String ID="id";
	
	
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getNormalValue() {
		return normalValue;
	}
	public void setNormalValue(String normalValue) {
		this.normalValue = normalValue;
	}
	public String getSense() {
		return sense;
	}
	public void setSense(String sense) {
		this.sense = sense;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getOtherassay() {
		return otherassay;
	}
	public void setOtherassay(String otherassay) {
		this.otherassay = otherassay;
	}
	
	


}
