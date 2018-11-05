package com.st.qsb.im;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.st.QSB.news.model.event.RefreshEvent;
import com.st.QSB.news.ui.activity.ChatActivity;
import com.st.QSB.news.ui.activity.ConversationActivity;
import com.st.QSB.news.utils.IManager;
import com.tencent.TIMConversationType;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IManager.getInstance().init(getApplication(), R.mipmap.ic_launcher,1400128918, 36941, true);
        IManager.getInstance().initTim("5b9f70a8c9e77c00012f7236", "eJxlkF1PgzAYhe-5FYTbGdePdaUmXkwhi4HNLcyoVwTbstUpdKUQyeJ-F6uJJL63z3Ny8p6z5-t*sEuzy4Lzuq1sbnstA--KD0Bw8Qe1ViIvbI6N*Aflh1ZG5kVppXEQQ4LnYLiRpISsrCrVr0JeWElBEXImKeWDClFJEZ6PEo045q7XBeDs2wkZDMeK2ju4ih9u727YkXWYdZpk9CTIpogEizjZpvSQsSiRnSJv97Lcm81poeIFbJLl9Hny1PftOuGv8GDaZJutjYnlo56mu1U1SWe6rpZtfD2qtOr9Z57hxxAigBAc0U6aRtWVExCABCIM3BDep-cFf3xhWA__");

    }

    public void go(View view) {
        ChatActivity.navToChat(this, "admin1", TIMConversationType.C2C, 1);
    }

    public void go1(View view) {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

}
