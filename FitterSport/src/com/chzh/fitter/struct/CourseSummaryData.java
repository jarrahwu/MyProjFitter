package com.chzh.fitter.struct;

import java.io.Serializable;

import com.jarrah.json.JProperty;

/**
 * 具体的健身方案的数据
 */
public class CourseSummaryData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JProperty(key="fpic")
	private String pic;
	
	@JProperty(key="fpic1080")
	private String pic1080;
	
	@JProperty(key="fbgpic")
	private String courseBg;
	
	@JProperty(key="url")
	private String actionsUrl;//动作列表的URL
	
	@JProperty(key="ftitle")
	private String courseTitle;
	
	@JProperty(key="fid")
	private int courseId;
	
	@JProperty(key="lock")
	private boolean isLock;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getActionsUrl() {
		return actionsUrl;
	}

	public void setActionsUrl(String courseUrl) {
		this.actionsUrl = courseUrl;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseBg() {
		return courseBg;
	}

	public void setCourseBg(String courseBg) {
		this.courseBg = courseBg;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public String getPic1080() {
		return pic1080;
	}

	public void setPic1080(String pic1080) {
		this.pic1080 = pic1080;
	}
}
