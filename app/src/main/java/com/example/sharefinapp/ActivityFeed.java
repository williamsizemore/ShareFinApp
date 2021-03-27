package com.example.sharefinapp;

import android.animation.Animator;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

//import com.example.sharefinapp.ui.main.SectionsPagerAdapter;
/* reference for FAB buttons: https://stackoverflow.com/questions/30699302/android-design-support-library-expandable-floating-action-buttonfab-menu */
public class ActivityFeed extends AppCompatActivity implements View.OnClickListener {
    private boolean isFABOpen = false;
    private FloatingActionButton addGroupFAB, addBillFAB, addButton;
    private LinearLayout addGroupFABLayout, addBillFABLayout, addButtonLayout;
    private Animation fab_open, fab_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        /* add the floating action button functionality */
        addButtonLayout = findViewById(R.id.fabAddButtonLayout);
        addGroupFABLayout = findViewById(R.id.fabAddGroupLayout);
        addBillFABLayout = findViewById(R.id.fabAddBillLayout);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addGroupFAB = findViewById(R.id.fabAddGroup);
        addBillFAB = findViewById(R.id.fabAddBill);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        addButton.setOnClickListener(this);
        addGroupFAB.setOnClickListener(this);
        addBillFAB.setOnClickListener(this);

        Log.v("user",FirebaseAuth.getInstance().getCurrentUser().toString());


    }
    @Override
    public void onClick (View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.addButton:
                Log.v("isFABOpen", String.valueOf(isFABOpen));
                if (!isFABOpen)
                    showFABMenu();
                else closeFABMenu();
                break;
            case R.id.fabAddGroup:
                Log.v("addGroup","Add Group was selected");
                //TODO
                break;
            case R.id.fabAddBill:
                Log.v("addBill","Add bill was selected");
                //todo
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

    public void showFABMenu()
    {

        addBillFABLayout.setVisibility(View.VISIBLE);
        addGroupFABLayout.setVisibility(View.VISIBLE);
        addButtonLayout.animate().rotationBy(135);

        addGroupFABLayout.startAnimation(fab_open);
        addBillFABLayout.startAnimation(fab_open);
        addGroupFAB.setClickable(true);
        addBillFAB.setClickable(true);
        isFABOpen = true;

    }

    public void closeFABMenu()
    {
        addButtonLayout.animate().rotation(0);
        addGroupFAB.startAnimation(fab_close);
        addBillFAB.startAnimation(fab_close);
        addGroupFABLayout.setVisibility(View.GONE);
        addBillFABLayout.setVisibility(View.GONE);
        addGroupFAB.setClickable(false);
        addBillFAB.setClickable(false);
        isFABOpen = false;
    }
}