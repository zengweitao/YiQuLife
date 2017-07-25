package com.quanmai.yiqu.api;

import android.content.Context;

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
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.mys.record.ManHour;
import com.quanmai.yiqu.ui.publish.ProductType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Api extends ApiConfig implements ApiMenu {
	private static ApiMenu mInstance;

	public static ApiMenu get() {
		if (mInstance == null) {
			mInstance = new Api();
		}
		return mInstance;
	}

	/** 8001 获取注册验证码 */
	@Override
	public void RegisteredCode(Context context, String phone,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8001&phone=" + phone;
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8002 注册 */
	@Override
	public void Registered(Context context, String phone, String code,
			String password, final ApiRequestListener<String> listener) {
		String paramStr = "type=8002&phone=" + phone + "&code=" + code
				+ "&password=" + Utils.getMD5(password);
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"),
						jsonObject.getString("token"));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8003 登录 */
	@Override
	public void Login(Context context, String phone, String pwd,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8003&phone=" + phone + "&password="
				+ Utils.getMD5(pwd);
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"),
						jsonObject.getString("token"));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8004 获取找回密码验证码 */
	@Override
	public void FindPwdCode(Context context, String phone,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8004&phone=" + phone;
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8005 找回密码 */
	@Override
	public void FindPwd(Context context, String phone, String code,
			String password, final ApiRequestListener<String> listener) {
		String paramStr = "type=8005&phone=" + phone + "&code=" + code
				+ "&password=" + Utils.getMD5(password);
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"),
						jsonObject.getString("token"));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8006 修改密码 */
	@Override
	public void ChangePwd(Context context, String password, String newpassword,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8006&password=" + Utils.getMD5(password)
				+ "&newpassword=" + Utils.getMD5(newpassword);
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {

				listener.onSuccess(jsonObject.getString("msg"),
						jsonObject.getString("token"));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8007 获取用户信息 */
	@Override
	public void UserDetail(final Context context,
			final ApiRequestListener<UserInfo> listener) {
		String paramStr = "type=8007";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new UserInfo(jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);

			}
		});
	}

	/** 8008 设置昵称设置性别 */
	@Override
	public void UserSetting(Context context, String img, String alias,
			String sex, String birth, final ApiRequestListener<String> listener) {
		StringBuilder params = new StringBuilder();
		params.append("type=8008");
		params.append("&img=");
		params.append(img);
		params.append("&alias=");
		params.append(alias);
		params.append("&sex=");
		params.append(sex);
		params.append("&birth=");
		params.append(birth);

		HttpPost(context, params.toString(), new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}

		});
	}

	/** 8010 签到 */
	@Override
	public void Sign(final Context context,
			final ApiRequestListener<SignData> listener) {
		String paramStr = "type=8010";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new SignData(jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8011 签到记录 */
	@Override
	public void SignRecord(final Context context, final int year,
			final int month, final ApiRequestListener<ManHour> listener) {
		// Calendar now = Calendar.getInstance();
		String paramStr = "type=8011&year=" + year + "&month=" + month;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				String signpoint="5";
				if(jsonObject.has("signpoint"))
				{
					signpoint=jsonObject.getString("signpoint");
				}
				if (jsonObject.has("times")) {
					ManHour manHour = new ManHour(jsonObject
							.getJSONArray("times"));
					
					listener.onSuccess(jsonObject.getString("msg") + "#"
							+ jsonObject.getString("signdays") + "#"
							+ jsonObject.getString("totaldays")+"#"+signpoint, manHour);
				} else {
					listener.onSuccess(jsonObject.getString("msg") + "#"
							+ jsonObject.getString("signdays") + "#"
							+ jsonObject.getString("totaldays")+"#"+signpoint, new ManHour(
							year, month));
				}
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8012 获取地址列表 */
	@Override
	public void AddressList(Context context, int page,
			final ApiRequestListener<CommonList<AddressInfo>> listener) {
		String paramStr = "type=8012" + "&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<AddressInfo> commonList = new CommonList<AddressInfo>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						AddressInfo info = new AddressInfo(array
								.getJSONObject(i));
						commonList.add(info);
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8013 新增地址 */
	@Override
	public void AddressAdd(Context context, AddressInfo info,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8013&" + info.toString();
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {

				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8014 编辑地址 */
	@Override
	public void AddressEdit(Context context, AddressInfo info,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8014&" + info.toString();
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8015 设为默认地址 */
	@Override
	public void AddressDefault(Context context, String address_id,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8015" + "&address_id=" + address_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8016 获取积分商品 */
	@Override
	public void QRCode(Context context, String Terminalno,
			final ApiRequestListener<BagInfo> listener) {
		String paramStr = "type=8016&terminalno=" + Terminalno;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new BagInfo(
						jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8017 积分商品支付 */
	@Override
	public void ScorePay(Context context, String Terminalno, int count,
			final ApiRequestListener<ScorePayInfo> listener) {
		String paramStr = "type=8017&count=" + count + "&terminalno="
				+ Terminalno;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"),
						new ScorePayInfo(jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8018 获取积分记录 */
	@Override
	public void ScoreRecord(Context context, int page,
			final ApiRequestListener<CommonList<ScoreMonthRecord>> listener) {
		String paramStr = "type=8018&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<ScoreMonthRecord> commonList = new CommonList<ScoreMonthRecord>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new ScoreMonthRecord(array
								.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8019 报修提交 */
	@Override
	public void FixCommit(Context context, FixInfo fixInfo,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8019" + "&address_id=" + fixInfo.address_id
				+ "&come_time=" + fixInfo.come_time + "&problem_id="
				+ fixInfo.problem_id + "&problem=" + fixInfo.problem
				+ "&description=" + fixInfo.description + "&describeimg="
				+ fixInfo.picurl;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8020报修记录 */
	@Override
	public void FixRecords(Context context, int page,
			final ApiRequestListener<CommonList<FixInfo>> listener) {
		String paramStr = "type=8020&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixInfo> commonList = new CommonList<FixInfo>();
				commonList.current_page = jsonObject.getInt("current_page");
				commonList.max_page = jsonObject.getInt("max_page");
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new FixInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8021商品首页 */
	@Override
	public void DealHomePage(Context context,
			final ApiRequestListener<HomeInfo> listener) {
		String paramStr = "type=8021";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				// CommonList<AdvertInfo> commonList = new
				// CommonList<AdvertInfo>();
				// JSONArray array = jsonObject.getJSONArray("classList");
				// for (int i = 0; i < array.length(); i++) {
				// commonList.add(new AdvertInfo(array.getJSONObject(i)));
				// }
				listener.onSuccess(jsonObject.getString("msg"), new HomeInfo(
						jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8022商品筛选 */
	@Override
	public void GoodsScreening(Context context,
			final ApiRequestListener<CommonList<SortInfo>> listener) {
		String paramStr = "type=8022";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<SortInfo> commonList = new CommonList<SortInfo>();
				if (jsonObject.has("classList")) {
					JSONArray array = jsonObject.getJSONArray("classList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new SortInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8023商品列表 */
	@Override
	public void GoodsList(Context context, int page, String keyword,
			String area_id, String class_id, String sort_type,
			final ApiRequestListener<CommonList<GoodsBasic>> listener) {
		String paramStr = "type=8023&page=" + page + "&keyword=" + keyword
				+ "&area_id=" + area_id + "&class_id=" + class_id
				+ "&sort_type=" + sort_type;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("goodsList")) {
					JSONArray array = jsonObject.getJSONArray("goodsList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new GoodsBasic(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8024用户空间用户信息 */
	@Override
	public void ZoneDetail(Context context, String user_id,
			final ApiRequestListener<ZoneInfo> listener) {
		String paramStr = "type=8024&user_id=" + user_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new ZoneInfo(
						jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8025用户空间商品列表 */
	@Override
	public void ZoneGoodsList(Context context, int page, String user_id,
			final ApiRequestListener<CommonList<GoodsBasic>> listener) {
		String paramStr = "type=8025&page=" + page + "&user_id=" + user_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("goodsList")) {
					JSONArray array = jsonObject.getJSONArray("goodsList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new GoodsBasic(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8026商品详情 */
	@Override
	public void GoodsDetail(Context context, String goods_id,
			final ApiRequestListener<GoodsInfo> listener) {
		String paramStr = "type=8026&goods_id=" + goods_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new GoodsInfo(
						jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8027商品评论 */
	@Override
	public void GoodsCommentList(Context context, int page, String goods_id,
			final ApiRequestListener<CommonList<CommentInfo>> listener) {
		String paramStr = "type=8027&page=" + page + "&goods_id=" + goods_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<CommentInfo> commonList = new CommonList<CommentInfo>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("comments")) {
					JSONArray array = jsonObject.getJSONArray("comments");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new CommentInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8028发布评论 */
	@Override
	public void GoodsComment(Context context, String goods_id,
			String comment_content, final ApiRequestListener<String> listener) {
		String paramStr = "type=8028&goods_id=" + goods_id
				+ "&comment_content=" + comment_content;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8029收藏商品列表 */
	@Override
	public void GoodsCollectionList(Context context, int page,
			final ApiRequestListener<CommonList<CollectionGoods>> listener) {
		String paramStr = "type=8029&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<CollectionGoods> commonList = new CommonList<CollectionGoods>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("goodsCollectList")) {
					JSONArray array = jsonObject
							.getJSONArray("goodsCollectList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new CollectionGoods(array
								.getJSONObject(i)));
					}
				}

				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8030收藏商品 */
	@Override
	public void GoodsCollection(Context context, String goods_id,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8030&goods_id=" + goods_id, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8031取消收藏 */
	@Override
	public void GoodsCollectionCancel(Context context, String goods_id,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8031&goods_id=" + goods_id, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8032发布管理列表 0正常 1已关闭 */
	@Override
	public void GoodsManageList(Context context, boolean isAll, int page,
			final ApiRequestListener<CommonList<GoodsBasic>> listener) {
		int status = 0;
		if (!isAll) {
			status = 1;
		}
		String paramStr = "type=8032&status=" + status + "&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("goodsList")) {
					JSONArray array = jsonObject.getJSONArray("goodsList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new GoodsBasic(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8033已删除列表 */
	@Override
	public void GoodsDeletedList(Context context, int page,
			final ApiRequestListener<CommonList<GoodsBasic>> listener) {
		String paramStr = "type=8032&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new GoodsBasic(jsonObject));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8034清空已删除的列表 */
	@Override
	public void GoodsDeletedListClear(Context context,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8034", new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8035发布产品分类选择列表 */
	@Override
	public void GoodsClassList(Context context,
			final ApiRequestListener<List<ProductType>> listener) {
		HttpPost(context, "type=8022", new HttpCallBack() {

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess("",
						ProductType.get(jsonObject.getJSONArray("classList")));
			}

		});
	}

	/** 8036发布产品 */
	@Override
	public void GoodsRelease(Context context, String goods_name,
			String goods_description, String goods_img, String goods_type,
			String goods_price, String class_id, String class_name,
			String goods_degree, String phone,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8036&goods_name=" + goods_name
				+ "&goods_description=" + goods_description + "&goods_img="
				+ goods_img + "&goods_type=" + goods_type + "&goods_price="
				+ goods_price + "&class_id=" + class_id + "&class_name="
				+ class_name + "&goods_degree=" + goods_degree + "&address_id=0&phone=" + phone, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8037编辑产品 */
	@Override
	public void GoodsEdit(Context context, String goods_id, String goods_name,
			String goods_description, String goods_img, String goods_type,
			String goods_price, String class_id, String class_name,
			String goods_degree, String position, String phone,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8037&address_id=0&goods_id=" + goods_id
				+ "&goods_name=" + goods_name + "&goods_description="
				+ goods_description + "&goods_img=" + goods_img
				+ "&goods_type=" + goods_type + "&goods_price=" + goods_price
				+ "&class_id=" + class_id + "&class_name=" + class_name
				+ "&goods_degree=" + goods_degree + "&position=" + position
				+ "&phone=" + phone, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				if (jsonObject.getInt("status") == 0) {
					listener.onFailure(jsonObject.getString("msg"));
				} else {
					listener.onSuccess(jsonObject.getString("msg"), "");
				}
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8038删除产品 */
	@Override
	public void GoodsDelete(Context context, String goods_id,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8038&goods_id=" + goods_id, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8039关闭产品 0开启 1关闭 */
	@Override
	public void GoodsClosed(Context context, boolean isOpen, String goods_id,
			final ApiRequestListener<String> listener) {
		int status = 0;
		if (isOpen) {
			status = 1;
		}
		HttpPost(context, "type=8039&goods_id=" + goods_id + "&status="
				+ status, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8040分享产品 */
	@Override
	public void GoodsShare(Context context, String goods_id,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8040&goods_id=" + goods_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {

				listener.onSuccess(jsonObject.getString("msg"),
						jsonObject.toString());
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8041留言列表 */
	@Override
	public void MessageList(Context context, int page,
			final ApiRequestListener<CommonList<CommentInfo>> listener) {
		String paramStr = "type=8041&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<CommentInfo> commonList = new CommonList<CommentInfo>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new CommentInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8042系统通知 */
	@Override
	public void NoticeList(Context context, int page,
			final ApiRequestListener<CommonList<Noticeinfo>> listener) {
		String paramStr = "type=8042&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<Noticeinfo> commonList = new CommonList<Noticeinfo>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new Noticeinfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8043管理员报修查看列表 */
	@Override
	public void FixManageList(Context context, int page, String classcode,
			final ApiRequestListener<CommonList<FixInfo>> listener) {
		String paramStr = "type=8043&page=" + page + "&classcode=" + classcode;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixInfo> commonList = new CommonList<FixInfo>();
				commonList.current_page = jsonObject.getInt("current_page");
				commonList.max_page = jsonObject.getInt("max_page");
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new FixInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8044管理员报修详情获取 */
	@Override
	public void FixManageDetail(Context context, String service_id,
			final ApiRequestListener<FixInfo> listener) {
		String paramStr = "type=8044&service_id=" + service_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new FixInfo(
						jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8045条形码获取 */
	@Override
	public void BarCode(Context context, String bar_code,
			final ApiRequestListener<BarCodeInfo> listener) {
		String paramStr = "type=8045&bar_code=" + bar_code;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"),
						new BarCodeInfo(jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8046获取周围设备坐标 */
	@Override
	public void NearbyDevices(Context context,
			final ApiRequestListener<CommonList<DeviceBean>> listener) {
		HttpPost(context, "type=8046", new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), DeviceBean
						.parseDeviceBean(jsonObject.optString("list")));
				// listener.onSuccess(
				// jsonObject.optString("msg"),
				// DeviceBean
				// .parseDeviceBean("[{\"location\":\"120,30\",\"name\":\"福二袋-2031\",\"address\":\"回龙观东区旺角龙锦三街北侧\"},{\"location\":\"120.01,30.1\",\"name\":\"福二袋-2031\",\"address\":\"回龙观东区旺角龙锦三街北侧\"}]"));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8047评分 */
	@Override
	public void Points(Context context, String points, String bar_code,
			String image, final ApiRequestListener<String> listener) {
		String parram = "";
		if (image == null)
			parram = "type=8047&points=" + points + "&bar_code=" + bar_code;
		else
			parram = "type=8047&points=" + points + "&bar_code=" + bar_code
					+ "&image=" + image;
		HttpPost(context, parram, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8009地址删除 */
	@Override
	public void AddressDelete(Context context, String address_id,
			final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8009&address_id=" + address_id,
				new HttpCallBack() {

					@Override
					public void onSuccess(JSONObject jsonObject)
							throws JSONException {
						listener.onSuccess(jsonObject.optString("msg"), "");
					}

					@Override
					public void onFailure(String msg) {
						listener.onFailure(msg);
					}
				});
	}

	/** 8048留言 */
	@Override
	public void Message(Context context, String user_id,
			String comment_content, final ApiRequestListener<String> listener) {
		String paramStr = "type=8048&user_id=" + user_id + "&content="
				+ comment_content;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8049得分记录 */
	// type=8049 垃圾分类得分时间格式要传为2012-01-01也就是yyyy-MM-dd这种格式
	@Override
	public void ScroeRecord(Context context, int page, String starttime,
			String endtime,
			final ApiRequestListener<CommonList<ScoreRecordInfo>> listener) {
		String paramStr = "type=8049&page=" + page + "&starttime=" + starttime
				+ "&endtime=" + endtime;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<ScoreRecordInfo> commonList = new CommonList<ScoreRecordInfo>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}
					if (jsonObject.has("scoreList")) {
						JSONArray array = jsonObject.getJSONArray("scoreList");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new ScoreRecordInfo(array
									.getJSONObject(i)));
						}
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8050得分记录 */
	@Override
	public void ScroeRecord(Context context, int page, String datetime,
			final ApiRequestListener<CommonList<ScoreRecordInfo>> listener) {
		String paramStr = "type=8050&page=" + page + "&datetime=" + datetime;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<ScoreRecordInfo> commonList = new CommonList<ScoreRecordInfo>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}

					if (jsonObject.has("scoreList")) {
						JSONArray array = jsonObject.getJSONArray("scoreList");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new ScoreRecordInfo(array
									.getJSONObject(i)));
						}
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8051分享送积分 */
	@Override
	public void ShareSuccess(Context context,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8051";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8052物业报修分类（管理员） */
	@Override
	public void FixManagerClass(Context context,
			final ApiRequestListener<CommonList<FixClass>> listener) {
		String paramStr = "type=8052";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixClass> commonList = new CommonList<FixClass>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}
					if (jsonObject.has("classlist")) {
						JSONArray array = jsonObject.getJSONArray("classlist");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new FixClass(array.getJSONObject(i)));
						}
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8053物业报修分类(用户) */
	@Override
	public void FixUserClass(Context context,
			final ApiRequestListener<CommonList<FixClass>> listener) {
		String paramStr = "type=8053";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixClass> commonList = new CommonList<FixClass>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}
					if (jsonObject.has("list")) {
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new FixClass(array.getJSONObject(i)));
						}
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8054获取图片验证页面 */
	@Override
	public void PicValidate(Context context,
			final ApiRequestListener<CommonList<PicValidateInfo>> listener) {
		String paramStr = "type=8054";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<PicValidateInfo> commonList = new CommonList<PicValidateInfo>();
				commonList.name = jsonObject.getString("classname");
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new PicValidateInfo(array
								.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("answers"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});

	}

	/** 8055图片验证成功 */
	@Override
	public void PicValidateSuccess(Context context,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8055";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				String point = jsonObject.getString("receivepoint");
				String times = jsonObject.getString("monthlasttimes");
				if (point.equals("0") && times.equals("0")) {
					listener.onFailure(jsonObject.getString("msg"));
				} else {
					listener.onSuccess(jsonObject.getString("receivepoint"),
							jsonObject.getString("monthlasttimes"));
				}

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}

	/** 8056图片验证成功 */
	@Override
	public void FixAttend(Context context, String service_id,
			final ApiRequestListener<String> listener) {
		String paramStr = "type=8056&&service_id=" + service_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
	/** 8060获取广告信息*/
	@Override
	public void GetAvd(Context context, String adType,final ApiRequestListener<BannerInfo> listener) {
		String paramStr = "type=8060&&adType=" + adType;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new BannerInfo(jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
	/** 8057获取社区列表*/
	@Override
	public void GetCommunityList(Context context, String city,final ApiRequestListener<CommCodeInfo> listener) {
		String paramStr = "type=8057&&city=" + city;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new CommCodeInfo(jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
	/** 8057获取社区列表*/
	@Override
	public void GetCommunityServiceList(Context context, String commcode,final ApiRequestListener<CommServiceList> listener) {
		String paramStr = "type=8058&&commcode=" + commcode;
		HttpPost(context, paramStr, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new CommServiceList(jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
}
