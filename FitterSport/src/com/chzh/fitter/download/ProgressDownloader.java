package com.chzh.fitter.download;

import java.io.File;
import java.lang.ref.WeakReference;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ProgressDownloader {

	private ProgressBar mProgressBar;

	private Context mContext;

	private ProgressHandler handler;

	private static final int DOWNLOADING = 1;
	private static final int DOWNLOAD_ERROR = -1;

	private static final int DOWNLOAD_THREAD_COUNT = 10;


	public ProgressDownloader(Context context, ProgressBar progressBar) {
		mContext = context;
		mProgressBar = progressBar;
		handler = new ProgressHandler(context, progressBar);
	}

	// 主线程(UI线程)
	// 业务逻辑正确，但是该程序运行的时候有问题
	// 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
	public void download(final String path, final File savedir) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileDownloader loader = new FileDownloader(mContext, path,
						savedir, DOWNLOAD_THREAD_COUNT);
				mProgressBar.setMax(loader.getFileSize());// 设置进度条的最大刻度为文件的长度
				try {
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {// 实时获知文件已经下载的数据长度
							Message msg = new Message();
							msg.what = DOWNLOADING;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);// 发送消息
						}
					});
				} catch (Exception e) {
					handler.obtainMessage(DOWNLOAD_ERROR).sendToTarget();
				}
			}
		}).start();
	}

	public static class ProgressHandler extends Handler {

		private WeakReference<ProgressBar> mReference;

		private Context theContext;

		public ProgressHandler(Context context, ProgressBar bar) {
			mReference = new WeakReference<ProgressBar>(bar);
			theContext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADING:
				mReference.get().setProgress(msg.getData().getInt("size"));
//				float num = (float) mReference.get().getProgress()
//						/ (float) mReference.get().getMax();
				// int result = (int) (num * 100);
				// resultView.setText(result + "%");
				if (mReference.get().getProgress() == mReference.get().getMax()) {
					Toast.makeText(theContext, "文件下载成功.", Toast.LENGTH_LONG).show();
				}
				break;

			case DOWNLOAD_ERROR:
				Toast.makeText(theContext, "下载出错.", Toast.LENGTH_LONG).show();
				break;
			}

		}
	}
	
}
