package com.st.QSB.news.presenter;

import android.util.Log;

import com.st.QSB.news.model.entity.Conversation;
import com.st.QSB.news.model.entity.NomalConversation;
import com.st.QSB.news.model.event.AccountRequestEvent;
import com.st.QSB.news.model.event.MessageEvent;
import com.st.QSB.news.model.event.RefreshEvent;
import com.st.QSB.news.ui.widget.ConversationView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 会话界面逻辑
 */
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public ConversationPresenter(ConversationView view){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        this.view = view;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                view.updateMessage(msg);
            }
        }else if (observable instanceof RefreshEvent){
            view.refresh();
        }
    }



    public void getConversation(){
        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
        List<TIMConversation> result = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        for (TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System) continue;
            result.add(conversation);
            accounts.add(conversation.getPeer());
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateMessage(timMessages.get(0));
                    }
                }
            });

        }
        //发送事件去请求数据
        AccountRequestEvent event = new AccountRequestEvent();
        event.userIds = accounts;
        EventBus.getDefault().post(event);

        view.initView(result);
    }

    /**
     * 删除会话
     *
     * @param type 会话类型
     * @param id 会话对象id
     */
    public boolean delConversation(TIMConversationType type, String id){
        return TIMManager.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


    public void updateData(List<NomalConversation> accounts, List<NomalConversation> conversationList) {
        for (NomalConversation local: conversationList) {
            for (NomalConversation net : accounts) {
                if(local.getIdentify().equals(net.userId)) {
                    local.name = net.name;
                    local.avator = net.avator;
                    local.userId = net.userId;
                    local.userType = net.userType;
                    break;
                }
            }
        }
    }
}
