package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.common.util.RelativeDateFormat;

public class CommentInfo {
	public CommentInfo(JSONObject jsonObject) throws JSONException {
		id = jsonObject.getString("comment_id");
		face = jsonObject.getString("face");
		alias = jsonObject.getString("alias");
		user_id = jsonObject.getString("user_id");
		content = jsonObject.getString("comment_content");
		time = RelativeDateFormat.format(jsonObject.getString("comment_time"));
	}

	/** 评论id */
	public String id;
	/** 用户头像 */
	public String face;
	/** 用户昵称 */
	public String alias;
	/** 用户id */
	public String user_id;
	/** 评论内容 */
	public String content;
	/** 评论时间 */
	public String time;
}
