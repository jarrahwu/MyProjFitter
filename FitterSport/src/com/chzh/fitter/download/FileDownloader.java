package com.chzh.fitter.download;

import java.io.File;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.util.Log;

/**
 * 文件下载器
 */
public class FileDownloader {
	private static final String TAG = "FileDownloader";

	private Context mContext;

	private DownloadFileDBService mFileDBService;
	/**
	 * 已下载的文件长度
	 */
	private int mDownloadedSize = 0;

	/**
	 * 原始文件长度
	 */
	private int mFileSize = 0;

	/**
	 * 线程数组
	 */
	private DownloadThread[] mThreads;

	/**
	 * 本地保存的文件
	 */
	private File mSaveFile;

	/**
	 * 每个线程下载的缓存数据
	 */
	private Map<Integer, Integer> mDownloadCacheData = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 每条线程下载的长度
	 */
	private int mBlock;

	/**
	 * 下载的路径
	 */
	private String mDownloadUrl;


	/**
	 * 获取线程数
	 */
	public int getThreadSize() {
		return mThreads.length;
	}
	/**
	 * 获取文件大小
	 * @return
	 */
	public int getFileSize() {
		return mFileSize;
	}
	/**
	 * 累计已下载大小
	 * @param size
	 */
	protected synchronized void append(int size) {
		mDownloadedSize += size;
	}
	/**
	 * 更新指定线程最后下载的位置
	 * @param threadId 线程id
	 * @param pos 最后下载的位置
	 */
	protected synchronized void update(int threadId, int pos) {
		this.mDownloadCacheData.put(threadId, pos);
		this.mFileDBService.update(this.mDownloadUrl, this.mDownloadCacheData);
	}
	/**
	 * 构建文件下载器
	 * @param downloadUrl 下载路径
	 * @param fileSaveDir 文件保存目录
	 * @param threadNum 下载线程数
	 */
	public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum) {
		try {
			this.mContext = context;
			this.mDownloadUrl = downloadUrl;
			mFileDBService = new DownloadFileDBService(this.mContext);
			URL url = new URL(this.mDownloadUrl);
			if(!fileSaveDir.exists()) fileSaveDir.mkdirs();
			this.mThreads = new DownloadThread[threadNum];
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", downloadUrl);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			printResponseHeader(conn);
			if (conn.getResponseCode()==200) {
				this.mFileSize = conn.getContentLength();//根据响应获取文件大小
				if (this.mFileSize <= 0) throw new RuntimeException("Unkown file size ");

				String filename = getFileName(conn);//获取文件名称
				this.mSaveFile = new File(fileSaveDir, filename);//构建保存文件
				Map<Integer, Integer> logdata = mFileDBService.getData(downloadUrl);//获取下载记录
				if(logdata.size()>0){//如果存在下载记录
					for(Map.Entry<Integer, Integer> entry : logdata.entrySet())
						mDownloadCacheData.put(entry.getKey(), entry.getValue());//把各条线程已经下载的数据长度放入data中
				}
				if(this.mDownloadCacheData.size()==this.mThreads.length){//下面计算所有线程已经下载的数据长度
					for (int i = 0; i < this.mThreads.length; i++) {
						this.mDownloadedSize += this.mDownloadCacheData.get(i+1);
					}
					print("已经下载的长度"+ this.mDownloadedSize);
				}
				//计算每条线程下载的数据长度
				this.mBlock = (this.mFileSize % this.mThreads.length)==0? this.mFileSize / this.mThreads.length : this.mFileSize / this.mThreads.length + 1;
			}else{
				throw new RuntimeException("server no response ");
			}
		} catch (Exception e) {
			print(e.toString());
			throw new RuntimeException("don't connection this url");
		}
	}
	/**
	 * 获取文件名
	 */
	private String getFileName(HttpURLConnection conn) {
		String filename = this.mDownloadUrl.substring(this.mDownloadUrl.lastIndexOf('/') + 1);
		if(filename==null || "".equals(filename.trim())){//如果获取不到文件名称
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);
				if (mine == null) break;
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
					if(m.find()) return m.group(1);
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//默认取一个文件名
		}
		return filename;
	}

	/**
	 *  开始下载文件
	 * @param listener 监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
	 * @return 已下载文件大小
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception{
		try {
			RandomAccessFile randOut = new RandomAccessFile(this.mSaveFile, "rw");
			if(this.mFileSize>0) randOut.setLength(this.mFileSize);
			randOut.close();
			URL url = new URL(this.mDownloadUrl);
			if(this.mDownloadCacheData.size() != this.mThreads.length){
				this.mDownloadCacheData.clear();
				for (int i = 0; i < this.mThreads.length; i++) {
					this.mDownloadCacheData.put(i+1, 0);//初始化每条线程已经下载的数据长度为0
				}
			}
			for (int i = 0; i < this.mThreads.length; i++) {//开启线程进行下载
				int downLength = this.mDownloadCacheData.get(i+1);
				if(downLength < this.mBlock && this.mDownloadedSize<this.mFileSize){//判断线程是否已经完成下载,否则继续下载
					this.mThreads[i] = new DownloadThread(this, url, this.mSaveFile, this.mBlock, this.mDownloadCacheData.get(i+1), i+1);
					this.mThreads[i].setPriority(7);
					this.mThreads[i].start();
				}else{
					this.mThreads[i] = null;
				}
			}
			this.mFileDBService.save(this.mDownloadUrl, this.mDownloadCacheData);
			boolean notFinish = true;//下载未完成
			while (notFinish) {// 循环判断所有线程是否完成下载
				Thread.sleep(900);
				notFinish = false;//假定全部线程下载完成
				for (int i = 0; i < this.mThreads.length; i++){
					if (this.mThreads[i] != null && !this.mThreads[i].isFinish()) {//如果发现线程未完成下载
						notFinish = true;//设置标志为下载没有完成
						if(this.mThreads[i].getDownLength() == -1){//如果下载失败,再重新下载
							this.mThreads[i] = new DownloadThread(this, url, this.mSaveFile, this.mBlock, this.mDownloadCacheData.get(i+1), i+1);
							this.mThreads[i].setPriority(7);
							this.mThreads[i].start();
						}
					}
				}
				if(listener!=null) listener.onDownloadSize(this.mDownloadedSize);//通知目前已经下载完成的数据长度
			}
			//download finish
			mFileDBService.delete(this.mDownloadUrl);
		} catch (Exception e) {
			print(e.toString());
			throw new Exception("file download fail");
		}
		return this.mDownloadedSize;
	}
	
	/**
	 * 获取Http响应头字段
	 * @param http
	 * @return
	 */
	public  Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null) break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}
	/**
	 * 打印Http头字段
	 * @param http
	 */
	public  void printResponseHeader(HttpURLConnection http){
		Map<String, String> header = getHttpResponseHeader(http);
		for(Map.Entry<String, String> entry : header.entrySet()){
			String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";
			print(key+ entry.getValue());
		}
	}

	private  void print(String msg){
		Log.w(TAG, msg);
//		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}
	
	public void closeDb() {
		mFileDBService.closeDb();
	}
}
