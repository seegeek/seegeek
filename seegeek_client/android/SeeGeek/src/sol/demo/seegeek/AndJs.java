package sol.demo.seegeek;

import com.baidu.location.BDLocation;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.xwalk.core.JavascriptInterface;

public class AndJs {
	private Context mContext;
	private static BDLocation location;
	private TelephonyManager tm;
    public static boolean isSoundEnable = false;

	public AndJs(Context context){
		this.mContext = context;
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
	}
}