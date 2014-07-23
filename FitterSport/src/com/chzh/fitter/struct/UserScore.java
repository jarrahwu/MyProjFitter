package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class UserScore {
	
	@JProperty(key="title")
	private String title;
	
	@JProperty(key="total")
	private int total;
	
	@JProperty(key="pic")
	private String pic;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
}
