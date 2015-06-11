package za.co.vehmon.application.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import za.co.vehmon.application.R;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.services.LeaveRequestContract;

/**
 * Created by Renaldo on 6/8/2015.
 */
public class ViewLeaveListAdapter extends SingleTypeAdapter<LeaveRequestContract> {

    private android.view.LayoutInflater inflater;
    @Override
    protected int[] getChildViewIds() {
        return new int[]
        {
            R.id.tv_LeaveTypeValue,R.id.tv_FromDateValue,R.id.tv_ToDateValue,R.id.tv_Status
        };
    }

    @Override
    protected void update(int i, LeaveRequestContract leaveRequestContract) {
        setText(0, String.format("%1$s",leaveRequestContract.LeaveRequestType));
        setText(1, String.format("%1$s",leaveRequestContract.StartDateTime));
        setText(2, String.format("%1$s",leaveRequestContract.EndDateTime));
        setText(3, String.format("%1$s",leaveRequestContract.Status));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeaveRequestContract leaveRequestContract = getItem(position);
        View rowView = null;

        rowView = inflater.inflate(R.layout.view_leave_items, parent, false);

        TextView tv_LeaveTypeValue = (TextView) rowView.findViewById(R.id.tv_LeaveTypeValue);
        tv_LeaveTypeValue.setText(leaveRequestContract.LeaveRequestType);
        TextView tv_FromDateValue = (TextView) rowView.findViewById(R.id.tv_FromDateValue);
        tv_FromDateValue.setText(leaveRequestContract.StartDateTime);
        TextView tv_ToDateValue = (TextView) rowView.findViewById(R.id.tv_ToDateValue);
        tv_ToDateValue.setText(leaveRequestContract.EndDateTime);
        TextView tvMessage = (TextView) rowView.findViewById(R.id.tv_Status);
        tvMessage.setText(leaveRequestContract.Status);

        if (leaveRequestContract.Status.equals("Approved"))
        {
            tvMessage.setTextColor(Color.parseColor("#13B30B"));
        }
        else if (leaveRequestContract.Status.equals("Declined"))
        {
            tvMessage.setTextColor(Color.parseColor("#CC240A"));
        }
        else
        {
            tvMessage.setTextColor(Color.parseColor("#E3CF36"));
        }
        return rowView;
    }

    /**
     * @param inflater
     * @param items
     */
    public ViewLeaveListAdapter(final LayoutInflater inflater, final List<LeaveRequestContract> items) {
        super(inflater, R.layout.view_leave_items);

        this.inflater = inflater;
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
