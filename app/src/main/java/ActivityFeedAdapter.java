import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ActivityFeedAdapter extends FragmentPagerAdapter {

    private static final int GROUPS = 0;
    private static final int BILLS = 1;

    private static final int[] TABS = new int[]{GROUPS, BILLS};
    private Context mContext;

    public ActivityFeedAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
