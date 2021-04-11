package com.example.sharefinapp;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.sharefinapp.ui.main.SectionsPagerAdapter;
/* reference for FAB buttons: https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu */
public class ActivityFeed extends AppCompatActivity implements View.OnClickListener {
    private boolean isFABOpen = false;
    private FloatingActionButton addGroupFAB, addBillFAB, addButton;
    private TextView textViewAddBill, textViewAddGroup;
    private Animation fab_open, fab_close, fab_clockwise, fab_counterClockwise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        TextView title = findViewById(R.id.title);
        title.setText("Welcome, " + DBManager.getInstance().getCurrentUserDisplayName());

        /* setup the TabLayout tabs */
        FragmentManager fm = getSupportFragmentManager();
        ActivityFeedAdapter activityFeedAdapter = new ActivityFeedAdapter(fm, getLifecycle());
        final ViewPager2 viewPager = findViewById(R.id.view_pager_activity_feed);
        viewPager.setAdapter(activityFeedAdapter);


        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position)
            {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /* add the floating action button functionality */
        textViewAddBill = findViewById(R.id.textView_addBill);    // mini FAB label
        textViewAddGroup = findViewById(R.id.textView_addGroup);  // mini FAB label
        addButton =  findViewById(R.id.addButton);              // main add button
        addGroupFAB = findViewById(R.id.fabAddGroup);           // mini FAB
        addBillFAB = findViewById(R.id.fabAddBill);             // mini FAB

//        final ActivityFeedAdapter activityFeedAdapter= new ActivityFeedAdapter(this,  tabLayout.getTabCount());
//        viewPager.setAdapter(activityFeedAdapter);

        /* setup the animations for the FAB buttons */
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fab_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_clockwise);
        fab_counterClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotation_anticlockwise);

        /* set the onclick listeners to the method implemented in this class */
        addButton.setOnClickListener(this);
        addGroupFAB.setOnClickListener(this);
        addBillFAB.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.addButton:
                if (!isFABOpen)
                    showFABMenu();
                else closeFABMenu();
                break;
            case R.id.fabAddGroup:
                Log.v("addGroup","Add Group was selected");
                Intent createGroupIntent = new Intent(this, CreateGroup.class);
                startActivityForResult(createGroupIntent,RESULT_OK, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.fabAddBill:
                Log.v("addBill","Add bill was selected");
                Intent createBillIntent = new Intent(this, CreateBill.class);
                startActivity(createBillIntent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
        }
    }
    /* override the backbutton to close the FAB if opened */
    @Override
    public void onBackPressed() {
        if (!isFABOpen) {
            super.onBackPressed();
        }
        else closeFABMenu();
    }

    // animate the mini FAB's display and make them clickable
    public void showFABMenu()
    {
        addButton.startAnimation(fab_clockwise);

        addGroupFAB.startAnimation(fab_open);
        addBillFAB.startAnimation(fab_open);
        textViewAddBill.setVisibility(View.VISIBLE);
        textViewAddGroup.setVisibility(View.VISIBLE);

        addGroupFAB.setClickable(true);
        addBillFAB.setClickable(true);
        isFABOpen = true;
    }

    // hide the mini FAB's and make them un-clickable
    public void closeFABMenu()
    {
        addButton.startAnimation(fab_counterClockwise);
        addGroupFAB.startAnimation(fab_close);
        addBillFAB.startAnimation(fab_close);
        textViewAddGroup.setVisibility(View.INVISIBLE);
        textViewAddBill.setVisibility(View.INVISIBLE);

        addGroupFAB.setClickable(false);
        addBillFAB.setClickable(false);
        isFABOpen = false;
    }

    // sign out the user and exit the application
    public void signOut(View view)
    {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(task -> {
                    Toast toast = Toast.makeText(getApplicationContext(),"Signed out. Come back Soon!", Toast.LENGTH_LONG);
                    toast.show();
                    finishAndRemoveTask();  // close the app after signing out
                });
    }

    /**
     *  NOT IMPLEMENTED YET
     * @param view
     */
    public void search(View view){
        Toast.makeText(this, "Search Function Not Implemented Yet",Toast.LENGTH_SHORT).show();
    }


}