package com.quanmai.yiqu.base;

import android.graphics.Bitmap;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class BaseWebViewClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		Log.e("q", "url="+url);
		if (url.equals("appfunc:changeNativeCity")) {
			return true;
		} else if (url.indexOf("appfunc:login") != -1) {
			// 加入购物车
			AddToCart(url.replace("appfunc:login:", ""));
			return true;
		} else if (url.indexOf("appfunc:pay") != -1) {
			return true;
		} else if (url.indexOf("appfunc:order") != -1) {// 支付
			NewOrder(url.replace("appfunc:order:", ""));
			return true;
		} else if (url.indexOf("appfunc:register") != -1) {
			return true;
		} else if (url.indexOf("appfunc:url:cart.php?") != -1) {
			GoCart();
			return true;
		} else if (url.indexOf("appfunc:id") != -1) {
			if ((url.replace("appfunc:id:", "")).length() < 1)
				return true;
			GoDetails(url.replace("appfunc:id:", ""));
			return true;
		} else if (url.indexOf("appfunc:shop_id") != -1) {
			if ((url.replace("appfunc:shop_id:", "")).length() < 1)
				return true;
			GoShop(url.replace("appfunc:shop_id:", ""));
			return true;
		} else if (url.indexOf("appfunc:alert") != -1) {
			String string = url.replace("appfunc:alert:", "");
			GetAlert(string);
			return true;
		} else if (url.indexOf("appfunc:usergift") != -1) {
			Recharge(url.replace("appfunc:usergift:", ""));
			return true;
		} else if (url.indexOf("appfunc:zhuce:") != -1) {
			String string = url.replace("appfunc:zhuce:", "");
			Registration(string);
			return true;
		} else if (url.indexOf("appfunc:bangding:") != -1) {
			String string = url.replace("appfunc:bangding:", "");
			Binding(string);
			return true;
		} else if (url.indexOf("is_child_url=1") != -1) {
			OpenNew(url);
			return true;
		} else {
			LoadUrl(url);
			return true;
		}
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			android.net.http.SslError error) {
		handler.proceed();
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		onStarted(view, url);
		showProgress();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		AddInteractive(view);
		super.onPageFinished(view, url);
		dismissProgress();
	}

	/** 加入订单 */
	public abstract void NewOrder(String OrderString);

	/** 加入购物车 */
	public abstract void AddToCart(String IdString);

	/** 去购物车 */
	public abstract void GoCart();

	/** 商品详情 */
	public abstract void GoDetails(String id);

	/** 去店铺 */
	public abstract void GoShop(String id);

	/** 对话框 */
	public abstract void GetAlert(String string);

	/** 充值 */
	public abstract void Recharge(String string);

	/** 注册 */
	public abstract void Registration(String string);

	/** 账号绑定 */
	public abstract void Binding(String string);

	/** 加载链接 */
	public abstract void LoadUrl(String url);

	/** 显示加载提示 */
	public abstract void showProgress();

	/** 隐藏加载提示 */
	public abstract void dismissProgress();

	/** 添加交互 */
	public abstract void AddInteractive(WebView view);

	/** 添加交互 */
	public abstract void onStarted(WebView view, String url);

	/** 打开新的 */
	public abstract void OpenNew(String url);
}
