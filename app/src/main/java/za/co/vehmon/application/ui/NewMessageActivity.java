package za.co.vehmon.application.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.MessageListRefreshEvent;
import za.co.vehmon.application.core.MessageWrapper;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.services.ConversationResponse;
import za.co.vehmon.application.services.UserDetailContract;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/3/2015.
 */
public class NewMessageActivity extends BootstrapActivity {

    @InjectView(R.id.listMessageContacts) protected ListView listMessageContacts;
    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject Bus eventBus;
    Activity myActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Select Contact");

        FetchContactsFromServer();
        this.myActivity = this;

        listMessageContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CreateNewMessage(myActivity,((UserDetailContract)listMessageContacts.getAdapter().getItem(position)).UserName);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    /**
     * Posts a {@link za.co.vehmon.application.core.StopTimerEvent} message to the {@link com.squareup.otto.Bus}
     */
    private void produceMessageListRefresh() {
        eventBus.post(new MessageListRefreshEvent());
    }

    private void FetchContactsFromServer()
    {
        final ProgressDialog barProgressDialog = ProgressDialog.show(this, "Please wait...", "Fetching Contacts", true);
        final Activity myActivity = this;
        new SafeAsyncTask<List<UserDetailContract>>() {
            @Override
            public List<UserDetailContract> call() throws Exception {
                final List<UserDetailContract> svc = serviceProvider.getService(myActivity).GetAllUsersFromServer();
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
            protected void onSuccess(final List<UserDetailContract> response) throws Exception {
                super.onSuccess(response);

                if (response == null || response.isEmpty())
                    return;

                BootstrapApplication app = (BootstrapApplication)myActivity.getApplicationContext();
                String username = app.getUser().getUsername();

                //Remove logged in user from the list
                int i=-1;
                int valueToRemove = -1;
                for (UserDetailContract user:response)
                {
                    i++;
                    if (user.UserName.toUpperCase().equals(username.toUpperCase()))
                    {
                        valueToRemove = i;
                    }
                }
                if (valueToRemove != -1)
                    response.remove(valueToRemove);

                ContactListAdapter adapter = new ContactListAdapter(myActivity.getLayoutInflater(), response);
                listMessageContacts.setAdapter(adapter);

                barProgressDialog.dismiss();
            }
        }.execute();
    }

    private void CreateNewMessage(final Activity activity, final String to)
    {
        //final ProgressDialog barProgressDialog = ProgressDialog.show(this,"Please wait...", "Creating Message",true);
        new SafeAsyncTask<MessageWrapper.MessageResult>() {
            @Override
            public MessageWrapper.MessageResult call() throws Exception {
                BootstrapApplication app = (BootstrapApplication)activity.getApplicationContext();
                String username = app.getUser().getUsername();
                ConversationResponse response = serviceProvider.getService(activity).CreateConversation(String.format("%1$s Conversation %2$s",username,to),to);
                if (response.CreateStatus.equals("0")) {
                    final MessageWrapper.MessageResult svc = serviceProvider.getService(activity).CreateNewMessage(activity, app.getUser().getUsername(), to,response.ConversationId);
                    return svc;
                }
                return null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof android.accounts.OperationCanceledException) {
                }
                //barProgressDialog.dismiss();
            }

            @Override
            protected void onSuccess(final MessageWrapper.MessageResult isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);
                produceMessageListRefresh();
            }
        }.execute();
    }


}
