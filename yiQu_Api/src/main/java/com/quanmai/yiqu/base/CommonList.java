package com.quanmai.yiqu.base;

import java.io.Serializable;
import java.util.ArrayList;

public class CommonList<T> extends ArrayList<T> implements Serializable {
	private static final long serialVersionUID = -1911205674136151829L;
	/** 所有条数 */
	public int max_page;
	/** 当前页 */
	public int current_page;
	/**  */
	public String name;
	/** 8050评分接口专用 */
	//TODO：以后修改
	public String raterScore;
	/**评论用*/
	//TODO：以后改
	public int total;
	/**游戏竞赛排名显示头像用*/
	public String img_address;

	public String listName;

}
