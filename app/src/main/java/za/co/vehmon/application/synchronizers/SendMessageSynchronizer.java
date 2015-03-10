package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.datasource.MessagesDatasource;
import za.co.vehmon.application.services.MessageResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/10/2015.
 */
public class SendMessageSynchronizer implements ISynchronize {
    @Override
    public SynchronizedResult Synchronize(final Context context,final VehmonServiceProvider serviceProvider) {
        final MessagesDatasource msgDS = new MessagesDatasource(context);
        List<Message> messages = msgDS.GetUnsyncedMessages(context);

        for (final Message msg : messages) {
            try {
                new SafeAsyncTask<MessageResponse>() {
                    @Override
                    public MessageResponse call() throws Exception {
                        final MessageResponse svc = serviceProvider.getService(context).SendMessageToServer(String.valueOf(msg.getMessageServerConversationID()), msg.getDate(), msg.getMessage());
                        return svc;
                    }

                    @Override
                    protected void onException(final Exception e) throws RuntimeException {
                        super.onException(e);
                        if (e instanceof OperationCanceledException) {
                        }
                    }

                    @Override
                    protected void onSuccess(final MessageResponse response) throws Exception {
                        super.onSuccess(response);

                        if (response == null)
                            return;

                        if (response.MessageStatus == "0") {
                            msgDS.updateSynced(msg.getMessageID());
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
