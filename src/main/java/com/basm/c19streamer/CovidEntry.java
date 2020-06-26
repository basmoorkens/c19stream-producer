package com.basm.c19streamer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CovidEntry {

	@JsonProperty(value="DATE")
	private String date;
	
	@JsonProperty(value="PROVINCE")
	private String province;
	
	@JsonProperty(value="REGION")
	private String region;
	
	@JsonProperty(value="AGEGROUP")
	private String ageGroup;
	
	@JsonProperty(value="SEX")
	private char sex;
	
	@JsonProperty(value="CASES")
	private int cases;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public int getCases() {
		return cases;
	}

	public void setCases(int cases) {
		this.cases = cases;
	}
}
