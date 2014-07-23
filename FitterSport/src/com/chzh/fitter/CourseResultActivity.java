package com.chzh.fitter;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.struct.SportLogData;
import com.chzh.fitter.struct.WXShareData;
import com.chzh.fitter.util.ImageUtil;
import com.chzh.fitter.util.JSONUtil;
import com.jarrah.json.XSON;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * 课程成绩的activity
 *
 */
public class CourseResultActivity extends SimpleTitleSActivity implements GlobalConstant{

	private SportLogData mCourseResultData;
	private WXShareData mWxShareData;
	private IWXAPI api;

	@Override
	protected void setupGUI() {
		bindClickEvent(findView(R.id.schedule_detail), "gotoScheduleDetail");
		bindClickEvent(findView(R.id.share), "shareToWxMoment");
		updateUI();
	}

	private void updateUI() {
		String url = GlobalConstant.HOST_IP + mCourseResultData.getBannerBg();
		ajaxImage(findView(R.id.banner_bg, ImageView.class), url);
		
		findView(R.id.exercise_duration, TextView.class).setText("" + mCourseResultData.getExerciseDuration());
		findView(R.id.calorie, TextView.class).setText("" + mCourseResultData.getCalorie());
		findView(R.id.score, TextView.class).setText(""+ mCourseResultData.getScore());
//		findView(R.id.result_comment, TextView.class).setText(mCourseResultData.getEffectArray().toString());
		
		//setSpinner
		SpinnerAdapter adapter = new SpinnerAdapter(this);
		findView(R.id.effectSpinner, Spinner.class).setAdapter(adapter);
		adapter.setData(JSONUtil.createJSONOArray(mCourseResultData.getEffectArray()));
		
	}

	public void gotoScheduleDetail(View v) {
		skipTo("detail_data", mCourseResultData, CourseScheduleActivity.class);
	}

	public void shareToWxMoment(View v) {
		getBitmapAndShare(GlobalConstant.HOST_IP + mWxShareData.getPic());
	}

	@Override
	protected String getTitleName() {
		mCourseResultData = (SportLogData) getIntent().getSerializableExtra("result_data");
		JSONObject shareObj = JSONUtil.createJSONObject(mCourseResultData.getShareObj());
		mWxShareData = new WXShareData();
		mWxShareData = new XSON().fromJSON(mWxShareData, shareObj);
		return "课程成绩";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_course_result;
	}
	
	private class SpinnerAdapter extends BaseDataAdapter {

		public SpinnerAdapter(Context context) {
			super(context);
		}

		@Override
		public void configureItem(BaseDataItemView itemView) {
			SpinnerItem si = (SpinnerItem) itemView;
			si.setTitle(getTitle(itemView.getPosition()));
		}

		@Override
		public BaseDataItemView instanceItemView() {
			return new SpinnerItem(mContext);
		}
		
		public String getTitle(int postion) {
			String title = "N/A";
			try {
				title = mDataSource.getString(postion);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return title;
		}
	}
	
	private class SpinnerItem extends BaseDataItemView {

		
		public SpinnerItem(Context context) {
			super(context);
		}
		

		@Override
		public void onDispatchData(JSONObject data) {
			
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_spinner);
		}
		
		public void setTitle(String itemTitle) {
			findView(R.id.spinnerItemTitle, TextView.class).setText(itemTitle);
		}
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
