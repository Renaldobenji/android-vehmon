package za.co.vehmon.application.ui;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;

/**
 * Created by Renaldo on 1/25/2015.
 */
public class MessageViewActivity extends BootstrapActivity {

    private List<Message> messageItems;

    @InjectView(R.id.listViewMessages) protected ListView listViewMessages;
    @Inject protected VehmonServiceProvider serviceProvider;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Messages" + getIntent().getExtras().getInt("msgConversationID"));
        messageItems = setupConvo().getMessages();
        MessageViewAdapter adapter = new MessageViewAdapter(this.getLayoutInflater(), messageItems);
        listViewMessages.setAdapter(adapter);
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
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg4);

        return convo;
    }
}
