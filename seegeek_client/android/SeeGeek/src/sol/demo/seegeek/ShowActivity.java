package sol.demo.seegeek;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class ShowActivity extends Activity {
	public static final String TAG = "SEEGEEK";
	private static XWalkView mXWalkView;
	private NetStatusReceiver netStatusReceiver;
	private AlertDialog dialogNoNet, dialogNetMobile;

	protected final String SGHOST = "58.53.219.69";
//	protected final String SGHOST = "124.126.126.19";
	protected final String webSocketUrl = "ws://" + SGHOST + ":5000/ws";
	protected final String baseShowUrl = "http://" + SGHOST + ":8081/seegeek/gsee/fullscreenplay.html?ItemId=";
	private final String BUNDLEKEY = "PushInfo";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		netStatusReceiver = new NetStatusReceiver();
		registerAllReceiver();
		
		AndJs aj = new AndJs(this);
		mXWalkView = (XWalkView) findViewById(R.id.xView);
		mXWalkView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mXWalkView.addJavascriptInterface(aj, "AndJs");
		mXWalkView.requestFocus();
		mXWalkView.setResourceClient(new SeeGeekViewClient(mXWalkView));

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			String itemId = bundle.getString(BUNDLEKEY);
			Log.e(TAG, "item Id : " + itemId);
			if (itemId != null) {
				mXWalkView.load(baseShowUrl + itemId, null);
			} else {
				Toast.makeText(this, "null item", Toast.LENGTH_SHORT)
						.show();
				mXWalkView.load(MainActivity.ASSETSPAGE, null);
			}
		} else {
			mXWalkView.load(MainActivity.ASSETSPAGE, null);
		}
	}

	private void showAlertDialogNoNet() {
		Log.i(TAG, "showAlertDialogNoNet");
		dialogNoNet = new AlertDialog.Builder(this).setCancelable(false)
				.setTitle("提示").setMessage("网络已断开，请确认网络已正确连接！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "no active network");
						onDestroy();
						finish();
						System.exit(0);
					}
				}).show();
	}

	private void showAlertDialogNetMobile() {
		Log.i(TAG, "showAlertDialogNetMobile");
		dialogNetMobile = new AlertDialog.Builder(this).setCancelable(false)
				.setTitle("提示").setMessage("非WIFI网络，是否继续？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "not wifi, continue");
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "not wifi, go home");
						onDestroy();
						finish();
						System.exit(0);
					}
				}).show();
	}

	private void registerAllReceiver() {
		IntentFilter filter1 = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(netStatusReceiver, filter1);
	}

	private void unRegisterAllReceiver() {
		try {
			this.unregisterReceiver(netStatusReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		Log.e(TAG, "onPause()");
		super.onPause();
		if (mXWalkView != null) {
			mXWalkView.pauseTimers();
			mXWalkView.onHide();
		}
	}

	@Override
	protected void onResume() {
		Log.e(TAG, "onResume()");
		super.onResume();
		if (mXWalkView != null) {
			mXWalkView.resumeTimers();
			mXWalkView.onShow();
		}
	}

	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy()");
		unRegisterAllReceiver();
		super.onDestroy();
		if (mXWalkView != null) {
			mXWalkView.onDestroy();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(TAG, "onActivityResult()");
		if (mXWalkView != null) {
			mXWalkView.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.e(TAG, "onNewIntent()");
		if (mXWalkView != null) {
			mXWalkView.onNewIntent(intent);
		}
	}

	protected void onStop() {
		Log.e(TAG, "onStop()");
		super.onStop();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

	class SeeGeekViewClient extends XWalkResourceClient {
		public SeeGeekViewClient(XWalkView arg0) {
			super(arg0);
		}

		// shouldOverrideUrlLoading 控制新的连接在当前WebView中打开
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onLoadFinished(XWalkView view, String url) {
			super.onLoadFinished(view, url);
		}

		public void onLoadStarted(XWalkView view, String url) {
			super.onLoadStarted(view, url);
		}
	}
	
	class NetStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeInfo = manager.getActiveNetworkInfo();
			if (activeInfo != null) {
				if (dialogNoNet != null) {
					if (dialogNoNet.isShowing())
						dialogNoNet.dismiss();
				}
				Log.i(TAG, "active network " + activeInfo.getTypeName());
				if (activeInfo.getTypeName().equals("mobile")) {
					if (dialogNetMobile != null) {
						if (!dialogNetMobile.isShowing()) {
							showAlertDialogNetMobile();
						}
					} else {
						showAlertDialogNetMobile();
					}
				} else {
					if (dialogNetMobile != null) {
						if (dialogNetMobile.isShowing()) {
							dialogNetMobile.dismiss();
						}
					}
				}
			} else {
				Log.i(TAG, "no active network");
				if (dialogNoNet != null) {
					if (!dialogNoNet.isShowing())
						showAlertDialogNoNet();
				} else {
					showAlertDialogNoNet();
				}
			}
		}
	};
}
