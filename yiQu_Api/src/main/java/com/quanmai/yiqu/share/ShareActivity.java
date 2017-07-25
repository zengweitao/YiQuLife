package com.quanmai.yiqu.share;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.share.ShareDialog.onShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareActivity extends BaseActivity {
	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private ShareDialog mShareDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wx();
		qq();
		wb();
	}

	/**
	 * @param url
	 *            分享的链接地址
	 * @param content
	 *            分享的内容
	 * @param img
	 *            分享的图片的路径
	 */
	public void getShareDialog(final String url, final String content, final String img,final String title) {
			mShareDialog = new ShareDialog(ShareActivity.this, new onShareListener() {

				@Override
				public void toWXF() {
					// 设置微信朋友圈分享
					CircleShareContent circleMedia = new CircleShareContent();
					// 设置微信朋友圈分享内容跳转URL
					if (!TextUtils.isEmpty(url)){
						circleMedia.setTargetUrl(url);
					}
					// 设置微信朋友圈分享内容
					if (!TextUtils.isEmpty(content)){
						circleMedia.setShareContent(content);
					}
					// 设置微信朋友圈分享图片
					circleMedia.setShareImage(new UMImage(mContext, img));

					if (!TextUtils.isEmpty(title)){
						circleMedia.setTitle(title);
					}

					mController.setShareMedia(circleMedia);
					share(SHARE_MEDIA.WEIXIN_CIRCLE);
				}

				@Override
				public void toWX() {
					// 设置微信好友分享
					WeiXinShareContent weixinContent = new WeiXinShareContent();

					if (!TextUtils.isEmpty(url)){
						weixinContent.setTargetUrl(url);
					}
					// 设置微信朋友圈分享内容
					if (!TextUtils.isEmpty(content)){
						weixinContent.setShareContent(content);
					}
					// 设置微信朋友圈分享图片
					weixinContent.setShareImage(new UMImage(mContext, img));

					if (!TextUtils.isEmpty(title)){
						weixinContent.setTitle(title);
					}

					mController.setShareMedia(weixinContent);
					share(SHARE_MEDIA.WEIXIN);
				}

				@Override
				public void toWB() {
					// 设置新浪分享
					SinaShareContent sinaContent = new SinaShareContent();
					// 设置新浪分享内容跳转URL

					// 设置微信朋友圈分享内容跳转URL
					if (!TextUtils.isEmpty(url)){
						sinaContent.setTargetUrl(url);
					}
					// 设置微信朋友圈分享内容
					if (!TextUtils.isEmpty(content)){
						String newContent = content+" "+url;
						if (newContent.length()>140){
							int length = 140 - url.length()-1;
							String splitContent = content.substring(0,length-3)+"...";
							newContent = splitContent+" "+url;
						}
						sinaContent.setShareContent(newContent);
					}
					// 设置微信朋友圈分享图片
					sinaContent.setShareImage(new UMImage(mContext, img));

					if (!TextUtils.isEmpty(title)){
						sinaContent.setTitle(title);
					}

					// 设置分享图片, 参数2为图片的url地址
					mController.setShareMedia(sinaContent);
					share(SHARE_MEDIA.SINA);
				}

				@Override
				public void toQZONE() {
					// 设置QQ空间分享内容
					QZoneShareContent qzone = new QZoneShareContent();
					if (!TextUtils.isEmpty(url)){
						qzone.setTargetUrl(url);
					}
					// 设置微信朋友圈分享内容
					if (!TextUtils.isEmpty(content)){
						qzone.setShareContent(content);
					}
					// 设置微信朋友圈分享图片
					qzone.setShareImage(new UMImage(mContext, img));

					if (!TextUtils.isEmpty(title)){
						qzone.setTitle(title);
					}

					mController.setShareMedia(qzone);
					share(SHARE_MEDIA.QZONE);
				}

				@Override
				public void toQQ() {
					// 设置QQ好友分享内容
					QQShareContent qqShareContent = new QQShareContent();
					if (!TextUtils.isEmpty(url)){
						qqShareContent.setTargetUrl(url);
					}
					// 设置微信朋友圈分享内容
					if (!TextUtils.isEmpty(content)){
						qqShareContent.setShareContent(content);
					}
					// 设置微信朋友圈分享图片
					qqShareContent.setShareImage(new UMImage(mContext, img));

					if (!TextUtils.isEmpty(title)){
						qqShareContent.setTitle(title);
					}
					mController.setShareMedia(qqShareContent);
					share(SHARE_MEDIA.QQ);
				}

			});
	}
	
	
	public void getShareDialog(final String url, final String content, final int img) {
		mShareDialog = new ShareDialog(ShareActivity.this, new onShareListener() {

			@Override
			public void toWXF() {
				// 设置微信朋友圈分享
				CircleShareContent circleMedia = new CircleShareContent();
				// 设置微信朋友圈分享内容跳转URL
				circleMedia.setTargetUrl(url);
				// 设置微信朋友圈分享内容
				circleMedia.setShareContent(content);
				// 设置微信朋友圈分享图片
				circleMedia.setShareImage(new UMImage(mContext, img));

				mController.setShareMedia(circleMedia);
				share(SHARE_MEDIA.WEIXIN_CIRCLE);
			}

			@Override
			public void toWX() {
				// 设置微信好友分享
				WeiXinShareContent weixinContent = new WeiXinShareContent();
				// 设置分享内容跳转URL
				weixinContent.setTargetUrl(url);
				// 设置分享内容
				weixinContent.setShareContent(content);
				// 设置分享图片
				weixinContent.setShareImage(new UMImage(mContext, img));

				mController.setShareMedia(weixinContent);
				share(SHARE_MEDIA.WEIXIN);
			}

			@Override
			public void toWB() {
				// 设置新浪分享
				SinaShareContent sinaContent = new SinaShareContent();
				// 设置新浪分享内容跳转URL
				sinaContent.setTargetUrl(url);

				String newContent = content+" "+url;
				if (newContent.length()>140){
					int length = 140 - url.length()-1;
					String splitContent = content.substring(0,length-3)+"...";
					newContent = splitContent+" "+url;
				}

				// 设置新浪分享内容
				sinaContent.setShareContent(newContent);
				// 设置新浪分享图片
				sinaContent.setShareImage(new UMImage(mContext, img));

				// 设置分享图片, 参数2为图片的url地址
				mController.setShareMedia(sinaContent);
				share(SHARE_MEDIA.SINA);
			}

			@Override
			public void toQZONE() {
				// 设置QQ空间分享内容
				QZoneShareContent qzone = new QZoneShareContent();
				// 设置点击消息的跳转URL
				qzone.setTargetUrl(url);
				// 设置分享内容的标题
				qzone.setTitle("益趣生活");
				// 设置分享内容
				qzone.setShareContent(content);
				// 设置分享图片
				qzone.setShareImage(new UMImage(mContext, img));

				mController.setShareMedia(qzone);
				share(SHARE_MEDIA.QZONE);
			}

			@Override
			public void toQQ() {
				// 设置QQ好友分享内容
				QQShareContent qqShareContent = new QQShareContent();
				// 设置点击分享内容的跳转链接
				qqShareContent.setTargetUrl(url);
				// 设置分享title
				qqShareContent.setTitle("益趣生活");
				// 设置分享内容
				qqShareContent.setShareContent(content);
				// 设置分享图片
				qqShareContent.setShareImage(new UMImage(mContext, img));

				mController.setShareMedia(qqShareContent);
				share(SHARE_MEDIA.QQ);
			}

		});
}

	public void showShareDialog() {
		if (mShareDialog != null) {
			mShareDialog.show();
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = mShareDialog.getWindow().getAttributes();
			lp.width = (int)(display.getWidth()); //设置宽度
			mShareDialog.getWindow().setAttributes(lp);
		}
	}

	private void share(SHARE_MEDIA media) {
		mController.postShare(mContext, media, new SnsPostListener() {

			@Override
			public void onStart() {
//				showLoadingDialog("请稍候");
//				showCustomToast("开始分享");
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
//				dismissLoadingDialog();

				if (eCode == 200) {
					ShareSuccess();
//					showCustomToast("分享成功");
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
//					showCustomToast("分享失败[" + eCode + "] " + eMsg);
				}
			}

		});
	}

	private void ShareSuccess() {
		IntegralApi.get().ShareSuccess(mContext, new ApiRequestListener<String>() {
			@Override
			public void onSuccess(String msg, String data) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFailure(String msg) {
				// TODO Auto-generated method stub
				showCustomToast(msg);
			}
		});

	}

	/**
	 * 微信好友以及朋友圈
	 */
	private void wx() {
		String appID = "wx1f0b92c98a95201e";
		String appSecret = "9241928b3f528345964644b05f31145e";

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
		wxHandler.addToSocialSDK();

		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * QQ好友以及QQ空间
	 */
	private void qq() {
		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104941357", "EachHeart");
		qqSsoHandler.addToSocialSDK();

		// QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1104941357",
				"EachHeart");
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 新浪微博
	 */
	private void wb() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
