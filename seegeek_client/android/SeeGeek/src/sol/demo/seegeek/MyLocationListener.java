package sol.demo.seegeek;

import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * 实现实位回调监听
 */
public class MyLocationListener implements BDLocationListener {
	@Override
	public void onReceiveLocation(BDLocation location) {
		Log.i(MainActivity.TAG, "onReceiveLocation");
		AndJs.updateLocation(location);
		MainActivity.setLocation(location);
		Message message = new Message();
		message.what = MainActivity.MSG_UPLOCATION;
		MainActivity.getHandler().sendMessage(message);
		// Receive Location
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\ndirection : ");
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append(location.getDirection());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			// 运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
		}
//		Log.i(MainActivity.TAG, sb.toString());
	}
}