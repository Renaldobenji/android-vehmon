package za.co.vehmon.application.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import za.co.vehmon.application.R;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.services.LeaveRequestContract;

/**
 * Created by Renaldo on 6/8/2015.
 */
public class ViewLeaveListAdapter extends SingleTypeAdapter<LeaveRequestContract> {
    @Override
    protected int[] getChildViewIds() {
        return new int[]
        {
            R.id.tv_LeaveTypeValue,R.id.tv_FromDateValue,R.id.tv_ToDateValue
        };
    }

    @Override
    protected void update(int i, LeaveRequestContract leaveRequestContract) {
        setText(0, String.format("%1$s",leaveRequestContract.LeaveRequestType));
        setText(1, String.format("%1$s",leaveRequestContract.StartDateTime));
        setText(2, String.format("%1$s",leaveRequestContract.EndDateTime));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * @param inflater
     * @param items
     */
    public ViewLeaveListAdapter(final LayoutInflater inflater, final List<LeaveRequestContract> items) {
        super(inflater, R.layout.view_leave_items);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public ViewLeaveListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        return position;
    }
}
