package com.example.sharefinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.firebase.ui.firestore.FirestoreArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateBill extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private EditText reminderDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        /* setup the custom actionbar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);  // add the back arrow button
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setCustomView(R.layout.create_bill_action_bar);    //todo change out to do a custom action bar with save icon included

        reminderDate = findViewById(R.id.create_bill_reminder_date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        reminderDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateBill.this, date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        populateGroupSelection();

    }

    @Override
    public void onStart() {

        super.onStart();
        populateGroupSelection();
    }

    public void updateDateLabel()
    {
        String dateFormat = "dd-MMM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        reminderDate.setText(sdf.format(calendar.getTime()));
    }

    // add the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // get and save the fields to the server
    public void onBillSave(View view)
    {
        //todo implement
        Log.v("Bill Save","Attempting to create and save bill");
    }

    // add the groups the user is found in to the group selection spinnner
    public void populateGroupSelection()
    {
//        QuerySnapshot query = DBManager.getInstance().getGroups();
        ArrayList<Group> objects = new ArrayList<>();
        DBManager.getInstance().getDb().collection("groups").whereArrayContains("groupUsers", DBManager.getInstance().getCurrentUserEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                    {
                        objects.add(doc.toObject(Group.class));
                    }
                }
                else
                    Log.d("getGroups Query", String.valueOf(task.getException()));
            }
        });


        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++)
        {
            groups.add((Group) objects.get(i));
        }

        Log.v("populateGroupSelection group: ",groups.toString());

        ArrayList<String> groupNames = new ArrayList<>();
        for (int i=0; i < groups.size(); i++)
        {
            groupNames.add(groups.get(i).getGroupName());
            Log.v("populateGroupSelection groupNames: ", groups.get(i).getGroupName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner groupSpinner = findViewById(R.id.create_bill_group_spinner);
        groupSpinner.setAdapter(arrayAdapter);
    }

}