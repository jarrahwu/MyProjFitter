package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class LevelData {
	
	@JProperty(key="id")
	int id;
	
	@JProperty(key="introl")
	String introduce;
	
	@JProperty(key="title")
	String title;
	
	@JProperty(key="pic")
	String img;
	
	@JProperty(key="finish")
	int finishStatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
