package za.co.vehmon.application.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import za.co.vehmon.application.R;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.services.UserDetailContract;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class ContactListAdapter extends SingleTypeAdapter<UserDetailContract> {
    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.iv_Messageavatar,R.id.lblContactName
        };
    }

    @Override
    protected void update(int i, UserDetailContract contract) {
        imageView(0).setBackgroundResource(R.drawable.gravatar_icon);
        setText(1, String.format("%1$s", contract.FirstName));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * @param inflater
     * @param items
     */
    public ContactListAdapter(final LayoutInflater inflater, final List<UserDetailContract> items) {
        super(inflater, R.layout.new_message_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public ContactListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).UserID);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

}
