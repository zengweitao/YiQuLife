package com.quanmai.yiqu.share;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.share.ShareDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by zhanjinj on 16/3/7.
 */
public class ShareUtil {
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private ShareDialog mShareDialog;

	private Activity mContext;

	public ShareUtil(Activity context) {
		this.mContext = context;
		wx();
		qq();
		wb();
	}

	/**
	 * @param url     分享的链接地址
	 * @param content 分享的内容
	 * @param img     分享的图片的路径
	 */
	public void getShareDialog(final String url, final String content, final String img) {
		mShareDialog = new ShareDialog(mContext, new ShareDialog.onShareListener() {

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
				// 设置新浪分享内容
				sinaContent.setShareContent(content);
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

	public void getShareDialog(final String url, final String content, final int img, final String title) {
		mShareDialog = new ShareDialog(mContext, new ShareDialog.onShareListener() {

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

				circleMedia.setTitle(title);

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

				weixinContent.setTitle(title);

				mController.setShareMedia(weixinContent);
				share(SHARE_MEDIA.WEIXIN);
			}

			@Override
			public void toWB() {
				// 设置新浪分享
				SinaShareContent sinaContent = new SinaShareContent();
				// 设置新浪分享内容跳转URL
				sinaContent.setTargetUrl(url);
				// 设置新浪分享内容
				sinaContent.setShareContent(content);
				// 设置新浪分享图片
				sinaContent.setShareImage(new UMImage(mContext, img));

				sinaContent.setTitle(title);

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

				qzone.setTitle(title);

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

				qqShareContent.setTitle(title);

				mController.setShareMedia(qqShareContent);
				share(SHARE_MEDIA.QQ);
			}

		});
	}

	public void showShareDialog() {
		if (mShareDialog != null) {
			mShareDialog.show();
			WindowManager windowManager = mContext.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = mShareDialog.getWindow().getAttributes();
			lp.width = (int)(display.getWidth()); //设置宽度
			mShareDialog.getWindow().setAttributes(lp);
		}
	}

	private void share(SHARE_MEDIA media) {
		mController.postShare(mContext, media, new SocializeListeners.SnsPostListener() {

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
		Api.get().ShareSuccess(mContext, new ApiConfig.ApiRequestListener<String>() {
			@Override
			public void onSuccess(String msg, String data) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(String msg) {
				// TODO Auto-generated method stub
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
		UMWXHandler wxHandler = new UMWXHandler(mContext, appID, appSecret);
		wxHandler.addToSocialSDK();

		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * QQ好友以及QQ空间
	 */
	private void qq() {
		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext, "1104941357", "EachHeart");
		qqSsoHandler.addToSocialSDK();

		// QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mContext, "1104941357",
				"EachHeart");
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 新浪微博
	 */
	private void wb() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}
}
