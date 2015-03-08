package za.co.vehmon.application.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.MessageWrapper;
import za.co.vehmon.application.services.ConversationResponse;
import za.co.vehmon.application.services.UserDetailContract;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/3/2015.
 */
public class NewMessageActivity extends BootstrapActivity {

    @InjectView(R.id.listMessageContacts) protected ListView listMessageContacts;
    @Inject protected VehmonServiceProvider serviceProvider;
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
                setResult(2);
                finish();
            }
        });
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

                ContactListAdapter adapter = new ContactListAdapter(myActivity.getLayoutInflater(), response);
                listMessageContacts.setAdapter(adapter);

                barProgressDialog.dismiss();
            }
        }.execute();
    }

    private void CreateNewMessage(final Activity activity, final String to)
    {
        final ProgressDialog barProgressDialog = ProgressDialog.show(this,"Please wait...", "Creating Message",true);
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
                barProgressDialog.dismiss();
            }

            @Override
            protected void onSuccess(final MessageWrapper.MessageResult isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);
                barProgressDialog.dismiss();
            }
        }.execute();
    }


}
