package za.co.vehmon.application.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.core.MessageWrapper;
import za.co.vehmon.application.services.MessageResponse;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 1/25/2015.
 */
public class MessageViewActivity extends BootstrapActivity {

    private List<Message> messageItems;
    private Integer messageConversationID;
    private String messageTo;

    @InjectView(R.id.listViewMessages) protected ListView listViewMessages;
    @InjectView(R.id.editTextMessage) protected EditText editTextMessage;
    @InjectView(R.id.imageButtonMessageSubmit) protected ImageButton imageButtonMessageSubmit;

    @Inject protected VehmonServiceProvider serviceProvider;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        messageConversationID = getIntent().getExtras().getInt("msgConversationID");
        messageTo = getIntent().getExtras().getString("msgTo");

        setTitle("Message");
        FetchMessageForConvo(messageConversationID);
    }

    private void FetchMessageForConvo(final long msgConID)
    {
        final ProgressDialog barProgressDialog = ProgressDialog.show(this,"Please wait...", "Fetching Messages",true);
        final Activity myActivity = this;
        new SafeAsyncTask<MessageWrapper.MessageResult>() {
            @Override
            public MessageWrapper.MessageResult call() throws Exception {
                final MessageWrapper.MessageResult svc = serviceProvider.getService(myActivity).GetAllMessageForConversation(myActivity,msgConID);
                return svc;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
                barProgressDialog.dismiss();
            }

            @Override
            protected void onSuccess(final MessageWrapper.MessageResult isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);

                MessageViewAdapter adapter = new MessageViewAdapter(myActivity.getLayoutInflater(), isSuccessful.getMessages(),myActivity);
                listViewMessages.setAdapter(adapter);

                barProgressDialog.dismiss();
            }
        }.execute();
    }

    @OnClick(R.id.imageButtonMessageSubmit)
    public void SubmitMessage(View view) {
        final ProgressDialog barProgressDialog = ProgressDialog.show(this, "Sending","Submitting message...", true);
        final Activity myActivity = this;
        new SafeAsyncTask<MessageWrapper.MessageResult>() {
            @Override
            public MessageWrapper.MessageResult call() throws Exception {
                //TODO: Test Message Send Activity
                BootstrapApplication app = (BootstrapApplication)myActivity.getApplicationContext();
                MessageWrapper.MessageResult svc = null;
                final MessageResponse response = serviceProvider.getService(myActivity).SendMessageToServer(messageConversationID.toString(), VehmonCurrentDate.GetCurrentDate().toString(), editTextMessage.getText().toString());
                if (response != null && response.MessageStatus == "0") {
                    svc = serviceProvider.getService(myActivity).SubmitMessage(myActivity,messageConversationID,app.getUser().getUsername(),messageTo, editTextMessage.getText().toString());
                }

                return svc;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
                barProgressDialog.dismiss();
            }

            @Override
            protected void onSuccess(final MessageWrapper.MessageResult isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);
                if (isSuccessful == null)
                {
                    Toast.makeText(getApplicationContext(), "Message Failed, Please try again.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    MessageViewAdapter adapter = new MessageViewAdapter(myActivity.getLayoutInflater(), isSuccessful.getMessages(), myActivity);
                    listViewMessages.setAdapter(adapter);
                    editTextMessage.setText("");
                }
                barProgressDialog.dismiss();
            }
        }.execute();
    }
}
