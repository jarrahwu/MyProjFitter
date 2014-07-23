package com.chzh.fitter.network;

import java.io.File;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 下载管理器 只是用文件缓存
 */
public class DownloadManager {
	
	private Context mContext;
	
	private AQuery mAQuery;

	public DownloadManager(Context context) {
		mContext = context;
		mAQuery = new AQuery(mContext);
	}
	
	/**
	 * 下载文件
	 * @param url 下载的url
	 * @param expire 文件缓存的时间 0 的时候就是一直使用缓存
	 * @param callback  
	 */
	public void downloadFile(String url, long expire, FileDownloadCallBack callback) {
		mAQuery.ajax(url, File.class, expire, callback);
	}
	
	/**
	 * 下载文件
	 * @param url 下载的url
	 * @param expire 文件缓存的时间 0 的时候就是一直使用缓存
	 * @param callback  
	 */
	public void downloadFileWithProgress(String url, long expire, ProgressBar progressBar, AjaxCallback<File> callback) {
		progressBar.setMax(0);
		progressBar.invalidate();
		mAQuery.ajax(url, File.class, expire, callback);
	}

	public void cancelDownload() {
		mAQuery.ajaxCancel();
	}
	
}
