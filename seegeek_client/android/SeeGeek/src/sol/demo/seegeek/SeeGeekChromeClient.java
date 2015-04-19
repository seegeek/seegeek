package sol.demo.seegeek;

import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

class SeeGeekChromeClient extends WebChromeClient {
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		return super.onJsAlert(view, url, message, result);
	}
	public void onShowCustomView(View view, CustomViewCallback callback) {
		Log.e(MainActivity.TAG, "onShowCustomView");
	}
	public View getVideoLoadingProgressView() {
		Log.e(MainActivity.TAG, "getVideoLoadingProgressView");
		return null;
	}
}