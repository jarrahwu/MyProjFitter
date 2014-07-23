package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class WXShareData {
	
	@JProperty(key="content")
	private String content;
	
	@JProperty(key="title")
	private String title;
	
	@JProperty(key="pic")
	private String pic;
	
	@JProperty(key="url")
	private String url;
	
	public String thumbImagePath;// 通过pic 字段获取的bitmap路径

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
