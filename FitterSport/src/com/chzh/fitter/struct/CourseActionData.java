package com.chzh.fitter.struct;

import java.io.Serializable;

import com.jarrah.json.JProperty;

/**
 * @author jarrahwu
 * 课程动作数据
 */
public class CourseActionData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JProperty(key="fid")
	private int planId;//方案Id
	
	@JProperty(key="introl")
	private String actionDescription;
	
	@JProperty(key="title")
	private String actionTitle;
	
	@JProperty(key="pic1080")
	private String actionIcon;
	
	@JProperty(key="url")
	private String mainViedoUrl;
	
	@JProperty(key="surl")
	private String videoPreviewUrl;
	
	@JProperty(key="eurl")
	private String videoIntroduceUrl;
	
	@JProperty(key="image")
	private String restPic;
	
	@JProperty(key="id")
	private int actionId;
	
	
	//这3个用来保存下载完毕时候文件保存的路径
	private String mainVideoFilePath;
	private String previewFilePath;
	private String introduceFileParh;
	
	/**
	 * 动作类型
	 */
	@JProperty(key="sporttype")
	private int actionType;
	
	@JProperty(key="sport")
	private int actionAmount;
	
	
	/**
	 * 动作播放的时长
	 */
	private float playDuration;
	
	private int feelId;//0-2 0 normal 1 good 2 bad
	
	private String backgroundUrl;
	
	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}

	public String getActionIcon() {
		return actionIcon;
	}

	public void setActionIcon(String actionIcon) {
		this.actionIcon = actionIcon;
	}

	public String getMainViedoUrl() {
		return mainViedoUrl;
	}

	public void setMainViedoUrl(String mainViedoUrl) {
		this.mainViedoUrl = mainViedoUrl;
	}

	public String getVideoPreviewUrl() {
		return videoPreviewUrl;
	}

	public void setVideoPreviewUrl(String videoPreviewUrl) {
		this.videoPreviewUrl = videoPreviewUrl;
	}

	public String getVideoIntroduceUrl() {
		return videoIntroduceUrl;
	}

	public void setVideoIntroduceUrl(String videoIntroduceUrl) {
		this.videoIntroduceUrl = videoIntroduceUrl;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getActionAmount() {
		return actionAmount;
	}

	public void setActionAmount(int actionAmount) {
		this.actionAmount = actionAmount;
	}

	public String getRestPic() {
		return restPic;
	}

	public void setRestPic(String restPic) {
		this.restPic = restPic;
	}

	public String getMainVideoFilePath() {
		return mainVideoFilePath;
	}

	public void setMainVideoFilePath(String mainVideoFilePath) {
		this.mainVideoFilePath = mainVideoFilePath;
	}

	public String getPreviewFilePath() {
		return previewFilePath;
	}

	public void setPreviewFilePath(String previewFilePath) {
		this.previewFilePath = previewFilePath;
	}

	public String getIntroduceFilePath() {
		return introduceFileParh;
	}

	public void setIntroduceFileParh(String introduceFileParh) {
		this.introduceFileParh = introduceFileParh;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public float getPlayDuration() {
		return playDuration;
	}

	public void setPlayDuration(float playDuration) {
		this.playDuration = playDuration;
	}

	public int getFeelId() {
		return feelId;
	}

	public void setFeelId(int feelId) {
		this.feelId = feelId;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
}
