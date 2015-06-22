package za.co.vehmon.application.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import za.co.vehmon.application.R;
import za.co.vehmon.application.core.ShiftReportContract;
import za.co.vehmon.application.services.LeaveRequestContract;

/**
 * Created by Renaldo on 6/8/2015.
 */
public class ViewShftsListAdapter extends SingleTypeAdapter<ShiftReportContract> {

    private LayoutInflater inflater;
    @Override
    protected int[] getChildViewIds() {
        return new int[]
        {
            R.id.tv_FromDateValue,R.id.tv_ToDateValue,R.id.tv_Hours
        };
    }

    @Override
    protected void update(int i, ShiftReportContract leaveRequestContract) {
        setText(0, String.format("%1$s",leaveRequestContract.StartDate));
        setText(1, String.format("%1$s",leaveRequestContract.EndDate));
        double d = Double.parseDouble(leaveRequestContract.MinutesWorked);
        setText(2, String.format("%.2f min", d));
    }
    /**
     * @param inflater
     * @param items
     */
    public ViewShftsListAdapter(final LayoutInflater inflater, final List<ShiftReportContract> items) {
        super(inflater, R.layout.view_shifts_detail_item);

        this.inflater = inflater;
        setItems(items);
    }

    /**
     * @param inflater
     */
    public ViewShftsListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        return position;
    }
}
