

package za.co.vehmon.application.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import za.co.vehmon.application.R;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new NewsListFragment();
                break;
            case 1:
                result = new TimeManagementFragment();
                break;
            case 2:
                result = new UserListFragment();
                break;
            case 3:
                result = new CheckInsListFragment();
                break;
            case 4:
                result = new MessageListFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_news);
            case 1:
                return "Time";
            case 2:
                return resources.getString(R.string.page_users);
            case 3:
                return resources.getString(R.string.page_checkins);
            case 4:
                return "Messages";
            default:
                return null;
        }
    }
}
