package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class ActionHistoryData {
	
	@JProperty(key="title")
	String title;
	
	@JProperty(key="pic")
	String actionIconUrl;
	
	/**
	 * 动作类型
	 */
	@JProperty(key="sporttype")
	int actionType;
	
	@JProperty(key="sport")
	int actionAmount;
	
	@JProperty(key="finish")
	int finish;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActionIconUrl() {
		return actionIconUrl;
	}

	public void setActionIconUrl(String actionIconUrl) {
		this.actionIconUrl = actionIconUrl;
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

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}
	
}
