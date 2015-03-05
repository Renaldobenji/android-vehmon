package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class Message {

    public int MessageConversationID;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getMessageConversationID() {
        return MessageConversationID;
    }

    public void setMessageConversationID(int messageConversationID) {
        MessageConversationID = messageConversationID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String Message;
    public String Date;

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

    public String From;
    public String To;

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int messageID;

    public int getMessageServerConversationID() {
        return MessageServerConversationID;
    }

    public void setMessageServerConversationID(int messageServerConversationID) {
        MessageServerConversationID = messageServerConversationID;
    }

    public int MessageServerConversationID;
}
