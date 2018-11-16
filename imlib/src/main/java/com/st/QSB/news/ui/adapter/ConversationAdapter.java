package com.st.QSB.news.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.st.QSB.news.model.entity.Conversation;
import com.st.QSB.news.model.entity.NomalConversation;
import com.st.QSB.news.ui.widget.CircleImageView;
import com.st.QSB.news.utils.TimeUtils;
import com.st.SQB.R;

import java.util.List;


/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<NomalConversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<NomalConversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.name);
            viewHolder.avatar = view.findViewById(R.id.avatar);
            viewHolder.lastMessage = view.findViewById(R.id.last_message);
            viewHolder.time = view.findViewById(R.id.message_time);
            viewHolder.unread = view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);
        viewHolder.tvName.setText(data.getName());
        String ava = data.getAvatar();
        if(TextUtils.isEmpty(ava)) ava = "";
        Glide.with(view.getContext()).load(Uri.parse(ava)).error(R.drawable.ic_head).into(viewHolder.avatar);
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtils.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackgroundResource(R.drawable.point1);
            }else{
                viewHolder.unread.setBackgroundResource(R.drawable.point2);
                if (unRead > 99){
                    unReadStr = "99+";
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
}
