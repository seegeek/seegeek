package sol.demo.seegeek;

import org.xwalk.core.XWalkView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

class MyXWalkView extends XWalkView {
	public MyXWalkView(Context arg0, Activity arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(MainActivity.TAG, "onKeyDown()");
		if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}