package sol.demo.seegeek;

import com.baidu.location.BDLocation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
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
    private static int MOOD_NOTIFICATIONS = R.layout.activity_main;

	public AndJs(Context context){
		this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String ret = tm.getDeviceId();
		Log.i(MainActivity.TAG, "getDeviceId = " + ret);
		return ret;
	}
	@JavascriptInterface
	public void showNotification(String title, String str) {
		Log.i(MainActivity.TAG, "showNotification = " + title + " | " + str);
//		showRemoteNotification(R.drawable.ic_launcher, str);
		showDefaultNotification(title, str, R.drawable.notification64, Notification.DEFAULT_ALL);
//		mNotificationManager.cancel(R.layout.activity_main);
	}

	public static void updateLocation(BDLocation l) {
		location = l;
	}
    
    @SuppressLint("NewApi")
	private PendingIntent makeDefaultIntent() {
        Intent[] intents = new Intent[4];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(mContext, MainActivity.class));
        // "App"
        intents[1] = new Intent(mContext, MainActivity.class);
        intents[1].putExtra("com.example.android.apis.Path", "App");
        // "App/Notification"
        intents[2] = new Intent(mContext, MainActivity.class);
        intents[2].putExtra("com.example.android.apis.Path", "App/Notification");
        // Now the activity to display to the user.
        intents[3] = new Intent(mContext, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivities(mContext, 0,
                intents, PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
	@SuppressWarnings("deprecation")
	private void showDefaultNotification(String title, String content, int icon, int type) {
//		PendingIntent contentIntent = makeDefaultIntent();
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