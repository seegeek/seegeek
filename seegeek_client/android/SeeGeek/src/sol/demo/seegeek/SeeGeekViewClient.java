package sol.demo.seegeek;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import android.webkit.WebView;

class SeeGeekViewClient extends XWalkResourceClient {
	public SeeGeekViewClient(XWalkView arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	// shouldOverrideUrlLoading 控制新的连接在当前WebView中打开
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	public void onLoadFinished(XWalkView view, String url) {
		super.onLoadFinished(view, url);
	}

	public void onLoadStarted(XWalkView view, String url) {
		super.onLoadStarted(view, url);
	}
	// doUpdateVisitedHistory 更新历史记录
	// onFormResubmission 应用程序重新请求网页数据
	// onLoadResource 加载指定地址提供的资源
	// onPageFinished 网页加载完毕
	// onPageStarted 网页开始加载
	// onReceivedError 报告错误信息
	// onScaleChanged WebView发生改变
}