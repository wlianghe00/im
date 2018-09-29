package com.st.QSB.news.utils;

import android.app.Application;

import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

public class IManager {
    private Application application;
    private static IManager manager = new IManager();

    public static IManager getInstance() {
        return manager;
    }

    public Application getApplication() {
        return application;
    }

    public void init(Application app, final int resId) {
        application = app;
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
}
