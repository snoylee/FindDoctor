package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class Disease implements Serializable{
    /**
	 * 
	 */
	/**索引字母*/
	public String index;
	private static final long serialVersionUID = 1L;
	//id
	private int  id;
	//名称
	private String name;
	//别名
	private String aliases;
	
	//人群
		private String person;
		
		//家族史
		private String familyHistory;
		
		//好发人去
		private String outline;
		
		//病因
		private String pathogen;
		
		//实验室结果
		private String laboratory;
		
		//饮食建议
		private String dietary;
		
		//运动建议
		private String exercise;
		
		//用药建议
		private String medication;
		
		//康复建议
		private String rehabilitation;
		//症状标签
		private String symptomsLabel;
		
		//权重
//		private Long weights;
		
		//症状字段
		private String symptoms;

		//部位
		private String location;
		
		//概述
		private String descript;
		
		//科室id
		private String hdeptid;
		
		//科室名称
		private String hdeptName;

	
		
		
		 public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		//id
		public static String ID="id";
		//名称
		public static String NAME="name";
		//别名
		public static String ALIASES="aliases";
		
		//人群
		public static String PERSON="person";
			
			//家族史
		public static String FAMILYHISTORY="familyHistory";
			
			//好发人去
		public static String OUTLINE="outline";
			
			//病因
		public static String PATHOGEN="pathogen";
			
			//实验室结果
		public static String LABORATORY="laboratory";
			
			//饮食建议
		public static String DIETARY="dietary";
			
			//运动建议
		public static String EXERCISE="exercise";
			
			//用药建议
		public static String MEDICATION="medication";
			
			//康复建议
		public static String REHABILITATION="rehabilitation";
			//症状标签
		public static String SYMPTOMSLABEL="symptomsLabel";
			
			//权重
		public static String WEIGHTS="weights";
			
			//症状字段
		public static String SYMPTOMS="symptoms";

			//部位
		public static String LOCATION="location";
			
			//概述
		public static String DESCRIPT="descript";
			
			//科室id
		public static String HDEPTID="hdeptid";
			
			//科室名称
		public static String HDEPTNAME="hdeptName";

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

		public String getAliases() {
			return aliases;
		}

		public void setAliases(String aliases) {
			this.aliases = aliases;
		}

		public String getPerson() {
			return person;
		}

		public void setPerson(String person) {
			this.person = person;
		}

		public String getFamilyHistory() {
			return familyHistory;
		}

		public void setFamilyHistory(String familyHistory) {
			this.familyHistory = familyHistory;
		}

		public String getOutline() {
			return outline;
		}

		public void setOutline(String outline) {
			this.outline = outline;
		}

		public String getPathogen() {
			return pathogen;
		}

		public void setPathogen(String pathogen) {
			this.pathogen = pathogen;
		}

		public String getLaboratory() {
			return laboratory;
		}

		public void setLaboratory(String laboratory) {
			this.laboratory = laboratory;
		}

		public String getDietary() {
			return dietary;
		}

		public void setDietary(String dietary) {
			this.dietary = dietary;
		}

		public String getExercise() {
			return exercise;
		}

		public void setExercise(String exercise) {
			this.exercise = exercise;
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

		public String getSymptomsLabel() {
			return symptomsLabel;
		}

		public void setSymptomsLabel(String symptomsLabel) {
			this.symptomsLabel = symptomsLabel;
		}

		public String getSymptoms() {
			return symptoms;
		}

		public void setSymptoms(String symptoms) {
			this.symptoms = symptoms;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getDescript() {
			return descript;
		}

		public void setDescript(String descript) {
			this.descript = descript;
		}

		public String getHdeptid() {
			return hdeptid;
		}

		public void setHdeptid(String hdeptid) {
			this.hdeptid = hdeptid;
		}

		public String getHdeptName() {
			return hdeptName;
		}

		public void setHdeptName(String hdeptName) {
			this.hdeptName = hdeptName;
		}
		
		
		
	

}
