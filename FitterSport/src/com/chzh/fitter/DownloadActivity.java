package com.chzh.fitter;

import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import com.chzh.fitter.download.ProgressDownloader;
import com.chzh.fitter.framework.BaseActivity;

public class DownloadActivity extends BaseActivity {

	private ProgressBar progressBar;


	@Override
	protected void setupViews() {
		setContentView(R.layout.download_test);
		progressBar = (ProgressBar) this.findViewById(R.id.progress);
		bindClickEvent(findViewById(R.id.dojob), "onDownloadClick");


	}

	public void onDownloadClick(View v) {
		String path = "http://static.tripbe.com/videofiles/20121214/9533522808.f4v.mp4";
		new ProgressDownloader(this, progressBar).download(path, Environment.getExternalStorageDirectory());
	}


}
