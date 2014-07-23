package com.jw.sample;

import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;

import com.jarrah.blur.Blur;
import com.jarrah.blur.ImageUtils;
import com.jw.core.BaseActivity;

public class PaddingActivity extends BaseActivity {

	private static final String BG_FILE = "/padding_bg.png";
	private static final String BLURRED_BG_FILE = "/padding_blurred_bg.png";
	private static final int ACTIVITY_BACKGROUND_RES = R.drawable.ship;

	@Override
	protected void setupViews() {
		setContentView(R.layout.activity_padding);
		setBackground();
	}

	@SuppressLint("NewApi")
	private void setBackground() {

		// 检查文件是否存在
		File bgFile = new File(getFilesDir() + BG_FILE);
		File blurredFile = new File(getFilesDir() + BLURRED_BG_FILE);

		if (!bgFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					ACTIVITY_BACKGROUND_RES);
			ImageUtils.storeImage(bitmap, bgFile);
		}

		if (!blurredFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					ACTIVITY_BACKGROUND_RES);
			Bitmap bluredBitmap = Blur.fastblur(this, bitmap, 117);
			ImageUtils.storeImage(bluredBitmap, blurredFile);
		}

		findView(R.id.layoutContainer, ViewGroup.class).setBackgroundResource(
				ACTIVITY_BACKGROUND_RES);
	}
}
