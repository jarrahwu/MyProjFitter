package com.chzh.fitter.data;

import java.io.Serializable;

import com.jarrah.json.JProperty;

public class SingleAction implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2902753805926195864L;

	@JProperty(key = "action_id")
	private int actionId;

	@JProperty(key = "action_title")
	private String actionTitle;

	@JProperty(key = "action_count")
	private String actounCount;

	@JProperty(key = "action_preview")
	private int videoPreview;

	@JProperty(key = "action_introduce")
	private int videoIntroduce;

	@JProperty(key = "action_main")
	private int video;

	@JProperty(key = "action_image")
	private int actionImage;

	@JProperty(key = "action_description")
	private String actionDescription;


	public SingleAction() {

	}

	public SingleAction(int actionId, String actionTitle, String actionCount,
			int actionImage, int videoPreview, int videoIntroduce, int video) {
		this.actionId = actionId;
		this.actionTitle = actionTitle;
		this.actounCount = actionCount;
		this.actionImage = actionImage;
		this.videoPreview = videoPreview;
		this.videoIntroduce = videoIntroduce;
		this.video = video;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}

	public String getActounCount() {
		return actounCount;
	}

	public void setActounCount(String actounCount) {
		this.actounCount = actounCount;
	}

	public int getVideoPreview() {
		return videoPreview;
	}

	public void setVideoPreview(int videoPreview) {
		this.videoPreview = videoPreview;
	}

	public int getVideoIntroduce() {
		return videoIntroduce;
	}

	public void setVideoIntroduce(int videoIntroduce) {
		this.videoIntroduce = videoIntroduce;
	}

	public int getVideo() {
		return video;
	}

	public void setVideo(int video) {
		this.video = video;
	}

	public int getActionImage() {
		return actionImage;
	}

	public void setActionImage(int actionImage) {
		this.actionImage = actionImage;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

}
