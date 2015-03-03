package za.co.vehmon.application.core;

import android.content.Context;

import java.util.List;

import za.co.vehmon.application.datasource.MessageConversationDatasource;
import za.co.vehmon.application.datasource.MessagesDatasource;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class MessageWrapper {

    public class MessageResult
    {
        public boolean isSuccessful() {
            return IsSuccessful;
        }

        public void setSuccessful(boolean isSuccessful) {
            IsSuccessful = isSuccessful;
        }

        public String getErrorMessage() {
            return ErrorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            ErrorMessage = errorMessage;
        }

        public boolean IsSuccessful;
        public String ErrorMessage;

        public List<MessageConversation> getMessageConversations() {
            return MessageConversations;
        }

        public void setMessageConversations(List<MessageConversation> messageConversations) {
            MessageConversations = messageConversations;
        }

        public List<MessageConversation> MessageConversations;

        public List<Message> getMessages() {
            return Messages;
        }

        public void setMessages(List<Message> messages) {
            Messages = messages;
        }

        public List<Message> Messages;
    }

    public MessageResult CreateNewConversation(Context context, String from, String to, int conversationID)
    {
        MessageResult result = new MessageResult();

       try {
           MessageConversationDatasource msgConvo = new MessageConversationDatasource(context);
           msgConvo.InsertMessageConversation(VehmonCurrentDate.GetCurrentDate(), from, to,conversationID);
       }catch (Exception e)
       {
            String renaldo = "Renaldo";
       }
       return result;
    }

    public MessageResult GetAllMessage(Context context)
    {
        MessageResult result = new MessageResult();

        try {
            MessageConversationDatasource msgConvo = new MessageConversationDatasource(context);
            result.setMessageConversations(msgConvo.GetAllConversations());
            result.setSuccessful(true);
        }catch (Exception e)
        {
            String renaldo = "Renaldo";
        }
        return result;
    }

    public MessageResult GetAllMessageForConversation(Context context, long convoID)
    {
        MessageResult result = new MessageResult();

        try
        {
            MessagesDatasource ds = new MessagesDatasource(context);
            result.setMessages(ds.GetMessagesForConversation(convoID));
            result.setSuccessful(true);
        }
        catch (Exception ex)
        {
            String renaldo = "Renaldo";
        }

        return result;

    }

    public  MessageResult SubmitMessage(Context context, Integer conversationID, String from, String to, String message)
    {
        MessageResult result = new MessageResult();

        try
        {
            MessagesDatasource ds = new MessagesDatasource(context);
            long messageID = ds.InsertMessage(conversationID, message,from,to,VehmonCurrentDate.GetCurrentDate());
            if (messageID == -1)
                result.setSuccessful(false);
            else {
                result = GetAllMessageForConversation(context,conversationID);
            }
        }
        catch (Exception ex)
        {
            String renaldo = "Renaldo";
        }

        return result;
    }
}
