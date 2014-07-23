package com.jw.sample;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jarrah.blur.Blur;
import com.jarrah.blur.ImageUtils;
import com.jw.core.AbstractListCell;
import com.jw.core.BaseActivity;
import com.jw.core.BaseDataAdapter;

public class MainActivity extends BaseActivity implements OnMenuItemClickListener {
	
	private static final int ACTIVITY_BACKGROUND_RES = R.drawable.beach;//本地的图片
	
	private static final String BG_FILE = "/bg.png"; //没有高斯模糊效果的图片
	private static final String BLURRED_BG_FILE = "/blurred_bg.png";//模糊效果的图片
	
	private static final int ACTION_NORMAL = 1;
	private static final int ACTION_BLUR = 2;
	private static final int ACTION_PADDING = 3;
	
	private ListView mListView;
	private BlurListAdapter mAdapter;

	@Override
	protected void setupViews() {
		setContentView(R.layout.activity_main);
		setBackground();
		initListView();
	}
	
	
	private void initListView() {
		mListView = findView(R.id.blurList, ListView.class);
		mAdapter = new BlurListAdapter(this);
		mListView.setAdapter(mAdapter);
		
		//读取测试数据
		mAdapter.setData(getTestData());
	}

	/**
	 * 读取Asserts 里面的数据
	 * @return
	 */
	private JSONArray getTestData() {
		JSONArray array = new JSONArray();
		for (int i = 0; i < 50; i++) {
			array.put(new JSONObject());
		}
		return array;
	}

	@SuppressLint("NewApi")
	private void setBackground() {
		
		//检查文件是否存在
		File bgFile = new File(getFilesDir() + BG_FILE);
		File blurredFile = new File(getFilesDir() + BLURRED_BG_FILE);
		
		if (!bgFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ACTIVITY_BACKGROUND_RES);
			ImageUtils.storeImage(bitmap, bgFile);
		}
		
		if (!blurredFile.exists()) {
//			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ACTIVITY_BACKGROUND_RES);
			
//			Bitmap bluredBitmap = Blur.fastblur(this, scaleBitmap, 20);
//			ImageUtils.storeImage(bluredBitmap, blurredFile);
		}
		
		findView(R.id.layoutContainer, ViewGroup.class).setBackgroundResource(ACTIVITY_BACKGROUND_RES);
	}
	
	//Adapter
	class BlurListAdapter extends BaseDataAdapter {

		public BlurListAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void configureItem(AbstractListCell itemView) {
			
		}

		@Override
		public AbstractListCell makeItemCell(int postion) {
			return new BlurItemCell(mContext);
		}
	}
	
	//Item
	class BlurItemCell extends AbstractListCell {

		public BlurItemCell(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			findView(R.id.cellText, TextView.class).setText("the list item at " + mPosition);
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.blur_list_cell);
		}
	}
	
	@SuppressLint("NewApi")
	@Override public boolean onCreateOptionsMenu(Menu menu) 
	    { 
	        super.onCreateOptionsMenu(menu); 
	        MenuItem normal = menu.add(0, ACTION_NORMAL, 0, "normal"); 
	        MenuItem blur = menu.add(0, ACTION_BLUR, 1, "blur"); 
	        MenuItem padding = menu.add(0, ACTION_PADDING, 1, "padding");
	        
	        normal.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
	        blur.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
	        padding.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        
	        normal.setOnMenuItemClickListener(this);
	        blur.setOnMenuItemClickListener(this);
	        padding.setOnMenuItemClickListener(this);
	        return true; 
	    }

	
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case ACTION_NORMAL:
			setBackgroundNormal();
			clearListBackground();
			break;
			
		case ACTION_BLUR:
			setBackgroundBlurred();
			clearListBackground();
			break;

		case ACTION_PADDING:
			setPaddingEffect(400);
			break;
		}
		return true;
	}

	private void clearListBackground() {
		mListView.setBackgroundResource(android.R.color.transparent);
	}

	@SuppressLint("NewApi")
	private void setPaddingEffect(int padding) {
		View container = findView(R.id.layoutContainer);
		container.setPadding(0, padding, 0, 0);
		Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir() + BLURRED_BG_FILE);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		bitmapDrawable.setGravity(Gravity.BOTTOM);
		mListView.setBackground(bitmapDrawable);
	}

	@SuppressLint("NewApi")
	private void setBackgroundBlurred() {
		Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir() + BLURRED_BG_FILE);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		findView(R.id.layoutContainer, ViewGroup.class).setBackground(bitmapDrawable);
	}

	@SuppressLint("NewApi")
	private void setBackgroundNormal() {
		Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir() + BG_FILE);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		findView(R.id.layoutContainer, ViewGroup.class).setBackground(bitmapDrawable);
	} 
}
