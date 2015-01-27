package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

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
import za.co.vehmon.application.core.User;
import za.co.vehmon.application.core.VehmonService;

import static za.co.vehmon.application.core.Constants.Extra.USER;

/**
 * Created by Renaldo on 1/21/2015.
 */
public class MessageListFragment extends ItemListFragment<MessageConversation> {

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
                        latest = serviceProvider.getService(getActivity()).FetchUnreadMessage();
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
