package com.chzh.fitter.download;

/**
 * @author jarrahwu
 * 下载进度监听器
 */
public interface DownloadProgressListener {
	/**
	 * 已经下载的大小
	 * @param size
	 */
	public void onDownloadSize(int size);
}
