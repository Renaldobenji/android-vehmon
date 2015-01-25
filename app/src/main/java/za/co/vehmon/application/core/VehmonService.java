
package za.co.vehmon.application.core;

import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class VehmonService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public VehmonService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public VehmonService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * TODO: Need to implement Service that authenticates user
     * @param email
     * @param password
     * @return
     */
    public User Authenticate(String email, String password) {
        User user = new User();
        user.setFirstName("Renaldo");
        user.setLastName("Benjamin");
        user.setObjectId("#12345");
        user.setPhone("0722771137");
        user.setUsername("Renaldob");
        user.setSessionToken("#SessionToken");
        return user;
    }

    public boolean ClockIn(Date date)
    {
        return true;
    }

    public boolean ClockOut(Date date)
    {
        return true;
    }

    public String[] FetchAbsenceTypes()
    {
        return new AbsenceRequestWrapper().FetchAbsenceTypes();
    }

    public AbsenceRequestWrapper.AbsenceRequestResult SubmitAbsenceRequest(int absenceRequestTypeID, Date fromDate, Date toDate)
    {
        return new AbsenceRequestWrapper().SubmitAbsenceRequestToServer(absenceRequestTypeID,fromDate,toDate);
    }

    public List<MessageConversation> FetchUnreadMessage()
    {
        List<MessageConversation> messages = new ArrayList<MessageConversation>();

        MessageConversation conversation = new MessageConversation();
        conversation.setFrom("Renaldo");
        conversation.setTo("Robyn");
        conversation.setMessageConversationID(1);
        conversation.setDate("2015-01-02");
        setupConvo(conversation);
        messages.add(conversation);

        MessageConversation conversation1 = new MessageConversation();
        conversation1.setFrom("Renaldo");
        conversation1.setTo("Robyn");
        conversation1.setMessageConversationID(2);
        conversation1.setDate("2015-01-05");
        setupConvo(conversation1);
        messages.add(conversation1);

        MessageConversation conversation2 = new MessageConversation();
        conversation2.setFrom("Renaldo");
        conversation2.setTo("Robyn");
        conversation2.setMessageConversationID(3);
        conversation2.setDate("2015-01-07");
        setupConvo(conversation2);
        messages.add(conversation2);

        MessageConversation conversation3 = new MessageConversation();
        conversation3.setFrom("Renaldo");
        conversation3.setTo("Robyn");
        conversation3.setMessageConversationID(4);
        conversation3.setDate("2015-01-09");
        setupConvo(conversation3);
        messages.add(conversation3);

        MessageConversation conversation4 = new MessageConversation();
        conversation4.setFrom("Renaldo");
        conversation4.setTo("Robyn");
        conversation4.setMessageConversationID(5);
        conversation4.setDate("2015-01-12");
        setupConvo(conversation4);
        messages.add(conversation4);

        return messages;
    }

    private void setupConvo(MessageConversation convo)
    {
        Message msg = new Message();
        msg.setTo("Renaldo");
        msg.setFrom("Robyn");
        msg.setDate("2014-05-31");
        msg.setMessage("hello, how are you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg);

        Message msg1 = new Message();
        msg1.setFrom("Renaldo");
        msg1.setTo("Robyn");
        msg1.setDate("2014-05-31");
        msg1.setMessage("hello, how are you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg1);

        Message msg2 = new Message();
        msg2.setTo("Renaldo");
        msg2.setFrom("Robyn");
        msg2.setDate("2014-05-31");
        msg2.setMessage("hello, how are you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg2);

        Message msg3 = new Message();
        msg3.setFrom("Renaldo");
        msg3.setTo("Robyn");
        msg3.setDate("2014-05-31");
        msg3.setMessage("hello, how are you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg3);

        Message msg4 = new Message();
        msg4.setTo("Renaldo");
        msg4.setFrom("Robyn");
        msg4.setDate("2014-05-31");
        msg4.setMessage("hello, how are you?");
        msg.setMessageConversationID(convo.getMessageConversationID());
        convo.Messages.add(msg4);
    }
}