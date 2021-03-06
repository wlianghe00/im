package com.st.QSB.news.ui.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.st.SQB.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener {

    private static final String TAG = "ChatInput";

    private ImageButton btnAdd, btnSend, btnVoice, btnKeyboard;
    private EditText editText;
    private boolean isSendVisible, isHoldVoiceBtn, isEmoticonReady;
    private InputMode inputMode = InputMode.NONE;
    private ChatView chatView;
    private LinearLayout morePanel, textPanel;
    private TextView voicePanel;
    private LinearLayout ll_voice;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    private boolean canChat = true;


    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        initView();
    }

    private void initView() {
        textPanel = findViewById(R.id.text_panel);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnVoice = findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(this);
        morePanel = findViewById(R.id.morePanel);
        LinearLayout BtnImage = findViewById(R.id.btn_photo);
        BtnImage.setOnClickListener(this);
        LinearLayout BtnPhoto = findViewById(R.id.btn_image);
        BtnPhoto.setOnClickListener(this);
        LinearLayout btnVideo = findViewById(R.id.btn_video);
        btnVideo.setOnClickListener(this);
        setSendBtn();
        btnKeyboard = findViewById(R.id.btn_keyboard);
        btnKeyboard.setOnClickListener(this);
        ll_voice = findViewById(R.id.ll_voice);
        voicePanel = findViewById(R.id.voice_panel);
        ll_voice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startY = 0;
                float endY = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        isHoldVoiceBtn = true;
                        updateVoiceView(false);
                        break;

                    case MotionEvent.ACTION_UP:
                        endY = event.getY();
                        if(isHoldVoiceBtn) {
                            isHoldVoiceBtn = false;
                            updateVoiceView(true);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        int instance = (int) Math.abs((moveY - startY));
                        if (instance > 200) {
                            isHoldVoiceBtn = false;
                            updateVoiceView(false);
                        }
                        break;
                }
                return true;
            }
        });
        editText = findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateView(InputMode.TEXT);
                }
            }
        });
        isSendVisible = editText.getText().length() != 0;
    }

    private void updateView(InputMode mode) {
        if (mode == inputMode) return;
        leavingCurrentState();
        switch (inputMode = mode) {
            case MORE:
                morePanel.setVisibility(VISIBLE);
                btnAdd.setBackgroundResource(R.drawable.ic_im_close);
                break;

            case TEXT:
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                btnAdd.setBackgroundResource(R.drawable.ic_more);
                break;

            case VOICE:
                btnAdd.setBackgroundResource(R.drawable.ic_more);
                ll_voice.setVisibility(VISIBLE);
                textPanel.setVisibility(GONE);
                btnVoice.setVisibility(GONE);
                btnKeyboard.setVisibility(VISIBLE);
                break;
        }
    }

    private void leavingCurrentState() {
        switch (inputMode) {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                break;

            case MORE:
                morePanel.setVisibility(GONE);
                break;

            case VOICE:
                ll_voice.setVisibility(GONE);
                textPanel.setVisibility(VISIBLE);
                btnVoice.setVisibility(VISIBLE);
                btnKeyboard.setVisibility(GONE);
                break;
        }
    }


    private void updateVoiceView(boolean send) {
        if (isHoldVoiceBtn) {
            voicePanel.setText(getResources().getString(R.string.chat_release_send));
            voicePanel.setTextColor(Color.WHITE);
            ll_voice.setBackgroundResource(R.drawable.btn_voice_pressed);
            chatView.startSendVoice();
        } else {
            voicePanel.setText(getResources().getString(R.string.chat_press_talk));
            voicePanel.setTextColor(getResources().getColor(R.color.voice_default_color));
            ll_voice.setBackgroundResource(R.drawable.btn_voice_normal);
            chatView.endSendVoice(send);
        }
    }


    /**
     * 关联聊天界面逻辑
     */
    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
        if (isSendVisible) {
            chatView.sending();
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSendBtn() {
        if (isSendVisible) {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Activity activity = (Activity) getContext();
        int id = v.getId();
        if (id == R.id.btn_send) {
            if(!checkCanChat()) return;
            chatView.sendText();
        }
        if (id == R.id.btn_add) {
            updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE);
        }
        if (id == R.id.btn_photo) {
            if(!checkCanChat()) return;
            if (activity != null && requestCamera(activity)) {
                chatView.sendPhoto();
            }
        }
        if (id == R.id.btn_image) {
            if(!checkCanChat()) return;
            if (activity != null && requestStorage(activity)) {
                chatView.sendImage();
            }
        }
        if (id == R.id.btn_voice) {
            if(!checkCanChat()) return;
            if (activity != null && requestAudio(activity)) {
                updateView(InputMode.VOICE);
            }
        }
        if (id == R.id.btn_keyboard) {
            updateView(InputMode.TEXT);
        }
        if (id == R.id.btn_video) {
            if(!checkCanChat()) return;
            if (getContext() instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) getContext();
                if (requestVideo(fragmentActivity)) {
                    VideoInputDialog.show(fragmentActivity.getSupportFragmentManager());
                }
            }
        }
    }

    public void setCanChat(boolean canChat) {
        this.canChat = canChat;
    }

    public boolean checkCanChat() {
        if(!canChat) {
            Toast.makeText(getContext(), "订单已结束，无法发送消息，如有疑问请联系在线客服", Toast.LENGTH_SHORT).show();
        }
        return canChat;
    }


    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text) {
        editText.setText(text);
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode) {
        updateView(mode);
    }


    public enum InputMode {
        TEXT,
        VOICE,
        MORE,
        VIDEO,
        NONE,
    }

    private boolean requestVideo(Activity activity) {
        if (afterM()) {
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestCamera(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestAudio(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestStorage(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
