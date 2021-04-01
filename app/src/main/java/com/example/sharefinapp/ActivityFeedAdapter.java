package com.example.sharefinapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class ActivityFeedAdapter extends FragmentStateAdapter {

    private final int tabCount;
    private final Context context;

    public ActivityFeedAdapter(Context context, int tabCount) {
        super((FragmentActivity) context);
        this.context = context;
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        return tabCount;
    }
}
