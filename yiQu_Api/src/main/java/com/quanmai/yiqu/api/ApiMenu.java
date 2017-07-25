	package com.quanmai.yiqu.api;

import java.util.List;
import android.content.Context;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.api.vo.BagInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.api.vo.CollectionGoods;
import com.quanmai.yiqu.api.vo.CommCodeInfo;
import com.quanmai.yiqu.api.vo.CommServiceList;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.api.vo.FixClass;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.GoodsInfo;
import com.quanmai.yiqu.api.vo.HomeInfo;
import com.quanmai.yiqu.api.vo.Noticeinfo;
import com.quanmai.yiqu.api.vo.PicValidateInfo;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.api.vo.ScorePayInfo;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.api.vo.SortInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.api.vo.ZoneInfo;
import com.quanmai.yiqu.baidu.DeviceBean;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.mys.record.ManHour;
import com.quanmai.yiqu.ui.publish.ProductType;

public interface ApiMenu {

	/** 8001 获取注册验证码 */
	public void RegisteredCode(Context context, String phone,
			ApiRequestListener<String> listener);

	/** 8002 注册 */
	public void Registered(Context context, String phone, String code,
			String password, ApiRequestListener<String> listener);

	/** 8003 登录 */
	public void Login(Context context, String phone, String pwd,
			ApiRequestListener<String> listener);

	/** 8004 获取找回密码验证码 */
	public void FindPwdCode(Context context, String phone,
			ApiRequestListener<String> listener);

	/** 8005 找回密码 */
	public void FindPwd(Context context, String phone, String code,
			String password, ApiRequestListener<String> listener);

	/** 8006 修改密码 */
	public void ChangePwd(Context context, String password, String newpassword,
			ApiRequestListener<String> listener);

	/** 8007 获取用户信息 */
	public void UserDetail(Context context,
			ApiRequestListener<UserInfo> listener);

	/** 8008 设置昵称设置性别 */
	public void UserSetting(Context context, String img, String alias, String sex,
			String birth, ApiRequestListener<String> listener);
	
	/** 8009地址删除 */
	public void AddressDelete(Context context, String adderss_id,
			ApiRequestListener<String> listener);

	/** 8010 签到 */
	public void Sign(Context context, ApiRequestListener<SignData> listener);

	/** 8011 签到记录 */
	public void SignRecord(Context context, int year, int month,
			ApiRequestListener<ManHour> listener);
	/** 8012 获取地址列表 */
	public void AddressList(Context context, int page,
			ApiRequestListener<CommonList<AddressInfo>> listener);

	/** 8013 新增地址 */
	public void AddressAdd(Context context, AddressInfo info,
			ApiRequestListener<String> listener);

	/** 8014 编辑地址 */
	public void AddressEdit(Context context, AddressInfo info,
			ApiRequestListener<String> listener);

	/** 8015 设为默认地址 */
	public void AddressDefault(Context context, String address_id,
			ApiRequestListener<String> listener);

	/** 8016 获取积分商品 */
	public void QRCode(Context context, String Terminalno,
			ApiRequestListener<BagInfo> listener);

	/** 8017 积分商品支付 */
	public void ScorePay(Context context, String Terminalno, int count,
			ApiRequestListener<ScorePayInfo> listener);

	/** 8018 获取积分记录 */
	public void ScoreRecord(Context context, int page,
			ApiRequestListener<CommonList<ScoreMonthRecord>> listener);

	/** 8019 报修提交 */
	public void FixCommit(Context context, FixInfo fixInfo,
			ApiRequestListener<String> listener);

	/** 8020报修记录 */
	public void FixRecords(Context context, int page,
			ApiRequestListener<CommonList<FixInfo>> listener);

	/** 8021商品首页 */
	public void DealHomePage(Context context,
			ApiRequestListener<HomeInfo> listener);

	/** 8022商品筛选 */
	public void GoodsScreening(Context context,ApiRequestListener<CommonList<SortInfo>> listener);

	/** 8023商品列表 */
	public void GoodsList(Context context, int page, String keyword,
			String area_id, String class_id, String sort_type,
			ApiRequestListener<CommonList<GoodsBasic>> listener);

	/** 8024用户空间用户信息 */
	public void ZoneDetail(Context context, String user_id,
			ApiRequestListener<ZoneInfo> listener);

	/** 8025用户空间商品列表 */
	public void ZoneGoodsList(Context context, int page, String user_id,
			ApiRequestListener<CommonList<GoodsBasic>> listener);

	/** 8026商品详情 */
	public void GoodsDetail(Context context, String goods_id,
			ApiRequestListener<GoodsInfo> listener);

	/** 8027商品评论 */
	public void GoodsCommentList(Context context, int page, String goods_id,
			ApiRequestListener<CommonList<CommentInfo>> listener);

	/** 8028发布评论 */
	public void GoodsComment(Context context, String goods_id,
			String comment_content, ApiRequestListener<String> listener);

	/** 8029收藏商品列表 */
	public void GoodsCollectionList(Context context, int page,
			ApiRequestListener<CommonList<CollectionGoods>> listener);

	/** 8030收藏商品 */
	public void GoodsCollection(Context context, String goods_id,
			ApiRequestListener<String> listener);

	/** 8031取消收藏 */
	public void GoodsCollectionCancel(Context context, String goods_id,
			ApiRequestListener<String> listener);

	/** 8032发布管理列表 */
	public void GoodsManageList(Context context,boolean isAll, int page,
			ApiRequestListener<CommonList<GoodsBasic>> listener);

	/** 8033已删除列表 */
	public void GoodsDeletedList(Context context, int page,
			ApiRequestListener<CommonList<GoodsBasic>> listener);

	/** 8034清空已删除的列表 */
	public void GoodsDeletedListClear(Context context,
			ApiRequestListener<String> listener);

	/** 8035发布产品分类选择列表 */
	public void GoodsClassList(Context context,
			ApiRequestListener<List<ProductType>> listener);

	/** 8036发布产品 */
	void GoodsRelease(Context context, String goods_name,
			String goods_description, String goods_img, String goods_type,
			String goods_price, String class_id, String class_name,
			String goods_degree , String phone,
			ApiRequestListener<String> listener);

	/** 8037编辑产品 */
	public void GoodsEdit(Context context, String goods_id, String goods_name,
			String goods_description, String goods_img, String goods_type,
			String goods_price, String class_id, String class_name,
			String goods_degree, String position, String phone,
			ApiRequestListener<String> listener);

	/** 8038删除产品 */
	public void GoodsDelete(Context context, String goods_id,
			ApiRequestListener<String> listener);

	/** 8039关闭产品 */
	public void GoodsClosed(Context context,boolean isOpen, String goods_id,
			ApiRequestListener<String> listener);

	/** 8040分享产品 */
	public void GoodsShare(Context context, String goods_id,
			ApiRequestListener<String> listener);

	/** 8041留言列表 */
	public void MessageList(Context context, int page,
			ApiRequestListener<CommonList<CommentInfo>> listener);

	/** 8042系统通知 */
	public void NoticeList(Context context, int page,
			ApiRequestListener<CommonList<Noticeinfo>> listener);

	/** 8043管理员报修查看列表 */
	public void FixManageList(Context context, int page,String classcode,
			ApiRequestListener<CommonList<FixInfo>> listener);

	/** 8044管理员报修详情获取 */
	public void FixManageDetail(Context context,String service_id,ApiRequestListener<FixInfo> listener);

	/** 8045条形码获取 */
	public void BarCode(Context context, String bar_code,
			ApiRequestListener<BarCodeInfo> listener);

	/** 8046获取周围设备坐标 */
	public void NearbyDevices(Context context,
			ApiRequestListener<CommonList<DeviceBean>> listener);
	
	/** 8047评分 */
	public void Points(Context context, String points,String bar_code, String iamge,
			ApiRequestListener<String> listener);
	
	
	/** 8048留言 */
	public void Message(Context context, String user_id,
			String comment_content, ApiRequestListener<String> listener);

	/** 8049得分记录 */
	public void ScroeRecord(Context context,int page,String starttime,String endtime,ApiRequestListener<CommonList<ScoreRecordInfo>> listener);

	/** 8050得分记录 */
	public void ScroeRecord(Context context,int page,String datetime,ApiRequestListener<CommonList<ScoreRecordInfo>> listener);

	/** 8051分享送积分 */
	public void ShareSuccess(Context context,ApiRequestListener<String> listener);

	/** 8052物业报修分类（管理员） */
	public void FixManagerClass(Context context,ApiRequestListener<CommonList<FixClass>> listener);
	
	/** 8053物业报修分类(用户) */
	public void FixUserClass(Context context,ApiRequestListener<CommonList<FixClass>> listener);
	
	/** 8054获取图片验证页面*/
	public void PicValidate(Context context,ApiRequestListener<CommonList<PicValidateInfo>> listener);
//
	/** 8055图片验证成功*/
	public void PicValidateSuccess(Context context,ApiRequestListener<String> listener);

	/** 8055图片验证成功*/
	public void FixAttend(Context context,String service_id,ApiRequestListener<String> listener);

	/** 8060获取广告信息*/
	public void GetAvd(Context context,String adType,ApiRequestListener<BannerInfo> listener);


	/** 8057获取小区列表*/
	public void GetCommunityList(Context context,String city,ApiRequestListener<CommCodeInfo> listener);

	/** 8058社区服务信息列表*/
	public void GetCommunityServiceList(Context context,String commcode,ApiRequestListener<CommServiceList> listener);

//	/** 8049回复 */
//	public void Message(Context context, String adderss_id,
//			ApiRequestListener<String> listener);
//


}
