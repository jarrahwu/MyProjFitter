package com.chzh.fitter.network;

import java.io.File;

import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.chzh.fitter.util.L;

public abstract class FileDownloadCallBack extends AjaxCallback<File> {
	
	@Override
	public void callback(String url, File object, AjaxStatus status) {
		if (status.getCode() == 200) {
			onFileDownloaded(url, object);
		}else {
			onFileDownloadError(status.getCode(), url);
		}
	}
	
	/**
	 * 文件下载完毕
	 * @param url
	 * @param object
	 */
	public abstract void onFileDownloaded(String url, File object);
	
	/**
	 * 文件下载出错
	 * @param httpCode
	 */
	public  void onFileDownloadError(int httpCode, String url) {
		L.red("下载出错了." + httpCode + " URL : " + url);
	}
}
