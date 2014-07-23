package com.chzh.fitter;

import com.chzh.fitter.framework.SimpleTitleSActivity;

public  abstract class WebTest extends SimpleTitleSActivity{

//	private WebView mWebView;
//
//	@Override
//	protected void setupGUI() {
//		mWebView = findView(R.id.web, WebView.class);
//		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
//					String decode = "";
//					try {
//						decode = URLDecoder.decode(url, "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}
//					L.red(decode);
//			}
//		});
//
//		mWebView.setWebChromeClient( new WebChromeClient() {
//
//		});
//
//		mWebView.loadUrl("http://192.168.3.144/user/personal");
//	}
//
//	@Override
//	protected String getTitleName() {
//		return "私人定制";
//	}
//
//	@Override
//	protected int getLayoutRes() {
//		return R.layout.activity_custom_schedule;
//	}


}
