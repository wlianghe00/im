package com.st.QSB.news.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.st.QSB.news.ui.fragment.ConversationFragment;
import com.st.QSB.news.utils.IManager;
import com.tencent.ilivesdk.ILiveSDK;

public class ConversationActivity extends AppCompatActivity {
    String selfAva;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        selfAva = intent.getStringExtra("selfAva");
        if (!ILiveSDK.getInstance().getLoginEngine().isLogin()) {//
            Toast.makeText(this, "IM未登录，请稍后再试", Toast.LENGTH_SHORT).show();
            IManager.getInstance().initTim();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
        if (savedInstanceState == null) {  //fragment重叠
            ConversationFragment fragment = new ConversationFragment();
            Bundle bundle = new Bundle();
            bundle.putString("selfAva", selfAva);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, getClass().getSimpleName()).commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        selfAva = intent.getStringExtra("selfAva");
    }
}
