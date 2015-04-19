package sol.demo.seegeek;

import org.xwalk.core.XWalkView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@SuppressLint("SetJavaScriptEnabled")
public class ShowLivingActivity extends Activity {
	public static final String TAG = "SEEGEEK";
	private XWalkView mXWalkView;
	public boolean isPreview = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        Bundle bundleFromMainActivity = this.getIntent().getExtras();
        String livingStream = bundleFromMainActivity.getString("livingStream");
        Log.e(TAG, "livingStream : " + livingStream);

		AndJs aj = new AndJs(this);
		mXWalkView = (XWalkView) findViewById(R.id.xView);
		mXWalkView.addJavascriptInterface(aj, "AndJs");
		mXWalkView.setResourceClient(new SeeGeekViewClient(mXWalkView));
		mXWalkView.load(livingStream, null);
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
		super.onStop();
	}
}
