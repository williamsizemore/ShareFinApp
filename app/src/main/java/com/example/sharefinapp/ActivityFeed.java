package com.example.sharefinapp;

import android.animation.Animator;
import android.content.Intent;
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
import android.widget.TextView;

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
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        /* add the floating action button functionality */
        textViewAddBill = findViewById(R.id.textView_addBill);    // mini FAB label
        textViewAddGroup = findViewById(R.id.textView_addGroup);  // mini FAB label
        addButton =  findViewById(R.id.addButton);              // main add button
        addGroupFAB = findViewById(R.id.fabAddGroup);           // mini FAB
        addBillFAB = findViewById(R.id.fabAddBill);             // mini FAB

        /* setup the animations for the FAB buttons */
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fab_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_clockwise);
        fab_counterClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotation_anticlockwise);

        /* set the onclick listeners to the method implemented in this class */
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
                if (!isFABOpen)
                    showFABMenu();
                else closeFABMenu();
                break;
            case R.id.fabAddGroup:
                Log.v("addGroup","Add Group was selected");
                Intent createGroupIntent = new Intent(this, CreateGroup.class);
                startActivity(createGroupIntent);
                break;
            case R.id.fabAddBill:
                Log.v("addBill","Add bill was selected");
                Intent createBillIntent = new Intent(this, CreateBill.class);
                startActivity(createBillIntent);
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
}