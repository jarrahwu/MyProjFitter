package com.chzh.fitter.view;

import net.simonvt.numberpicker.NumberPicker;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;

public class EditView extends BaseView implements IEditMode{

	private int min;
	private int max;
	private NumberPicker mPicker;
	private String lastValue;

	public EditView(Context context) {
		super(context);
	}

	public EditView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditView(Context context, String title) {
		this(context);
		findView(R.id.title, TextView.class).setText(title);
		refreshDrawableState();
	}
	
	
	public EditView(Context context, String title, String lastValue, int min, int max) {
		this(context, title);
		this.min = min;
		this.max = max;
		this.lastValue = lastValue;
		upDataUi();
		refreshDrawableState();
	}


	@Override
	public void setupViews() {
		setContentView(R.layout.edit_view);
	}

	@Override
	public String getEditValue() {
		String ret = "";
		if (findView(R.id.edit_text).getVisibility() == View.GONE) {
			ret = "" + mPicker.getValue();
		}else {
			ret = findView(R.id.edit_text, EditText.class).getText().toString().trim();
		}
		return ret;
	}
	
	public void upDataUi() {
		findView(R.id.edit_text, EditText.class).setText(lastValue);
		mPicker = findView(R.id.picker, NumberPicker.class);
		mPicker.setMinValue(min);
		mPicker.setMaxValue(max);
	}

	public NumberPicker getPicker() {
		return mPicker;
	}

	public View getEditText() {
		return findView(R.id.edit_text);
	}

	public void setInitPickerValue() {
		float lastValue = Float.valueOf(this.lastValue);
		int value = (int) lastValue;
		mPicker.setValue(value);
	};
}
