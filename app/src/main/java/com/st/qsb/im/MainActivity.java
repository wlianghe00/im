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

    }

    public void go(View view) {
        ChatActivity.navToChat(this, "admin1", TIMConversationType.C2C, 1);
    }

    public void go1(View view) {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

}
