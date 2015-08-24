package com.xweisoft.wx.family.ui.load;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.WXApplication;
import com.xweisoft.wx.family.logic.global.GlobalConstant;
import com.xweisoft.wx.family.logic.global.GlobalVariable;
import com.xweisoft.wx.family.logic.global.HttpAddressProperties;
import com.xweisoft.wx.family.logic.initialize.InitManager;
import com.xweisoft.wx.family.logic.model.ChildrenItem;
import com.xweisoft.wx.family.logic.model.UserItem;
import com.xweisoft.wx.family.logic.model.response.ChooseChildrenResp;
import com.xweisoft.wx.family.logic.model.response.LoginResp;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.pc.LoginActivity;
import com.xweisoft.wx.family.ui.sliding.WXMainFragmentActivity;
import com.xweisoft.wx.family.util.HttpRequestUtil;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.util.SecurityUtil;
import com.xweisoft.wx.family.util.SharedPreferencesUtil;
import com.xweisoft.wx.family.util.Util;
import com.xweisoft.wx.family.widget.NetHandler;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoadActivity extends BaseActivity
{
    /**
     * 是否点击了返回按钮
     */
    private boolean isBack = false;
    
    private String phone;
    
    private String password;
    
    private ChildrenItem childrenItem;
    
    /**
     * 登录Handler
     */
    private NetHandler loginHandler = new NetHandler(false)
    {
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            LoginResp resp = null;
            if (null != msg.obj && msg.obj instanceof LoginResp)
            {
                resp = (LoginResp) msg.obj;
                UserItem item = new UserItem();
                item.phone = phone;
                item.password = password;
               /* item.parentSex = resp.parentSex;
                item.userId = resp.userId;
                item.children = resp.children;*/
                LoginUtil.saveLoginInfo(mContext, item);
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String cookieStr = gson.toJson(WXApplication.getInstance().cookieList);
                SharedPreferencesUtil.saveSharedPreferences(mContext,
                        SharedPreferencesUtil.SP_KEY_LOGIN_COOKIE,
                        cookieStr);
                if (ListUtil.isEmpty(item.children))
                {
                    showToast("无法连接值服务器，请稍后再试");
                    finish();
                }
                else
                {
                    String childrenId = SharedPreferencesUtil.getSharedPreferences(mContext,
                            GlobalConstant.UserInfoPreference.CHILDREN,
                            "");
                    if (TextUtils.isEmpty(childrenId))
                    {
                        startActivity(new Intent(mContext,
                        		LoginActivity.class));
                        finish();
                    }
                    else
                    {
                        boolean login = false;
                        for (ChildrenItem children : item.children)
                        {
                            if (null != children
                                    && childrenId.equals(children.studentId))
                            {
                                login = true;
                                childrenItem = children;
                            }
                        }
                        if (login)
                        {
                            Map<String, String> param = HttpRequestUtil.getCommonParams("");
                            param.put("studentId", childrenItem.studentId);
                            HttpRequestUtil.sendHttpPostCommonRequest(mContext,
                                    HttpAddressProperties.CHOOSE_CHILDREN,
                                    param,
                                    ChooseChildrenResp.class,
                                    handler);
                        }
                        else
                        {
                            startActivity(new Intent(mContext,
                                    LoginActivity.class));
                            finish();
                        }
                    }
                }
            }
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        
        @Override
        public void netTimeout()
        {
            super.netTimeout();
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        
        @Override
        public void networkErr()
        {
            super.networkErr();
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        
        @Override
        public void otherErr()
        {
            super.otherErr();
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        
    };
    
    private Handler handler = new NetHandler(false)
    {
        
        @Override
        public void onResponse()
        {
            super.onResponse();
        }
        
        @Override
        public void onSuccess(String errMsg, Message msg)
        {
            ChooseChildrenResp resp = null;
            if (null != msg.obj && msg.obj instanceof ChooseChildrenResp)
            {
                resp = (ChooseChildrenResp) msg.obj;
                if (null != childrenItem)
                {
                    if (null != WXApplication.getInstance().loginUserItem)
                    {
                        childrenItem.classinfoId = resp.classinfoId;
                        childrenItem.classinfoName = resp.classinfoName;
                        WXApplication.getInstance().groupId = resp.groupId;
                        WXApplication.getInstance().selectedItem = childrenItem;
                    }
                    SharedPreferencesUtil.saveSharedPreferences(mContext,
                            GlobalConstant.UserInfoPreference.CHILDREN,
                            childrenItem.studentId);
                    WXApplication.getInstance().selectStudentId = childrenItem.studentId;
                    SharedPreferencesUtil.saveSharedPreferences(mContext,
                            GlobalConstant.UserInfoPreference.CLASSINFOID,
                            childrenItem.classinfoId);
                }
            }
            startActivity(new Intent(mContext, WXMainFragmentActivity.class));
            finish();
        }
        
        @Override
        public void onFailed(String errCode, String errMsg, Message msg)
        {
            finish();
        }
        
        @Override
        public void netTimeout()
        {
            super.netTimeout();
            finish();
        }
        
        @Override
        public void networkErr()
        {
            super.networkErr();
            finish();
        }
        
        @Override
        public void otherErr()
        {
            super.otherErr();
            finish();
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //解决程序在安装完成后点击打开按钮后 程序按home键后再次进入程序一直重启无法后台运行的bug
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }
        setContentView(R.layout.wx_loading);
        GlobalVariable.currentActivity = this;
        Util.getStatusBarHeight(this);
        if (SharedPreferencesUtil.getSharedPreferences(this,
                GlobalConstant.REMEMBER_PASSWORD,
                false))
        {
            phone = SharedPreferencesUtil.getSharedPreferences(LoadActivity.this,
                    GlobalConstant.UserInfoPreference.TELPHONE,
                    "");
            password = SharedPreferencesUtil.getSharedPreferences(LoadActivity.this,
                    GlobalConstant.UserInfoPreference.PASSWORD,
                    "");
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password))
            {
                new Handler().postDelayed(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        Map<String, String> reqMap = HttpRequestUtil.getCommonParams("");
                        reqMap.put("phone", phone);
                        reqMap.put("password", SecurityUtil.MD5(password));
                        HttpRequestUtil.sendHttpPostCommonRequest(LoadActivity.this,
                                HttpAddressProperties.LOGIN_URL,
                                reqMap,
                                LoginResp.class,
                                loginHandler);
                    }
                },
                        2000);
            }
            else
            {
                toLoginActivity();
            }
        }
        else
        {
            toLoginActivity();
        }
    }
    
    private void toLoginActivity()
    {
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                InitManager.getInstance().initClient(LoadActivity.this);
                if (!isBack)
                {
                    Intent intent = new Intent(LoadActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            isBack = true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    public void initViews()
    {
        
    }
    
    @Override
    public void bindListener()
    {
        
    }
    
    @Override
    public int getActivityLayout()
    {
        return 0;
    }
}
