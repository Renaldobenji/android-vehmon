package za.co.vehmon.application.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class MessageConversation {
    public MessageConversation()
    {
        Messages = new ArrayList<Message>();
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String Date;

    public String From;
    public String To;

    public List<Message> getMessages() {
        return Messages;
    }

    public void setMessages(List<Message> messages) {
        Messages = messages;
    }

    public List<Message> Messages;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public int getMessageConversationID() {
        return MessageConversationID;
    }

    public void setMessageConversationID(int messageConversationID) {
        MessageConversationID = messageConversationID;
    }

    public int MessageConversationID;

}
