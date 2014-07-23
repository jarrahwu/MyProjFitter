package com.chzh.fitter;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.util.L;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button shareBtn, regBtn;
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_share);
        
    	api = WXAPIFactory.createWXAPI(this, GlobalConstant.APP_ID, false);

    	regBtn = (Button) findViewById(R.id.btnRegApp);
    	regBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    api.registerApp(GlobalConstant.APP_ID);    	
			}
		});
    	
    	shareBtn = (Button) findViewById(R.id.btnShareImage);
    	shareBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WXWebpageObject webObj = new WXWebpageObject();
				webObj.webpageUrl = "http://admin.togoalad.com/web/share.php";
				
				WXMediaMessage mediaMessage = new WXMediaMessage();
				mediaMessage.mediaObject = webObj;
				mediaMessage.title = "web page title";
				mediaMessage.description = "web page desc";
				
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = String.valueOf(System.currentTimeMillis());
				req.message = mediaMessage;
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
				
				api.sendReq(req);
			}
		});
    	
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		L.red("onReq");
//		switch (req.getType()) {
//		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();		
//			break;
//		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
//			break;
//		default:
//			break;
//		}
	}

	// µÚÈý·½Ó¦ÓÃ·¢ËÍµ½Î¢ÐÅµÄÇëÇó´¦ÀíºóµÄÏìÓ¦½á¹û£¬»á»Øµ÷µ½¸Ã·½·¨
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "ok";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "cancel";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "auth canceled";
			break;
		default:
			result = "unknow";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
//	private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
//	}
	
}
