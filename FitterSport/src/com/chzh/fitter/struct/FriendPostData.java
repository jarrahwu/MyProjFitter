package com.chzh.fitter.struct;

import java.io.Serializable;

import com.jarrah.json.JProperty;

public class FriendPostData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JProperty(key="uid")
	String uid;
	
	@JProperty(key="createtime")
	String createtime;
	
	@JProperty(key="content")
	String content;
	
	@JProperty(key="gscore")
	int gscore;
	
	@JProperty(key="nickname")
	String nickname;
	
	@JProperty(key="pic")
	String pic;
	
	@JProperty(key="fritotal")
	int friendCount;
	
	@JProperty(key="stitle")
	String actTitle;
	
	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGscore() {
		return gscore;
	}

	public void setGscore(int gscore) {
		this.gscore = gscore;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public String getActTitle() {
		return actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}
}
