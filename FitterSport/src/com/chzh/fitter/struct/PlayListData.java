package com.chzh.fitter.struct;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayListData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CourseActionData> list;

	public PlayListData(ArrayList<CourseActionData> list) {
		this.list = list;
	}

	public ArrayList<CourseActionData> getList() {
		return list;
	}

	public void setList(ArrayList<CourseActionData> list) {
		this.list = list;
	}
	
	
}
