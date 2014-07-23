package com.chzh.fitter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.chzh.fitter.adapter.InfoAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.util.JSONUtil;

public class InfoDetailActivity extends SimpleTitleSActivity {

	public static final int TYPE_STATUS = 0;
	public static final int TYPE_LEVEL  = 1;
	public static final int TYPE_SCORE  = 2;
	public static final int TYPE_MEDAL  = 3;

	private ListView mListView;
	private InfoAdapter mAdapter;
	private int mType;

	@Override
	protected void setupGUI() {
		mListView = findView(R.id.info_list, ListView.class);
		setInfoModel();
	}

	/**
	 * 根据intent的类型,显示不同的布局
	 */
	private void setInfoModel() {
		mType = getIntent().getIntExtra("info_type", -1);
		switch (mType) {
		case TYPE_STATUS:// 状态查询
			gone(R.id.level);
			gone(R.id.medal);
			setHeaderInfo();
			setStatus();
			//设置列表title
			setListTitle("状态查询", R.drawable.ic_status_tag);
			break;

		case TYPE_LEVEL:// 等级查询
			gone(R.id.status_parent);
			gone(R.id.medal);
//			findView(R.id.list_title, TextView.class).setText(" 等级说明: ");
			setListTitle("等级说明", R.drawable.ic_level_tag);
			setHeaderInfo();
			break;

		case TYPE_SCORE:// 积分查询
			gone(R.id.medal);
			gone(R.id.header);
			gone(R.id.list_title);
			setHeaderInfo();
			break;

		case TYPE_MEDAL:// 勋章查询
			gone(R.id.level);
			gone(R.id.status_parent);
			setHeaderInfo();
//			findView(R.id.list_title, TextView.class).setText("勋章说明: ");
			setListTitle("勋章查询", R.drawable.ic_medal_tag);
			break;
			
		default:
			break;
		}
		setListAdapter(mType);
	}

	private void setListTitle(String text, int drawableLeft) {
		TextView tv = findView(R.id.list_title, TextView.class);
		tv.setText(text);
		tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
		tv.setCompoundDrawablePadding((int) DensityUtil.convertDpToPixel(4));
	}

	private void setStatus() {
		
	}

	private void setMedal() {
		ajaxImage(findView(R.id.medal, ImageView.class), GlobalConstant.HOST_IP + mAdapter.getMedalUrl());
	}

	private void setHeaderInfo() {
		XUser info = new UICore(this).getUserFromPreference();
		String url = GlobalConstant.HOST_IP + info.getPortrait();
		ajaxCornerImage(findView(R.id.info_portrait, ImageView.class), url, (int)DensityUtil.convertDpToPixel(10));
		
		findView(R.id.name, TextView.class).setText(info.getNickName());
		findView(R.id.status, TextView.class).setText(info.getActiveStatus());
	}

	public void setListAdapter(int type) {
		mAdapter = new InfoAdapter(this, type);
		mListView.setAdapter(mAdapter);
		setDataByType(type);
	}

	private void setDataByType(int type) {
		queryInfo(type);
	}

	
	private void queryInfo(int type) {
		String[] queryUrls = new String[4];
		queryUrls[TYPE_LEVEL]  = GlobalConstant.USER_HOST + "level";
		queryUrls[TYPE_MEDAL]  = GlobalConstant.USER_HOST + "medal";
		queryUrls[TYPE_SCORE]  = GlobalConstant.USER_HOST + "score";
		queryUrls[TYPE_STATUS] = GlobalConstant.USER_HOST + "state";
		
		JHttpManager httpManager = new JHttpManager(this);
		String cookie = new UICore(this).getToken();
		httpManager.get(queryUrls[type], new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				JSONArray elem = JSONUtil.getJsonArrays(obj, "elem");
				mAdapter.setData(elem);
				
				//
				if (mType == TYPE_MEDAL) {
					setMedal();
				}
			}
		}, cookie);
	}
	
	

	@Override
	protected String getTitleName() {
		return getIntent().getStringExtra("title");
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_info_detail;
	}

}
