package za.co.vehmon.application.ui.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.InjectView;
import za.co.vehmon.application.R;

/**
 * Created by Renaldo on 2/14/2015.
 */
public class NewMessageDialog extends DialogFragment
{
    @InjectView(R.id.txt_your_name) protected EditText content;
    private EditText mEditText;

    public interface NewMessageDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public NewMessageDialog()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_message, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        getDialog().setTitle("New Message");
        getDialog().setCancelable(true);
        return view;
    }

}
