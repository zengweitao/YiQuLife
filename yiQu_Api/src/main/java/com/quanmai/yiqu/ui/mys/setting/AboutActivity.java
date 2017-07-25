package com.quanmai.yiqu.ui.mys.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.share.ShareActivity;

/**
 * 关于
 */
public class AboutActivity extends ShareActivity implements OnClickListener {
	WebView webViewAbout;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		init();
	}

	private void init() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText("关于");

		webViewAbout = (WebView) findViewById(R.id.webViewAbout);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		webViewAbout.getSettings().setJavaScriptEnabled(true);
		webViewAbout.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webViewAbout.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		webViewAbout.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress > 0 && newProgress < 100) {
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);
				} else {
					progressBar.setVisibility(View.GONE);
				}
			}
		});

		webViewAbout.loadUrl(ApiConfig.CLSSSIFIGARBAGE_URL+"/yiqu/about/index.html");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back: {
				finish();
				break;
			}
			default:
				break;
		}
	}

	private String apkVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String versionName = info.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "无法获取版本号";
		}
	}

	private Bitmap generateQRCode(String content) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			// MultiFormatWriter writer = new MultiFormatWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
					700, 700);
			return bitMatrix2Bitmap(matrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int color = Color.WHITE;
				if (matrix.get(i, j)) {
					color = Color.BLACK;
				}
				rawData[i + (j * w)] = color;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}
}
