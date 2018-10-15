package com.st.QSB.news.ui.widget;

import com.tencent.TIMConversation;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMMessage;

import java.util.List;

/**
 * 会话列表界面的接口
 */
public interface ConversationView {

    /**
     * 初始化界面或刷新界面
     */
    void initView(List<TIMConversation> conversationList);


    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    void updateMessage(TIMMessage message);

    /**
     * 删除会话
     */
    void removeConversation(String identify);


    /**
     * 刷新
     */
    void refresh();




}
