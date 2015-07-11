package sol.demo.seegeek;

import java.util.Timer;
import java.util.TimerTask;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	public static final String TAG = "SEEGEEK";
	private XWalkView mXWalkView;
	private boolean isExit = false;

	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02"; // "gcj02";"bd09ll";"bd09";
	private int span = 15000;

	private final String SG_CONFIG_PATH = Environment
			.getExternalStorageDirectory() + "/SeeGeek";
	private final String SG_CONFIG_FILE = "sg_config.xml";
	private final String ASSETSPAGE = "file:///android_asset/default.html";
	private String mainUrl = "file:///android_asset/default.html";
    private final int GETMAINURL = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AndJs aj = new AndJs(this);
		mXWalkView = (XWalkView) findViewById(R.id.xView);
		mXWalkView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mXWalkView.addJavascriptInterface(aj, "AndJs");
		mXWalkView.requestFocus();
		mXWalkView.setResourceClient(new SeeGeekViewClient(mXWalkView));

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			String livingStream = bundle.getString("livingStream");
			if (livingStream != null) {
				Log.e(TAG, "livingStream : " + livingStream);
				Toast.makeText(this, "livingStream : " + livingStream,
						Toast.LENGTH_SHORT).show();
				mXWalkView.load(livingStream, null);
			} else {
				Log.e(TAG, "no livingStream");
				Toast.makeText(this, "no livingStream", Toast.LENGTH_SHORT)
						.show();
				mXWalkView.load(mainUrl, null);
			}
		} else {
			mLocationClient = new LocationClient(this.getApplicationContext());
			mLocationClient.registerLocationListener(new MyLocationListener());
			initBaiduLocation();
			mLocationClient.start();
			
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Utils.getConfigFromServer(SG_CONFIG_PATH, SG_CONFIG_FILE);
					Log.e(TAG, "getConfigFromServer");
				}
			}).start();

			mainUrl = Utils.getUrl(SG_CONFIG_PATH, SG_CONFIG_FILE);
			if (mainUrl == null) {
				mXWalkView.load(ASSETSPAGE, null);
				new Thread(new Runnable() {
					public void run() {
						while (mainUrl == null) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							mainUrl = Utils.getUrl(SG_CONFIG_PATH, SG_CONFIG_FILE);
						}
						Log.e(TAG, "getUrl: " + mainUrl);
						Message message = new Message();
	                    message.what = GETMAINURL;
	                    myHandler.sendMessage(message);
					}
				}).start();
			} else {
				Toast.makeText(this, "server is " + mainUrl, Toast.LENGTH_SHORT)
				.show();
				mXWalkView.load(mainUrl, null);
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
               switch (msg.what) {
                    case GETMAINURL:
						Log.e(TAG, "load: " + mainUrl);
                    	mXWalkView.load(mainUrl, null);
                    	break;
                    default:
                    	break;
               }
               super.handleMessage(msg);
          }
     };

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
		mLocationClient.stop();
		super.onStop();
	}

	private void initBaiduLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(TAG, "onKeyDown()" + keyCode + event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mXWalkView.getNavigationHistory().canGoBack()) {
				exitBy2Click();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exitBy2Click() {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			new Timer().schedule(new TimerTask() {
				public void run() {
					isExit = false;
				}
			}, 1000);
		} else {
			finish();
			System.exit(0);
		}
	}

	class SeeGeekViewClient extends XWalkResourceClient {
		public SeeGeekViewClient(XWalkView arg0) {
			super(arg0);
		}

		// shouldOverrideUrlLoading 控制新的连接在当前WebView中打开
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.e(TAG, "shouldOverrideUrlLoading " + url);
			view.loadUrl(url);
			return true;
		}

		public void onLoadFinished(XWalkView view, String url) {
			Log.e(TAG, "onLoadFinished " + url);
			if (url.equals(ASSETSPAGE)) {
				Log.e(TAG, "onLoadFinished:url is " + url);
				if (mainUrl != null) {
					Log.e(TAG, "onLoadFinished:mainUrl is " + mainUrl);
					super.onLoadStarted(view, mainUrl);
				}
			}
			super.onLoadFinished(view, url);
		}

		public void onLoadStarted(XWalkView view, String url) {
			Log.e(TAG, "onLoadStarted " + url);
			super.onLoadStarted(view, url);
		}
	}
}
