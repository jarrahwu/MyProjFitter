package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.util.JSONUtil;

public class CourseCommentItemView extends BaseDataItemView {

	private ImageView mFeelNormal, mFeelGood, mFeelBad;
	private boolean   isNormalSelected, isGoodSelected, isBadSelected;
	private OnFeelSelectedLisener mOnFeelSelectedLisener;

	public CourseCommentItemView(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
//		int actionImage = JSONUtil.getInt(data, "action_image");
//		String actionName = JSONUtil.getString(data, "action_name");
//		String actionCount = JSONUtil.getString(data, "action_count");
		int feelId = JSONUtil.getInt(data, "feel_id");

//		findView(R.id.action_img, ImageView.class).setImageResource(actionImage);
//		findView(R.id.action_name, TextView.class).setText(actionName);
//		findView(R.id.action_count, TextView.class).setText(actionCount);
		selectFeel(feelId);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_action_comment);
		mFeelNormal = findView(R.id.feel_normal, ImageView.class);
		mFeelGood = findView(R.id.feel_good, ImageView.class);
		mFeelBad = findView(R.id.feel_bad, ImageView.class);

		bindClickEvent(mFeelBad, "onBadClick");
		bindClickEvent(mFeelGood, "onGoodClick");
		bindClickEvent(mFeelNormal, "onNormalClick");
	}

	public void onBadClick(View v) {
		selectFeel(2);
	}

	public void onGoodClick(View v) {
		selectFeel(1);
	}

	public void onNormalClick(View v) {
		selectFeel(0);
	}

	// 0 ~ 2
	public void selectFeel(int feelId) {
		switch (feelId) {
		case 0:
			toggoleNormal();
			break;

		case 1:
			toggoleGood();
			break;

		case 2:
			toggoleBad();
			break;

		default:
			break;
		}

		if (mOnFeelSelectedLisener != null) {
			mOnFeelSelectedLisener.onFeelSelected(mPosition, feelId);
		}
	}

	public void toggoleNormal() {
		dimAll();
		if (isNormalSelected) {
			mFeelNormal.setImageResource(R.drawable.ic_feel_normal);
		}else {
			mFeelNormal.setImageResource(R.drawable.ic_feel_normal_sel);
		}
		isBadSelected = !isBadSelected;
	}

	public void toggoleGood() {
		dimAll();
		if (isGoodSelected) {
			mFeelGood.setImageResource(R.drawable.ic_feel_good);
		}else {
			mFeelGood.setImageResource(R.drawable.ic_feel_good_sel);
		}
		isBadSelected = !isBadSelected;
	}

	public void toggoleBad() {
		dimAll();
		if (isBadSelected) {
			mFeelBad.setImageResource(R.drawable.ic_feel_bad);
		}else {
			mFeelBad.setImageResource(R.drawable.ic_feel_bad_sel);
		}
		isBadSelected = !isBadSelected;
	}

	/**
	 * 取消所有高亮
	 */
	public void dimAll() {
		isNormalSelected = false;
		isGoodSelected = false;
		isBadSelected = false;
		mFeelNormal.setImageResource(R.drawable.ic_feel_normal);
		mFeelGood.setImageResource(R.drawable.ic_feel_good);
		mFeelBad.setImageResource(R.drawable.ic_feel_bad);
	}

	public interface OnFeelSelectedLisener {
		void onFeelSelected(int position, int feelId);
	}

	public void setOnFeelSelectedListener(OnFeelSelectedLisener l) {
		mOnFeelSelectedLisener = l;
	}

	public void setCommentData(CourseActionData courseActionData) {
		ajaxImage(findView(R.id.action_img, ImageView.class), GlobalConstant.HOST_IP + courseActionData.getActionIcon());
		findView(R.id.action_name, TextView.class).setText(courseActionData.getActionTitle());
		findView(R.id.action_count, TextView.class).setText("" + courseActionData.getPlayDuration() + "s");
	}

}
