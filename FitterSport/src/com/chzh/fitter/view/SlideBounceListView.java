package com.chzh.fitter.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class SlideBounceListView extends BounceListView implements OnScrollListener{

	
	private View mHeader;
	private HeaderStickyListener mHeaderStickyListener;
	
	public SlideBounceListView(Context context) {
		super(context);
	}

	public SlideBounceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlideBounceListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void enableScrollStickyHeader(View header) {
		mHeader = header;
		setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem != 0) {
				mHeader.setVisibility(View.VISIBLE);
				dispatchHeaderStickyTop();
			}else {
				mHeader.setVisibility(View.GONE);
				dispatchHeaderScroll();
			}
	}
	
	private void dispatchHeaderStickyTop() {
		if (mHeaderStickyListener != null) {
			mHeaderStickyListener.onHeaderStickyTop();
		}
	}
	
	private void dispatchHeaderScroll() {
		if (mHeaderStickyListener != null) {
			mHeaderStickyListener.onHeaderScroll();
		}
	}
	
	public void setHeaderStickyListener(HeaderStickyListener headerStickyListener) {
		mHeaderStickyListener = headerStickyListener;
	}
	
	public interface HeaderStickyListener {
		void onHeaderStickyTop();
		void onHeaderScroll();
	}
}
