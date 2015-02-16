package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.BootstrapServiceProvider;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.MessageConversation;
import za.co.vehmon.application.core.MessageWrapper;
import za.co.vehmon.application.core.User;
import za.co.vehmon.application.core.VehmonService;
import za.co.vehmon.application.ui.Dialogs.NewMessageDialog;
import za.co.vehmon.application.util.SafeAsyncTask;

import static za.co.vehmon.application.core.Constants.Extra.USER;

/**
 * Created by Renaldo on 1/21/2015.
 */
public class MessageListFragment extends ItemListFragment<MessageConversation>{

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No Messages");
    }

    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu, final MenuInflater inflater) {
        inflater.inflate(R.menu.message_menu, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!isUsable()) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.newMessage:
                showNewMessageDialog();
                return true;
            case R.id.logout:
                this.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNewMessageDialog() {
        AlertDialog.Builder messageRecipient = new AlertDialog.Builder(getActivity());
        messageRecipient.setMessage("Choose recipient here");
        messageRecipient.setCancelable(true);
        messageRecipient.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateNewMessage();
                        dialog.cancel();
                    }
                });
        messageRecipient.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = messageRecipient.create();
        alert11.show();
    }

    private void CreateNewMessage()
    {
        final ProgressDialog barProgressDialog = ProgressDialog.show(getActivity(),"Please wait...", "Creating Message",true);
        new SafeAsyncTask<MessageWrapper.MessageResult>() {
            @Override
            public MessageWrapper.MessageResult call() throws Exception {
                final MessageWrapper.MessageResult svc = serviceProvider.getService(getActivity()).CreateNewMessage(getActivity(),"Renaldo", "Robyn");
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
                barProgressDialog.dismiss();
                forceRefresh();
            }
        }.execute();
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_users;
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(1);

        getListAdapter().addHeader(activity.getLayoutInflater()
                .inflate(R.layout.message_list_item_label, null));
    }

    @Override
    protected SingleTypeAdapter<MessageConversation> createAdapter(List<MessageConversation> items) {
        return new MessageListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final MessageConversation msgConversation = ((MessageConversation) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), MessageViewActivity.class).putExtra("msgConversationID",msgConversation.getMessageConversationID()));
    }

    @Override
    public void onLoadFinished(final Loader<List<MessageConversation>> loader, final List<MessageConversation> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    public Loader<List<MessageConversation>> onCreateLoader(int id, Bundle args) {
        final List<MessageConversation> initialItems = items;
        return new ThrowableLoader<List<MessageConversation>>(getActivity(), items) {
            @Override
            public List<MessageConversation> loadData() throws Exception {

                try {
                    List<MessageConversation> latest = null;

                    if (getActivity() != null) {
                        MessageWrapper.MessageResult result = serviceProvider.getService(getActivity()).GetAllMessageConversations(getActivity());

                        if (result.isSuccessful())
                            latest = result.getMessageConversations();
                        else
                            latest = null;
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };
    }
}
