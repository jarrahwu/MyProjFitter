package com.chzh.fitter.struct;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.util.JSONUtil;
import com.jarrah.json.JProperty;

public class SportLogData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JProperty(key="fid")
	private int courseId;
	
	@JProperty(key="id")
	private int planId;
	
	@JProperty(key="title")
	private String title;
	
	@JProperty(key="percent")
	private String percentage;
		
	@JProperty(key="title")
	private String courseTitle;
	
	@JProperty(key="etimes")
	private int exerciseDuration;
	
	@JProperty(key="pic")
	private String bannerBg;
	
	@JProperty(key="bgpic")
	private String background;
	
	@JProperty(key="custkll")
	private int calorie;
	
	@JProperty(key="score")
	private int score;
	
	private String effectArray;
	
	private String shareObj;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}


	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public int getExerciseDuration() {
		return exerciseDuration;
	}

	public void setExerciseDuration(int exerciseDuration) {
		this.exerciseDuration = exerciseDuration;
	}

	public String getBannerBg() {
		return bannerBg;
	}

	public void setBannerBg(String bannerBg) {
		this.bannerBg = bannerBg;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * post fid 对应 courseId, pid 对应 planId
	 * @return
	 */
	public String getCourseDetailUrl() {
		return GlobalConstant.SPORT_LOG_DETAIL;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}
	
	
	public String getEffectArray() {
		return effectArray;
	}

	public void setEffectArray(String effectArray) {
		this.effectArray = effectArray;
	}

	public void parseEffectArray(JSONObject obj, String key) {
		setEffectArray(JSONUtil.getJsonArrays(obj, key).toString());
	}

	public String getShareObj() {
		return shareObj;
	}

	public void setShareObj(String shareObj) {
		this.shareObj = shareObj;
	}
	
	public void parseShareObJ(JSONObject obj, String key) {
		setShareObj(JSONUtil.getString(obj, key));
	}
}
