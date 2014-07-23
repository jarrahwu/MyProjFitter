package com.chzh.fitter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.chzh.fitter.adapter.CourseComentAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.struct.CourseCommentData;
import com.chzh.fitter.struct.PlayListData;
import com.chzh.fitter.struct.WXShareData;
import com.chzh.fitter.util.ImageUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.video.VideoPlayerActivity;
import com.chzh.fitter.view.BounceListView;
import com.jarrah.json.XSON;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class CourseCommentActivity extends SimpleTitleSActivity implements GlobalConstant{
	private BounceListView mList;
	private CourseComentAdapter mAdapter;
	private ArrayList<CourseActionData> mCourseList;
	
	protected WXShareData mWxShareData; //微信分享的数据
	private IWXAPI api;

	@Override
	protected void setupGUI() {
		mList = findView(R.id.action_list, BounceListView.class);
		mAdapter = new CourseComentAdapter(this);
		mAdapter.setData(getData());
		mList.setAdapter(mAdapter);
		
		bindClickEvent(findView(R.id.doAgain), "onDoAgainClick");
		bindClickEvent(findView(R.id.invite), "onInviteClick");
		bindClickEvent(findView(R.id.share), "onShareClick");
		
		sumbitCourseResult(mAdapter.getCourseCommentResult());
	}

	public void onDoAgainClick(View v) {
		PlayListData playListData = new PlayListData(mCourseList);
		skipTo("play_list", playListData, VideoPlayerActivity.class);
		finish();
	}
	
	public void onInviteClick(View v) {
		skipTo(AddFitterFriendActivity.class);
	}
	
	public void onShareClick(View v) {
		getBitmapAndShare(GlobalConstant.HOST_IP + mWxShareData.getPic());
	}

	/**
	 * 提交用户的数据
	 * 
	 * @param courseCommentResult
	 */
	private void sumbitCourseResult(ArrayList<CourseActionData> courseCommentResult) {
		
		JHttpManager httpManager = new JHttpManager(this);
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		//put data
		JSONArray array = new JSONArray();
		for (int i = 0; i < courseCommentResult.size(); i++) {
			CourseActionData curData = courseCommentResult.get(i);
			
			JSONObject obj = genCommentObj(curData.getPlayDuration(),
					curData.getFeelId(), curData.getPlanId(),
					curData.getActionId());
			array.put(obj);
		}
		
		data.put("elem", array);
		
		//request
		String url = USER_COURSE_LOG;
		httpManager.post(data, url,new CodeCallBack() {
					@Override
					public void handleCallBack(JSONObject obj) {
						L.red(CourseSummaryActivity.class, obj);
						mWxShareData = new WXShareData();
						mWxShareData = new XSON().fromJSON(mWxShareData, obj);
						showToast("已提交数据");
					}
				}, new UICore(this).getToken());
	}

	private JSONObject genCommentObj(float playDuration, int feelId,
			int planId, int actionId) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("etimes", playDuration);
			obj.put("effect", feelId);
			obj.put("pid", planId);
			obj.put("vid", actionId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	private JSONArray getData() {
		CourseCommentData cData = (CourseCommentData) getIntent()
				.getSerializableExtra("comment_list");
		
		if(cData == null) return null;
		
		mCourseList = cData.getCommentList();

		JSONArray array = new JSONArray();
		for (int i = 0; i < mCourseList.size(); i++) {
			array.put(new JSONObject());
		}
		mAdapter.setCourseCommentList(mCourseList);
		return array;
	}

	@Override
	protected String getTitleName() {
		return "课程评价";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_course_comment;
	}
	
	@Override
	protected void onDestroy() {
		sumbitCourseResult(mAdapter.getCourseCommentResult());
		super.onDestroy();
	}
	
	private void regToWx() {
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}
	
	private void shareWebPage(WXShareData data) {
		WXWebpageObject webObj = new WXWebpageObject();
		webObj.webpageUrl = data.getUrl();
		
		WXMediaMessage mediaMessage = new WXMediaMessage();
		mediaMessage.mediaObject = webObj;
		mediaMessage.title = data.getTitle();
		mediaMessage.description = data.getContent();
		
		Bitmap bmp = ImageUtil.getResizedImage(data.thumbImagePath, null, 100, true, 0);
		mediaMessage.setThumbImage(bmp);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = mediaMessage;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		
		api.sendReq(req);
	}

	/**
	 * 获取到缩略图然后分享
	 * @param url
	 * @return
	 */
	private void getBitmapAndShare(String url) {
		DownloadManager dm = new DownloadManager(this);
		dm.downloadFile(url, 60 * 60 * 1000, new FileDownloadCallBack() {
			@Override
			public void onFileDownloaded(String url, File object) {
				mWxShareData.thumbImagePath = object.getAbsolutePath();
				regToWx();
				shareWebPage(mWxShareData);
			}
		});
	}
}
