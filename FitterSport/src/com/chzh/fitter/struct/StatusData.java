package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class StatusData {
	
	@JProperty(key = "id")
	private int id;

	@JProperty(key = "title")
	private String title;

	@JProperty(key = "introl")
	private String introduce;

	@JProperty(key = "state")
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
