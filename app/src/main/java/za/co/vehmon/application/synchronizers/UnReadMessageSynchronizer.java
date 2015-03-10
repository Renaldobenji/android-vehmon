package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.AbsenceRequest;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.datasource.MessageConversationDatasource;
import za.co.vehmon.application.datasource.MessagesDatasource;
import za.co.vehmon.application.services.MessageResponse;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 3/5/2015.
 */
public class UnReadMessageSynchronizer implements ISynchronize {

    @Override
    public SynchronizedResult Synchronize(final Context context,final VehmonServiceProvider serviceProvider) {
        SynchronizedResult result = new SynchronizedResult();
        result.setSynchronizedSuccessful(true);

        try {
            final MessageConversationDatasource msgConvoDS = new MessageConversationDatasource(context);
            final MessagesDatasource msgDS = new MessagesDatasource(context);

            new SafeAsyncTask<List<MessageResponse>>() {
                @Override
                public List<MessageResponse> call() throws Exception {
                    final List<MessageResponse> unreadMessages = serviceProvider.getService(context).SyncUnreadMessagesFromServer();
                    return unreadMessages;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final List<MessageResponse> response) throws Exception {
                    super.onSuccess(response);

                    if (response == null)
                        return;

                    if (response.isEmpty())
                        return;

                    for (final MessageResponse msg : response) {
                        long conversationID;
                        MessageConversation conv = msgConvoDS.GetConversation(msg.ConversationId);
                        if (conv == null)
                        {
                            conversationID = msgConvoDS.InsertMessageConversation(VehmonCurrentDate.GetCurrentDate(),msg.Sender,"",msg.ConversationId);
                        }
                        else
                        {
                            conversationID = conv.getMessageConversationID();
                        }
                        msgDS.InsertMessage(conversationID,msg.Message,msg.Sender,"",VehmonCurrentDate.GetCurrentDate(),1);
                    }
                }
            }.execute();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

}
