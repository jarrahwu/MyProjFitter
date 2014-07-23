package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class SelfPlanData {
	@JProperty(key="pic1080")
	String img;
	
	@JProperty(key="title")
	String title;
	
	@JProperty(key="course_url")
	String courseSummaryUrl;
	
	@JProperty(key="createtime")
	String time;
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCourseSummaryUrl() {
		return courseSummaryUrl;
	}

	public void setCourseSummaryUrl(String courseSummaryUrl) {
		this.courseSummaryUrl = courseSummaryUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
