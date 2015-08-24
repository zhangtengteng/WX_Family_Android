package com.xweisoft.wx.family.logic.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.xweisoft.wx.family.logic.model.ChildrenItem;

/**
 * 登录响应
 * 
 * @author 李晨光
 * @version [版本号, 2014年6月30日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LoginResp extends CommonResp {
	/**
	 * 注释内容
	 */
	// private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	// public String userId;

	/**
	 * 性别
	 */
	// public String parentSex;

	/**
	 * 孩子集合
	 */
	// public ArrayList<ChildrenItem> children;

	public String ID;
	public String UserName;
	public String RealName;
	public String PassWord;
	public String UserTel;
	public String CityID;
	public String CreateTime;
	public String UpdateTime;
	public String state;
	public String Position;
	public String ClientType;
	public String LastLoginIP;
	public String AppCode;
	public String Money;
	public String HeadImgUrl;
	public String RealImgUrl;
	public String VipLevel;
	public String WantedCard;
	public String CityName;

}
