package za.co.vehmon.application.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.core.User;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class MessageListAdapter extends SingleTypeAdapter<MessageConversation> {
    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.iv_Messageavatar,R.id.tv_messageusername,R.id.tv_messagedate
        };
    }

    @Override
    protected void update(int i, MessageConversation messageConversation) {
        imageView(0).setBackgroundResource(R.drawable.gravatar_icon);
        setText(1, String.format("%1$s", messageConversation.getTo()));
        setText(2, String.format("%1$s", messageConversation.getDate()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * @param inflater
     * @param items
     */
    public MessageListAdapter(final LayoutInflater inflater, final List<MessageConversation> items) {
        super(inflater, R.layout.message_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public MessageListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).getMessageConversationID());
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

}
