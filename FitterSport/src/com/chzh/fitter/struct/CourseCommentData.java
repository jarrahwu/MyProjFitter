package com.chzh.fitter.struct;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseCommentData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CourseActionData> commentList;
	
	public CourseCommentData(ArrayList<CourseActionData> list) {
		commentList = list;
	}

	public ArrayList<CourseActionData> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<CourseActionData> commentList) {
		this.commentList = commentList;
	}
	
	
}
