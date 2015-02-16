package za.co.vehmon.application.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.core.MessageWrapper;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 1/25/2015.
 */
public class MessageViewActivity extends BootstrapActivity {

    private List<Message> messageItems;
    private Integer messageConversationID;

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
        setTitle("Messages" + messageConversationID);
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

                MessageViewAdapter adapter = new MessageViewAdapter(myActivity.getLayoutInflater(), isSuccessful.getMessages());
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
                final MessageWrapper.MessageResult svc = serviceProvider.getService(myActivity).SubmitMessage(myActivity,messageConversationID,"Renaldo","Robyn", editTextMessage.getText().toString());
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

                MessageViewAdapter adapter = new MessageViewAdapter(myActivity.getLayoutInflater(), isSuccessful.getMessages());
                listViewMessages.setAdapter(adapter);
                editTextMessage.setText("");
                barProgressDialog.dismiss();
            }
        }.execute();
    }

    private MessageConversation setupConvo()
    {
        MessageConversation convo = new MessageConversation();
        convo.setFrom("Renaldo");
        convo.setTo("Robyn");
        convo.setMessageConversationID(5);
        convo.setDate("2015-01-12");

        Message msg = new Message();
        msg.setTo("Renaldo");
        msg.setFrom("Robyn");
        msg.setDate("2014-05-31");
        msg.setMessage("hello, how are you? Im Robyn an you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg);

        Message msg1 = new Message();
        msg1.setFrom("Renaldo");
        msg1.setTo("Robyn");
        msg1.setDate("2014-05-31");
        msg1.setMessage("hey hey, Im Renaldo, Im the Best");
        msg1.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg1);

        Message msg2 = new Message();
        msg2.setTo("Renaldo");
        msg2.setFrom("Robyn");
        msg2.setDate("2014-05-31");
        msg2.setMessage("Yeah i know you are the best, help me to become the best?");
        msg2.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg2);

        Message msg3 = new Message();
        msg3.setFrom("Renaldo");
        msg3.setTo("Robyn");
        msg3.setDate("2014-05-31");
        msg3.setMessage("I cant, i was born with it, i cant help you?");
        msg3.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg3);

        Message msg5 = new Message();
        msg5.setFrom("Renaldo");
        msg5.setTo("Robyn");
        msg5.setDate("2014-05-31");
        msg5.setMessage("Im Sorry");
        msg5.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg5);

        Message msg4 = new Message();
        msg4.setTo("Renaldo");
        msg4.setFrom("Robyn");
        msg4.setDate("2014-05-31");
        msg4.setMessage("Ok No problem, i understand");
        msg4.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg4);

        Message msg7 = new Message();
        msg7.setTo("Renaldo");
        msg7.setFrom("Robyn");
        msg7.setDate("2014-05-31");
        msg7.setMessage("Ok No problem, i understand");
        msg7.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg7);


        Message msg6 = new Message();
        msg6.setTo("Renaldo");
        msg6.setFrom("Robyn");
        msg6.setDate("2014-05-31");
        msg6.setMessage("Ok No problem, i understand");
        msg6.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg6);

        convo.Messages.add(msg3);

        return convo;
    }
}
