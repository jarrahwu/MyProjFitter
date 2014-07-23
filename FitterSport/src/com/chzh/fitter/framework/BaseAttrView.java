package com.chzh.fitter.framework;

import com.chzh.fitter.util.DensityUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * 自定义属性View基类
 */
public abstract class BaseAttrView extends BaseView{

	protected TypedArray mTypedArray;

	public BaseAttrView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		if(mAttrs != null)
			mTypedArray = getTypedArrayById(mAttrs);
	}

	/**
	 * 重写这个方法,并返回相对应的typedArray
	 * context.obtainStyledAttributes(attr, res_id);//TypedArray是一个数组容器,用完需要recycle()一下
	 * @param context
	 * @return
	 */
	protected abstract TypedArray getTypedArrayById(AttributeSet attrs);

	/**
	 * 返回的数据是dip单位转换为像素
	 * @param array_index
	 * @return
	 */
	protected float getDimention(int array_index) { //默认大小5dip
		float d =  mTypedArray.getDimensionPixelSize(array_index, (int) DensityUtil.convertDpToPixel(5));
		return d;
	}

	protected int getColor(int array_index) { //默认白色
		int defaultColor = Color.rgb(0xff, 0xff, 0xff);
		return mTypedArray.getColor(array_index, defaultColor);
	}

	protected Drawable getDrawable(int array_index) {
		return mTypedArray.getDrawable(array_index);
	}

	protected String getString(int array_index) {
		return mTypedArray.getString(array_index);
	}
}
