package com.chzh.fitter;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.data.SharedPreferencesHelper;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.framework.XWindowManager;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.CameraUtil;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.util.FileUtil;
import com.chzh.fitter.util.ImageUtil;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.InfoTextView;
import com.chzh.fitter.view.PopupButtonView;
import com.chzh.fitter.view.PopupButtonView.OnPopupItemClickListener;
import com.chzh.fitter.view.RoundImageView;

public class UserActivity extends SimpleTitleSActivity implements GlobalConstant, OnCheckedChangeListener{

	//request codes
	public static final int EDIT_NICK = 0xaa;
	public static final int EDIT_PORTRAIT = EDIT_NICK + 1;
	public static final int EDIT_HEIGHT = EDIT_PORTRAIT + 1;
	public static final int EDIT_WEIGHT = EDIT_HEIGHT + 1;
	public static final int EDIT_AGE = EDIT_WEIGHT + 1;
	public static final int EDIT_PORTRAIT_BY_LOCAL_PHOTOS = EDIT_AGE +1;

	//the photo popup
	private Dialog mPortraitPopup;

	//modified data 2 post
	private HashMap<String, Object> mData2post;

	private XUser mUserInfo;
	private UICore mUICore;

	@Override
	protected void setupGUI() {
		mData2post = new HashMap<String, Object>();
		bindClicks(R.id.portrait, R.id.nick, R.id.age, R.id.weight, R.id.height);
		mUICore = new UICore(this);
		setUserDataFromLocal();
	}

	private void setUserDataFromLocal() {
		mUserInfo = mUICore.getUserFromPreference();
		
		findView(R.id.nick, InfoTextView.class).setRightText(mUserInfo.getNickName());
		findView(R.id.age, InfoTextView.class).setRightText("" + mUserInfo.getAge());
		findView(R.id.weight, InfoTextView.class).setRightText("" + mUserInfo.getWeight());
		findView(R.id.height, InfoTextView.class).setRightText("" + mUserInfo.getHeight());

		findView(R.id.fitter_account, InfoTextView.class).setRightText("" + mUserInfo.getMoblie());
		String url = HOST_IP + mUserInfo.getPortrait();
		findView(R.id.portrait, RoundImageView.class).ajaxAutoOrientationImage(url);
		
		int gender = mUserInfo.getGender();
		boolean isFemale = gender == 0 ? false : true;
		findView(R.id.gender, Switch.class).setChecked(isFemale);
		
		findView(R.id.bmi, InfoTextView.class).setRightText("" + mUserInfo.getBmi());
		
		findView(R.id.gender, Switch.class).setOnCheckedChangeListener(this);
	}

	@Override
	protected String getTitleName() {
		return "个人资料";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_user;
	}


	public void bindClicks(int... ids) {
		for (int id : ids) {
			bindClickEvent(findViewById(id), "clickToEdit");
		}
	}

	/**
	 * info edit click event trigger
	 * @param v
	 */
	public void clickToEdit(View v) {
		InfoTextView infoTextView = null;
		String value = "";
		if(v instanceof InfoTextView){
		 infoTextView = (InfoTextView) v;
		 value = infoTextView.getRightText();
		}
		 
		switch (v.getId()) {
		case R.id.portrait:
			showPortraitMenu();
			break;

		case R.id.nick:
			skipToEditForResult(EDIT_NICK, value);
			break;

		case R.id.age:
			skipToEditForResult(EDIT_AGE, value);
			break;

		case R.id.weight:
			skipToEditForResult(EDIT_WEIGHT, value);
			break;

		case R.id.height:
			skipToEditForResult(EDIT_HEIGHT, value);
			break;

		default:
			break;
		}
	}

	/**
	 *显示头像更换方式的popupwindow
	 */
	private void showPortraitMenu() {
		initInflater();
		PopupButtonView contentView = new PopupButtonView(this, "相机拍照", "选取照片");
		mPortraitPopup = XWindowManager.createPopupDialog(this, contentView);
		XWindowManager.showDialog(mPortraitPopup);

		contentView.setOnPopupItemClickListener(new OnPopupItemClickListener() {

			@Override
			public void onItemClick(View v, int position) {
				switch (position) {
				case 0: //照相机
					File photoFile = FileUtil.getPortraitFile();
					CameraUtil cUtil = new CameraUtil(UserActivity.this);
					cUtil.openCamera(UserActivity.this, EDIT_PORTRAIT, photoFile);
					XWindowManager.dismissDialog(mPortraitPopup);
					break;

				case 1: //图库选取图片
					CameraUtil util = new CameraUtil(UserActivity.this);
					util.openPhotos(UserActivity.this, EDIT_PORTRAIT_BY_LOCAL_PHOTOS);
					XWindowManager.dismissDialog(mPortraitPopup);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * Activity跳转
	 * @param cls
	 */
	public void skipToEditForResult(int requestCode, String value) {
		Intent i = new Intent();
		i.setClass(this, EditActivity.class);
		i.putExtra("edit_mode", requestCode);
		i.putExtra("last_value", value); 
		startActivityForResult(i, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String dataKey = EditActivity.BACK_KEY;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case EDIT_PORTRAIT: //拍照返回
				String path = FileUtil.getPhotoFilePath();
				int target = DensityUtil.dip2px(70);
				Bitmap btm = ImageUtil.getResizedImage(path, null, target, true, 0);
				btm = ImageUtil.autoFixOrientation(btm, null, null, path);
				findView(R.id.portrait, RoundImageView.class).setImageBitmap(btm);
				
				String imgPath = FileUtil.saveFile(btm, FileUtil.IMAGE_DIR + "/", "tempHeader.jpg", true);
				//multipart modify user portrait
				modifyPortrait(imgPath);
				break;

			case EDIT_PORTRAIT_BY_LOCAL_PHOTOS://图库返回
				L.red(data.getData());
				RoundImageView imageView = findView(R.id.portrait, RoundImageView.class);
				String picturePath = CameraUtil.getPhotoPathByLocalUri(this, data);
				int theTarget = DensityUtil.dip2px(70);
				Bitmap localBtm = ImageUtil.getResizedImage(picturePath, null, theTarget, true, 0);
				localBtm = ImageUtil.autoFixOrientation(localBtm, null, null, picturePath);
				imageView.setImageBitmap(localBtm);
				//multipart modify user portrait
				String imgPath1 = FileUtil.saveFile(localBtm, FileUtil.IMAGE_DIR + "/", "tempHeader.jpg", true);
				modifyPortrait(imgPath1);
				break;

			case EDIT_AGE:
				print("age", ""+data.getIntExtra(dataKey, -1));
				modifyUserInfo("age", data.getIntExtra(dataKey, -1), null);
				break;

			case EDIT_HEIGHT:
				print("height", ""+data.getFloatExtra(dataKey, 0.0f));
				modifyUserInfo("height", data.getFloatExtra(dataKey, 0.0f), null);
				break;

			case EDIT_NICK:
				print("nick", data.getStringExtra(dataKey));
				modifyUserInfo("nickname", data.getStringExtra(dataKey), null);
				break;

			case EDIT_WEIGHT:
				print("weight", "" + data.getFloatExtra(dataKey, 0.0f));
				modifyUserInfo("weight", data.getFloatExtra(dataKey, 0.0f), null);
				break;



			default:
				break;
			}
		}
	}

	public void modifyUserInfo(final String key, final Object value, final String preferenceKey) {
		final SharedPreferencesHelper helper = new SharedPreferencesHelper(this);
		helper.openOrCreateSharedPreferences(USER_PREFERENCE);

		JHttpManager httpManager = new JHttpManager(this);
		final String cookie = mUICore.getToken();

		mData2post.put(key, value);
		httpManager.post(mData2post, USER_MODIFY, new CodeCallBack() {

			@Override
			public void handleCallBack(JSONObject obj) {
				showToast(key + " 修改成功");
				String theKey = preferenceKey == null ? key : preferenceKey;
				helper.modify(theKey, value);
				setUserDataFromLocal();
			}
		}, cookie);
	}

	public void modifyPortrait(String path) {
		JHttpManager httpManager = new JHttpManager(this);
		final String cookie = mUICore.getToken();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("pic", path);
		httpManager.multiPart(USER_MODIFY, data, cookie, new CodeCallBack() {

			@Override
			public void handleCallBack(JSONObject obj) {
				L.red(obj);
				String pic = JSONUtil.getString(obj, "pic");
				SharedPreferencesHelper helper = new SharedPreferencesHelper(mContext);
				helper.openOrCreateSharedPreferences(USER_PREFERENCE);
				helper.modify("portrait", pic);
				showToast("头像更换成功");
			}
		});
	}

	public void print(String from, String data) {
		System.out.println(from + " : " + data);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int gender = isChecked ? 1 : 0;
		modifyUserInfo("sex", gender, "gender");
	}
}
