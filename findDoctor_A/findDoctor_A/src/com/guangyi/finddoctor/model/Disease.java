package com.guangyi.finddoctor.model;

import java.io.Serializable;

public class Disease implements Serializable{
    /**
	 * 
	 */
	/**������ĸ*/
	public String index;
	private static final long serialVersionUID = 1L;
	//id
	private int  id;
	//����
	private String name;
	//����
	private String aliases;
	
	//��Ⱥ
		private String person;
		
		//����ʷ
		private String familyHistory;
		
		//�÷���ȥ
		private String outline;
		
		//����
		private String pathogen;
		
		//ʵ���ҽ��
		private String laboratory;
		
		//��ʳ����
		private String dietary;
		
		//�˶�����
		private String exercise;
		
		//��ҩ����
		private String medication;
		
		//��������
		private String rehabilitation;
		//֢״��ǩ
		private String symptomsLabel;
		
		//Ȩ��
//		private Long weights;
		
		//֢״�ֶ�
		private String symptoms;

		//��λ
		private String location;
		
		//����
		private String descript;
		
		//����id
		private String hdeptid;
		
		//��������
		private String hdeptName;

	
		
		
		 public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		//id
		public static String ID="id";
		//����
		public static String NAME="name";
		//����
		public static String ALIASES="aliases";
		
		//��Ⱥ
		public static String PERSON="person";
			
			//����ʷ
		public static String FAMILYHISTORY="familyHistory";
			
			//�÷���ȥ
		public static String OUTLINE="outline";
			
			//����
		public static String PATHOGEN="pathogen";
			
			//ʵ���ҽ��
		public static String LABORATORY="laboratory";
			
			//��ʳ����
		public static String DIETARY="dietary";
			
			//�˶�����
		public static String EXERCISE="exercise";
			
			//��ҩ����
		public static String MEDICATION="medication";
			
			//��������
		public static String REHABILITATION="rehabilitation";
			//֢״��ǩ
		public static String SYMPTOMSLABEL="symptomsLabel";
			
			//Ȩ��
		public static String WEIGHTS="weights";
			
			//֢״�ֶ�
		public static String SYMPTOMS="symptoms";

			//��λ
		public static String LOCATION="location";
			
			//����
		public static String DESCRIPT="descript";
			
			//����id
		public static String HDEPTID="hdeptid";
			
			//��������
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
