package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class DiseaseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String hdeptid; //科室id
	private String hdeptName;//科室名
	private String descript;//概述
	private String symptoms;//症状
	private String pathogen; //病因
	private String medication;//用药建议
	private String rehabilitation;//康复建议
	
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
	public String getHdeptid() {
		return hdeptid;
	}
	public void setHdeptid(String hdeptid) {
		this.hdeptid = hdeptid;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getSymptoms() {
		return symptoms;
	}
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	public String getPathogen() {
		return pathogen;
	}
	public void setPathogen(String pathogen) {
		this.pathogen = pathogen;
	}
	public String getMedication() {
		return medication;
	}
	public void setMedication(String medication) {
		this.medication = medication;
	}
	public String getRehabilitation() {
		return rehabilitation;
	}
	public void setRehabilitation(String rehabilitation) {
		this.rehabilitation = rehabilitation;
	}
	public String getHdeptName() {
		return hdeptName;
	}
	public void setHdeptName(String hdeptName) {
		this.hdeptName = hdeptName;
	}

}
