import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ActivityFeedAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final int tabCount;


    public ActivityFeedAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        this.context = context;
        this.tabCount = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 1:
                BillsFragment billsFragment = new BillsFragment();
                return billsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
