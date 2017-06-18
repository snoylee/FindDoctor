package com.guangyi.forDoctor.model;

import java.io.Serializable;

public class Evaluate implements Serializable{
	
	private String evaluateId;
	private String evaluateContext;
	private String evaluateTime;
	private int efficiency;
	private int service;
	private int skill;
	private String consProblem;
	private int consId;
	
	
public static String EVALUATEID="evaluateId";
public static String EVALUATECONTEXT="evaluateContext";
public static String EVALUATETIME="evaluateTime";
public static String EFFICIENCY="efficiency";
public static String SERVICE="service";
public static String SKILL="skill";
public static String CONSPROBLEM="consProblem";
public static String CONSID="consId";



public int getConsId() {
	return consId;
}
public void setConsId(int consId) {
	this.consId = consId;
}
public String getConsProblem() {
	return consProblem;
}
public void setConsProblem(String consProblem) {
	this.consProblem = consProblem;
}
public String getEvaluateId() {
	return evaluateId;
}
public void setEvaluateId(String evaluateId) {
	this.evaluateId = evaluateId;
}
public String getEvaluateContext() {
	return evaluateContext;
}
public void setEvaluateContext(String evaluateContext) {
	this.evaluateContext = evaluateContext;
}
public String getEvaluateTime() {
	return evaluateTime;
}
public void setEvaluateTime(String evaluateTime) {
	this.evaluateTime = evaluateTime;
}
public int getEfficiency() {
	return efficiency;
}
public void setEfficiency(int efficiency) {
	this.efficiency = efficiency;
}
public int getService() {
	return service;
}
public void setService(int service) {
	this.service = service;
}
public int getSkill() {
	return skill;
}
public void setSkill(int skill) {
	this.skill = skill;
}


}
