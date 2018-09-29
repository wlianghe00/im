package com.st.qsb.im;

import android.app.Application;

import com.st.QSB.news.utils.IManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IManager.getInstance().init(this, R.mipmap.ic_launcher);
    }
}
