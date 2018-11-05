package com.st.QSB.news.utils;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.st.QSB.news.model.event.FriendshipEvent;
import com.st.QSB.news.model.event.RefreshEvent;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

public class IManager implements ILiveLoginManager.TILVBStatusListener {
    private Application application;
    private int resId;
    private int SDK_APPID;
    private int ACCOUNT_TYPE;
    private static IManager manager = new IManager();
    private String userId;
    private String userSig;

    private boolean managerVer = false;  //是否是管理员版本

    public static IManager getInstance() {
        return manager;
    }

    public int getResId() {
        return resId;
    }

    public Application getApplication() {
        return application;
    }

    public boolean isManagerVer() {
        return managerVer;
    }

    public void init(Application app, final int resId, int SDK_APPID, int ACCOUNT_TYPE) {
        init(app, resId, SDK_APPID, ACCOUNT_TYPE, false);
    }

    public void init(Application app, final int resId, int SDK_APPID, int ACCOUNT_TYPE, boolean managerVer) {
        application = app;
        this.resId = resId;
        this.SDK_APPID = SDK_APPID;
        this.ACCOUNT_TYPE = ACCOUNT_TYPE;
        this.managerVer = managerVer;
        if(MsfSdkUtils.isMainProcess(application)) {
            // 设置离线推送监听器
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
//                     这里的doNotify是ImSDK内置的通知栏提醒，应用也可以选择自己利用回调参数notification来构造自己的通知栏提醒
                    notification.doNotify(application.getApplicationContext(), resId);
                }
            });
        }
    }

    private void initTim(String userId, String userSig) {
        this.userId = userId;
        this.userSig = userSig;
        Log.e(IManager.class.getSimpleName(), "开始初始化腾讯im:" + userId + "---" + userSig);
        ILiveSDK.getInstance().initSdk(application.getApplicationContext(), SDK_APPID, ACCOUNT_TYPE);
        RefreshEvent.getInstance();  //设置刷新监听
        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        ILiveLoginManager.getInstance().setUserStatusListener(this);
        loginSDK(userId, userSig);
    }

    public void initTim() {
        if(!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userSig)) {
            initTim(userId, userSig);
        }
    }

    private void loginSDK(final String id, final String userSig) {
        ILiveLoginManager.getInstance().iLiveLogin(id, userSig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e(IManager.class.getSimpleName(),"IM帐号登录成功,id:" + id + "---userSig:" + userSig);
//                EventBus.getDefault().post(new IMLoginEvent(ISATAppConfig.LOAD_SUCCESS));
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(IManager.class.getSimpleName(),"IM帐号登录失败," + module + "--" + errCode + "--" + errMsg);
//                EventBus.getDefault().post(new IMLoginEvent(ISATAppConfig.LOAD_FAIL));
            }
        });
    }

    @Override
    public void onForceOffline(int error, String message) {
        //sign过期，票据重新登录
        Log.e(IManager.class.getSimpleName(),"onUserSigExpired：票据过期，重新登录");
//        EventBus.getDefault().post(new IMStatusEvent());
    }
}
