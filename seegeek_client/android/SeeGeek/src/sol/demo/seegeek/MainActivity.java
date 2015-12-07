package sol.demo.seegeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import sol.demo.seegeek.websocket.WebSocketConnection;
import sol.demo.seegeek.websocket.WebSocketConnectionHandler;
import sol.demo.seegeek.websocket.WebSocketException;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	public static final String TAG = "SEEGEEK";
	private Context mContext;
	private static XWalkView mXWalkView;
	private static WebSocketConnection ws;
	private NetStatusReceiver netStatusReceiver;
	private SocketStatusReceiver socketStatusReceiver;
	private NotificationManager mNotificationManager;
	private static TelephonyManager tm;
	private boolean isExit = false;
	private AlertDialog dialogNoNet, dialogNetMobile;
	private AlertDialog dialogSocketDisconnect;

	private LocationClient mLocationClient;
	private static BDLocation mLocation = new BDLocation();
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02"; // "gcj02";"bd09ll";"bd09";
	private int span = 15000;

	final static String SG_BASE_PATH = Environment
			.getExternalStorageDirectory() + "/SeeGeek";
	final static String SG_CONFIG_FILE = "sg_config.xml";
	final static String ASSETSPAGE = "file:///android_asset/default.html";
	protected final String SGHOST = "58.53.219.69";
	// protected final String SGHOST = "124.126.126.19";
	protected final String remoteConfigUrl = "http://" + SGHOST
			+ "/sg_config.xml";
	protected final String webSocketUrl = "ws://" + SGHOST + ":5000/ws";
	protected final String baseShowUrl = "http://" + SGHOST
			+ ":8081/seegeek/gsee/fullscreenplay.html?ItemId=";
	protected final String mainPage1 = "http://" + SGHOST
			+ ":8081/seegeek/gsee/discover.html";
	protected final String mainPage2 = "http://" + SGHOST
			+ ":8081/seegeek/gsee/my.html";
	private final String SGWEBSOCKET_ACTION = "Seegeek.WebSocket.ForPublishPush";
	private final String BUNDLEKEY = "PushInfo";
	public final static int MSG_MAINURL = 0;
	public final static int MSG_UPLOCATION = 1;
	private static String mainUrl;
	private List<Integer> mainHash;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		netStatusReceiver = new NetStatusReceiver();
		socketStatusReceiver = new SocketStatusReceiver();

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mainHash = new ArrayList<Integer>();
		mainHash.add(mainPage1.hashCode());
		mainHash.add(mainPage2.hashCode());

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		ws = new WebSocketConnection();
		initWebSocket();

		registerAllReceiver();

//		String testPush = "{\"ItemId\": \"555\", \"nickname\": \"昵称\","
//				+ "\"Location\": \"位置\", \"title\": \"标题\"}";
//		showNotification(testPush, R.drawable.notification64,
//				Notification.DEFAULT_ALL);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
		initBaiduLocation();
		mLocationClient.start();

		AndJs aj = new AndJs(this);
		mXWalkView = (XWalkView) findViewById(R.id.xView);
		mXWalkView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mXWalkView.addJavascriptInterface(aj, "AndJs");
		mXWalkView.requestFocus();
		mXWalkView.setResourceClient(new SeeGeekViewClient(mXWalkView));

		new Thread(new Runnable() {
			public void run() {
				while (!Utils.getConfigFromServer(remoteConfigUrl,
						SG_BASE_PATH, SG_CONFIG_FILE)) {
					Log.e(TAG, "getConfigFromServer");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		mainUrl = Utils.getUrl(SG_BASE_PATH, SG_CONFIG_FILE);
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
						mainUrl = Utils.getUrl(SG_BASE_PATH, SG_CONFIG_FILE);
					}
					Log.e(TAG, "getUrl: " + mainUrl);
					Message message = new Message();
					message.what = MSG_MAINURL;
					myHandler.sendMessage(message);
				}
			}).start();
		} else {
			mXWalkView.load(mainUrl, null);
		}
	}

	private void initWebSocket() {
		try {
			ws.connect(webSocketUrl, new WebSocketConnectionHandler() {
				public void onOpen() {
					Log.e(TAG, "WebSocket::onOpen : " + webSocketUrl + " - "
							+ ws.isConnected());
					if (dialogSocketDisconnect != null)
						if (dialogSocketDisconnect.isShowing())
							dialogSocketDisconnect.dismiss();
				}

				public void onTextMessage(String payload) {
					Log.e(TAG, "WebSocket::onTextMessage : " + payload + " - "
							+ ws.isConnected());
					Toast.makeText(mContext,
							"WebSocket::onTextMessage " + payload,
							Toast.LENGTH_SHORT).show();
					if (AndJs.isSoundEnable) {
						showNotification(payload, R.drawable.notification64,
								Notification.DEFAULT_ALL);
					} else {
						showNotification(payload, R.drawable.notification64,
								Notification.DEFAULT_ALL
										- Notification.DEFAULT_SOUND);
					}
				}

				public void onClose(int code, String reason) {
					Log.e(TAG, "WebSocket::onClose : (" + code + ") " + reason
							+ " - " + ws.isConnected());
					Intent i = new Intent(SGWEBSOCKET_ACTION);
					sendBroadcast(i);
				}
			});
		} catch (WebSocketException e) {
			Log.d(TAG, e.toString());
		}
	}

	@SuppressWarnings("deprecation")
	private void showNotification(String info, int icon, int type) {
		String title = "title";
		String location = "location";
		String nickname = "nickname";
		String itemId = "itemId";
		JSONTokener jsonParser = new JSONTokener(info);
		try {
			JSONObject person = (JSONObject) jsonParser.nextValue();
			title = person.getString("title");
			location = person.getString("Location");
			nickname = person.getString("nickname");
			itemId = person.getString("ItemId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ShowActivity.class);
		Bundle bundle = new Bundle();
		Log.e(TAG, "title " + title + ",location " + location + ",nickname "
				+ nickname + ",itemId " + itemId);
		bundle.putString(BUNDLEKEY, itemId); // 压入itemId
		intent.putExtras(bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(
				MainActivity.this, 0, intent, 0);
		Notification notification = new Notification(icon, title + " - "
				+ nickname + " @ " + location, // 滚动信息
				System.currentTimeMillis());
		notification.defaults = type;
		notification.setLatestEventInfo(mContext, title, nickname + " @ "
				+ location, contentIntent);
		mNotificationManager.notify(R.layout.activity_main, notification);
	}

	@SuppressLint("HandlerLeak")
	static Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_MAINURL:
				Log.e(TAG, "load: " + mainUrl);
				mXWalkView.load(mainUrl, null);
				break;
			case MSG_UPLOCATION:
				if (ws.isConnected()) {
					int lo = (int) (mLocation.getLongitude() * 3600);
					int la = (int) (mLocation.getLatitude() * 3600);
					Log.e(TAG,
							"{\"IMEI\":\"" + tm.getDeviceId()
									+ "\",\"location\":[" + lo + "," + la
									+ "]}" + ws.isConnected());
					ws.sendTextMessage("{\"IMEI\":\"" + tm.getDeviceId()
							+ "\",\"location\":[" + lo + "," + la + "]}");
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public static Handler getHandler() {
		return myHandler;
	}

	private void showAlertDialogSocketDisconnect() {
		Log.i(TAG, "showAlertDialogSocketDisconnect");
		dialogSocketDisconnect = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("服务器去火星了，我们正在玩命找回...")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "reconnecting socket");
					}
				}).show();
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
						mXWalkView.load(mainUrl, null);
					}
				}).show();
	}

	private void registerAllReceiver() {
		IntentFilter filter1 = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(netStatusReceiver, filter1);
		IntentFilter filter2 = new IntentFilter(SGWEBSOCKET_ACTION);
		this.registerReceiver(socketStatusReceiver, filter2);
	}

	private void unRegisterAllReceiver() {
		try {
			this.unregisterReceiver(netStatusReceiver);
			this.unregisterReceiver(socketStatusReceiver);
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
		mLocationClient.stop();
		ws.disconnect();
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

	private void initBaiduLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (!mXWalkView.getNavigationHistory().canGoBack()) {
			boolean isMain = false;
			if (mXWalkView != null) {
				if (mainHash.contains(mXWalkView.getUrl().hashCode())) {
					isMain = true;
				}
			}
			if (isMain) {
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

	public static void setLocation(BDLocation location) {
		mLocation = location;
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
			if (url.equals(ASSETSPAGE)) {
				if (mainUrl != null) {
					super.onLoadStarted(view, mainUrl);
				}
			}
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

	class SocketStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (dialogSocketDisconnect != null) {
				if (!dialogSocketDisconnect.isShowing()) {
					dialogSocketDisconnect.show();
				}
			} else {
				showAlertDialogSocketDisconnect();
			}

			new Timer().schedule(new TimerTask() {
				public void run() {
					Log.i(TAG, "reconnecting socket");
					ws.reconnect();
				}
			}, 5000);
		}
	};
}
