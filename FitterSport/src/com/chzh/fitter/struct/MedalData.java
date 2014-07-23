package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class MedalData {
	@JProperty(key = "id")
	private int id;

	@JProperty(key = "title")
	private String title;

	@JProperty(key = "introl")
	private String introduce;

	@JProperty(key = "pic")
	private String img;

	@JProperty(key = "finish")
	private int finishStatus;

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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(int finishStatus) {
		this.finishStatus = finishStatus;
	}

}
