package za.co.vehmon.application.ui;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;

/**
 * Created by Renaldo on 1/25/2015.
 */
public class MessageViewAdapter extends SingleTypeAdapter<Message> {

    private android.view.LayoutInflater inflater;
    private Activity myActivity;

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.ll_messageIndicator,R.id.tv_messageText,R.id.ll_message
        };
    }

    @Override
    protected void update(int i, Message message) {


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg = getItem(position);
        View rowView = null;

        BootstrapApplication app = (BootstrapApplication)myActivity.getApplicationContext();
        if (msg.getTo().equals(app.getUser().getUsername()) || msg.getFrom().equals(app.getUser().getUsername()))
        {
            rowView = inflater.inflate(R.layout.message_view_list_item_right, parent, false);
        }
        else
        {
            rowView = inflater.inflate(R.layout.message_view_list_item_left, parent, false);
        }

        TextView tvMessage = (TextView) rowView.findViewById(R.id.tv_messageText);
        LinearLayout llMessageIndicator = (LinearLayout) rowView.findViewById(R.id.ll_messageIndicator);
        LinearLayout llMessage = (LinearLayout) rowView.findViewById(R.id.ll_message);

        tvMessage.setText(msg.getMessage());

        return rowView;
    }

    /**
     * @param inflater
     * @param items
     */
    public MessageViewAdapter(final LayoutInflater inflater, final List<Message> items, Activity myActivity) {
        super(inflater, R.layout.message_view_list_item);
        this.inflater = inflater;
        setItems(items);
        this.myActivity = myActivity;
    }

    /**
     * @param inflater
     */
    public MessageViewAdapter(final LayoutInflater inflater) {
        this(inflater, null,null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).getMessageConversationID());
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }
}
