package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class DynamicData {
	
	@JProperty(key="ptotal")
	int commentCount;
	
	@JProperty(key="zantotal")
	int favorCount;
	
	@JProperty(key="createtime")
	String postTime;
	
	@JProperty(key="content")
	String content;

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(int favorCount) {
		this.favorCount = favorCount;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
