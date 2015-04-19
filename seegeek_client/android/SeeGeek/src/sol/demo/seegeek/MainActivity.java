package sol.demo.seegeek;

import java.util.Timer;
import java.util.TimerTask;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkSettings;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	public static final String TAG = "SEEGEEK";
	private XWalkView mXWalkView;
//	private String selfdir = Environment.getExternalStorageDirectory() + "/SeeGeek/";
	public boolean isPreview = true;

	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02"; // "gcj02";"bd09ll";"bd09";
	private int span = 5000;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
		initBaiduLocation();
		mLocationClient.start();

		AndJs aj = new AndJs(this);
		mXWalkView = (XWalkView) findViewById(R.id.xView);
		mXWalkView.addJavascriptInterface(aj, "AndJs");
		mXWalkView.setResourceClient(new SeeGeekViewClient(mXWalkView));
		
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            String livingStream = bundle.getString("livingStream");
            if ( livingStream != null ) {
                Log.e(TAG, "livingStream : " + livingStream);
                Toast.makeText(this, "livingStream : " + livingStream, Toast.LENGTH_SHORT).show();
                mXWalkView.load(livingStream, null);
            } else {
                Log.e(TAG, "no livingStream");
                Toast.makeText(this, "no livingStream", Toast.LENGTH_SHORT).show();
            	mXWalkView.load("http://58.53.219.69/client/index.html", null);
            }
        } else {
            Log.e(TAG, "go to index");
            Toast.makeText(this, "go to index", Toast.LENGTH_SHORT).show();
        	mXWalkView.load("http://58.53.219.69/client/index.html", null);
//        	mXWalkView.load("http://m.baidu.com/", null);
        }
		Log.i(TAG, "View size: " + mXWalkView.getWidth() + "x" + mXWalkView.getHeight());
		Log.i(TAG, "Measured size: " + mXWalkView.getMeasuredWidth() + "x" + mXWalkView.getMeasuredHeight());
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

	private void initBaiduLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(TAG, "onKeyDown()");
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			if (vMain.canGoBack()){
//				vMain.goBack();
//				return true;
//			}
			finish();
			System.exit(0);
//            exitBy2Click();
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private boolean isExit = false;
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
}
