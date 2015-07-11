package sol.demo.seegeek;

import com.baidu.location.BDLocation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.xwalk.core.JavascriptInterface;

public class AndJs {
	private Context mContext;
	private static BDLocation location;
    private NotificationManager mNotificationManager;
	private TelephonyManager tm;
    private static int MOOD_NOTIFICATIONS = R.layout.activity_main;
    private boolean isSoundEnable = false;

	public AndJs(Context context){
		this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static void updateLocation(BDLocation l) {
		location = l;
	}
	
	@JavascriptInterface
	public void showToast(String toast) {
		Log.i(MainActivity.TAG, "showToast = " + toast);
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}
	
	@JavascriptInterface
	public String getCurrentLocation() {
		Log.i(MainActivity.TAG, "getCurrentLocation = " + location.getAddrStr());
		return location.getAddrStr();
	}
	
	@JavascriptInterface
	public String getCurrentGPS() {
		int lo = (int) (location.getLongitude() * 3600);
		int la = (int) (location.getLatitude() * 3600);
		String ret =  lo+ "," + la;
		Log.i(MainActivity.TAG, "getCurrentGPS = " + ret);
		return ret;
	}
	
	@JavascriptInterface
	public String getLongitude() {
		int lo = (int) (location.getLongitude() * 3600);
		Log.i(MainActivity.TAG, "getCurrentGPS = " + lo);
		return Integer.toString(lo);
	}
	
	@JavascriptInterface
	public String getLatitude() {
		int la = (int) (location.getLatitude() * 3600);
		Log.i(MainActivity.TAG, "getCurrentGPS = " + la);
		return Integer.toString(la);
	}
	
	@JavascriptInterface
	public String getIMEI() {
		String ret = tm.getDeviceId();
		Log.i(MainActivity.TAG, "IMEI = " + ret);
		return ret;
	}
	
	@JavascriptInterface
	public String getIMSI() {
		String ret = tm.getSubscriberId();
		Log.i(MainActivity.TAG, "IMSI = " + ret);
		return ret;
	}

	@JavascriptInterface
	public String getPhoneNum() {
		String ret = tm.getLine1Number();
		Log.i(MainActivity.TAG, "PhoneNumber = " + ret);
		return ret;
	}
	
	@JavascriptInterface
	public void setSoundEnable(boolean b) {
		Log.i(MainActivity.TAG, "setSoundEnable = " + b);
		isSoundEnable = b;
	}
	
	@JavascriptInterface
	public void showNotification(String title, String str) {
		Log.i(MainActivity.TAG, "showNotification = " + title + " | " + str);
		if (isSoundEnable) {
			showDefaultNotification(title, str, R.drawable.notification64, Notification.DEFAULT_ALL);
		} else {
			showDefaultNotification(title, str, R.drawable.notification64, Notification.DEFAULT_ALL - Notification.DEFAULT_SOUND);
		}
	}
    
	@SuppressWarnings("deprecation")
	private void showDefaultNotification(String title, String content, int icon, int type) {
		Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("livingStream", content); // 压入数据 
        intent.putExtras(bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new Notification(icon, title + ": " + content, System.currentTimeMillis());
        notification.setLatestEventInfo(mContext, title, content, contentIntent);
        notification.defaults = type;
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notification);
    }
    
	private PendingIntent makeMoodIntent(int moodId) {
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, MainActivity.class).putExtra("moodimg", moodId),
                PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
    @SuppressWarnings("unused")
	private void showRemoteNotification(int moodId, String str) {
        Notification notif = new Notification();
        notif.contentIntent = makeMoodIntent(moodId);
        CharSequence text = str;
        notif.tickerText = text;
        notif.icon = moodId;
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.statusbar_remoteview);
        contentView.setTextViewText(R.id.text, text);
        contentView.setImageViewResource(R.id.icon, moodId);
        notif.contentView = contentView;
        notif.defaults = Notification.DEFAULT_ALL;
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notif);
    }
}