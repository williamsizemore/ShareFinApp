package com.example.sharefinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.HashMap;

public class CreateBill extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private EditText reminderDateField, billNameField,amountField;
    private TextView user1, user2, user3, user4,user5;
    private EditText user1amount, user2amount, user3amount, user4amount, user5amount;
    private Spinner groupField;
    private Spinner splitTypeField;
    private ArrayList<Group> groups;
    private ArrayList<User> users;
    private DatePickerDialog.OnDateSetListener date;
    private Group selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Slide());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        /* setup the custom actionbar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);  // add the back arrow button
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setCustomView(R.layout.create_bill_action_bar);    //todo change out to do a custom action bar with save icon included
        groupField = findViewById(R.id.create_bill_group_spinner);


        reminderDateField = findViewById(R.id.create_bill_reminder_date);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        reminderDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        user1 = findViewById(R.id.bill_create_user1);
        user1amount = findViewById(R.id.bill_create_user1_amount);

        user2 = findViewById(R.id.bill_create_user2);
        user2amount = findViewById(R.id.bill_create_user2_amount);

        user3 = findViewById(R.id.bill_create_user3);
        user3amount = findViewById(R.id.bill_create_user3_amount);

        user4 = findViewById(R.id.bill_create_user4);
        user4amount = findViewById(R.id.bill_create_user4_amount);

        user5 = findViewById(R.id.bill_create_user5);
        user5amount = findViewById(R.id.bill_create_user5_amount);

        populateGroupSelection();

        getWindow().setExitTransition(new Slide());

    }
    // open the date dialog
    public void setReminderDate(View view)
    {
        new DatePickerDialog(CreateBill.this, date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onStart() {

        super.onStart();
        groupField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getUsersInSelectedGroup(groupField.getSelectedItem().toString()); // todo issues here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateDateLabel()
    {
        String dateFormat = "dd-MMM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        reminderDateField.setText(sdf.format(calendar.getTime()));
    }

    // add the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            getWindow().setExitTransition(new Slide());
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // get and save the fields to the server
    public void onBillSave(View view) throws Exception
    {
        Log.v("Bill Save","Attempting to create and save bill");
        String billName, category, groupName, splitType, recurrence;
        double amount;
        Date reminderDate, createDate;
        HashMap<String, Double> billSplit = new HashMap<>();

        // todo - grab all the textfield data and clean/process it
        // use some of global fields above
        billNameField = findViewById(R.id.create_bill_name);


        Spinner categoryField = findViewById(R.id.create_bill_category);
        category = categoryField.getSelectedItem().toString();


        groupName = groupField.getSelectedItem().toString();

        amountField = findViewById(R.id.create_bill_amount);

        splitTypeField = findViewById(R.id.create_bill_split_type_spinner);
        splitType = splitTypeField.getSelectedItem().toString();
        String[] split_type = getResources().getStringArray(R.array.bill_split_type);


        Spinner recurrenceField = findViewById(R.id.create_bill_recurrence);
        recurrence = recurrenceField.getSelectedItem().toString();

//        SimpleDateFormat sdf = new SimpleDateFormat(reminderDateField.getText().toString());
        reminderDate = (calendar.getTime());
        Log.v("reminderDate",reminderDate.toString());

        if (validateFields())
        {
            // category, groupName, splitType, recurrence, reminderDate
            createDate =  Calendar.getInstance().getTime();

            amount = Double.parseDouble(amountField.getText().toString());

            Log.v("validateFields: users", String.valueOf(users.size()));
            if (splitType.equals(split_type[0]))
            {
                billSplit.put(users.get(0).getUserEmail(),Double.parseDouble(user1amount.getText().toString()));    // put user1's split of the bill into the hashmap
                billSplit.put(users.get(1).getUserEmail(), Double.parseDouble((user2amount.getText().toString()))); // put user2's split of the bill into the hashmap

                if (user3amount.getVisibility() == View.VISIBLE)
                    billSplit.put(users.get(2).getUserEmail(),Double.parseDouble(user3amount.getText().toString()));
                if (user4amount.getVisibility() == View.VISIBLE)
                    billSplit.put(users.get(3).getUserEmail(), Double.parseDouble(user4amount.getText().toString()));
                if (user5amount.getVisibility() == View.VISIBLE)
                    billSplit.put(users.get(4).getUserEmail(), Double.parseDouble(user5amount.getText().toString()));
            }
            // if it is based on percentages, it will need to be converted to dollar values first
            else if (splitType.equals(split_type[1]))
            {
                double split1, split2, split3, split4, split5;
                split1 = (Double.parseDouble(user1amount.getText().toString()) / 100) * amount;
                billSplit.put(users.get(0).getUserEmail(),split1);

                split2 = (Double.parseDouble(user2amount.getText().toString()) / 100) * amount;
                billSplit.put(users.get(1).getUserEmail(),split2);

                if (user3amount.getVisibility() == View.VISIBLE) {
                    split3 = (Double.parseDouble(user3amount.getText().toString()) / 100) * amount;
                    billSplit.put(users.get(2).getUserEmail(),split3);
                }
                if (user4amount.getVisibility() == View.VISIBLE) {
                    split4 = (Double.parseDouble(user4amount.getText().toString()) / 100) * amount;
                    billSplit.put(users.get(3).getUserEmail(),split4);
                }
                if (user5amount.getVisibility() == View.VISIBLE){
                    split5 = (Double.parseDouble(user5amount.getText().toString()) / 100) * amount;
                    billSplit.put(users.get(4).getUserEmail(),split5);
                }
            }
            billName = billNameField.getText().toString();


            if (categoryField.getSelectedItem().toString().equals("Category"))
                category = null;
            else
                category = categoryField.getSelectedItem().toString();

            recurrence = recurrenceField.getSelectedItem().toString();

            // set the values of the bill
            Bill bill = new Bill();
            bill.setName(billName);
            bill.setAmountDue(amount);
            bill.setAmountPaid(0.00);
            bill.setCategory(category);
            bill.setCreateDate(createDate);
            bill.setRemindDate(reminderDate);
            bill.setGroupID(selectedGroup.getGroupID());
            bill.setDescription(null);
            bill.setPhotoURI(null);
            bill.setRecurring(recurrence);
            bill.setSplitType(splitType);
            bill.setBillSplit(billSplit);

            DBManager.getInstance().insertData("bills",bill);
            // todo create another method to add the bill to the calendar
            Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
            Calendar calendarEvent = Calendar.getInstance();
            calendarIntent.setType("vnd.android.cursor.item/event");
            calendarIntent.putExtra("beginTime",calendar.getTimeInMillis());
            calendarIntent.putExtra("endTime",calendar.getTimeInMillis() + 60 * 60 * 1000);
            calendarIntent.putExtra("allDay",true);
            calendarIntent.putExtra("rule","FREQ=" + recurrence.toUpperCase());
            calendarIntent.putExtra("title",billName + " due");
            startActivityForResult(calendarIntent, RESULT_OK);

            Toast.makeText(this, "Success!", Toast.LENGTH_LONG);
            finish();
        }
        else Log.v("onBillSave","Cancelled save attempt, valid data no entered");
    }

    /*
        validate all the fields and amounts for the create bill screen
     */
    public boolean validateFields()
    {

        // bill name check
        if (billNameField.getText().toString().isEmpty()) {
            billNameField.setError("Please Enter a bill name");
            return false;
        }
        if (user1amount.getText().toString().isEmpty()) {
            user1amount.setError("Enter an amount");
            return false;
        }
        if (user2amount.getText().toString().isEmpty()) {
            user2amount.setError("Enter an amount");
            return false;
        }

        if (user3amount.getVisibility() == View.VISIBLE) {
            if (user3amount.getText().toString().isEmpty())
            {
                user3amount.setError("Enter an amount");
                return false;
            }
        }
        if (user4amount.getVisibility() == View.VISIBLE) {
            if (user4amount.getText().toString().isEmpty()) {
                user4amount.setError("Enter an amount");
                return false;
            }
        }
        if (user5amount.getVisibility() == View.VISIBLE) {
            if (user5amount.getText().toString().isEmpty()) {
                user5amount.setError("Enter an amount");
                return false;
            }
        }
        if (reminderDateField.getText().toString().isEmpty()) {
            reminderDateField.setError("Please select a date");
            return false;
        }

        try {
            Double.parseDouble(amountField.getText().toString());
        }
        catch(Exception e)
        {
            Log.e("Error Parsing AmountField in onBillSave: ",e.getStackTrace().toString());
            amountField.setError("Please enter a billable amount");
            return false;
        }
        String[] split_type = getResources().getStringArray(R.array.bill_split_type);
        // if split by amount is selected, then check the totals to see if they add up
        if (splitTypeField.getSelectedItem().toString().equals(split_type[0]))
        {
            double amount = Double.parseDouble(amountField.getText().toString());
            double totalEntered = 0.00;

            totalEntered += Double.parseDouble(user1amount.getText().toString());
            totalEntered += Double.parseDouble(user2amount.getText().toString());

            if (user3amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user3amount.getText().toString());
            if(user4amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user4amount.getText().toString());
            if (user5amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user5amount.getText().toString());

            if (Double.compare(amount,totalEntered) == 0)
            {
                Log.v("createBill amount validation: ", "Amounts match");
            }
            else if (Double.compare(amount,totalEntered) > 0)
            {
                Toast.makeText(this,"Short by $" + (amount - totalEntered),Toast.LENGTH_LONG).show();
                return false;
            }
            else if (Double.compare(amount,totalEntered) < 0)
            {
                Toast.makeText(this, "Over by $" + (totalEntered - amount), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        // else if split by percentage is selected
        else {
            double totalEntered = 0.00;

            totalEntered += Double.parseDouble(user1amount.getText().toString());
            totalEntered += Double.parseDouble(user2amount.getText().toString());

            if (user3amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user3amount.getText().toString());
            if(user4amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user4amount.getText().toString());
            if (user5amount.getVisibility() == View.VISIBLE)
                totalEntered += Double.parseDouble(user5amount.getText().toString());

            if (Double.compare(totalEntered, 100) == 0)
            {
                Log.v("createBill amount validation: ", "Percentage correct at 100%");
            }
            else if (totalEntered < 100)
            {
                Toast.makeText(this,"Short by " + (100 - totalEntered) + "%",Toast.LENGTH_LONG).show();
                return false;
            }
            else if (totalEntered > 100)
            {
                Toast.makeText(this,"Over by " + (totalEntered - 100) + "%",Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    // add the groups the user is found in to the group selection spinnner
    public void populateGroupSelection()
    {
        ArrayList<Object> objects = new ArrayList<>();
        DBManager.getInstance().getDb().collection("groups").whereArrayContains("groupUserIDs", DBManager.getInstance().getCurrentUserID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                    {
                        objects.add(doc.toObject(Group.class));
                    }
                    addGroupsToSpinner(objects);
                }
                else
                    Log.d("test getGroups Query", String.valueOf(task.getException()));
            }
        });
    }
    public void addGroupsToSpinner(ArrayList<Object> objects) {
         groups = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++)
        {
            groups.add((Group) objects.get(i));
        }

        Log.v("test populateGroupSelection group: ",groups.toString());

        ArrayList<String> groupNames = new ArrayList<>();
        for (int i=0; i < groups.size(); i++)
        {
            groupNames.add(groups.get(i).getGroupName());
            Log.v("test populateGroupSelection groupNames: ", groups.get(i).getGroupName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner groupSpinner = findViewById(R.id.create_bill_group_spinner);
        groupSpinner.setAdapter(arrayAdapter);
    }

    public void getUsersInSelectedGroup(String groupName)
    {
        Log.v("test getUsersInSelectedGroup: groupName = ", groupName);
        // get the list of users in the group
//        Group selectedGroup = DBManager.getInstance().getGroup(groupName);    // getGroup has issue of returning before query finishes
        selectedGroup = new Group();
        int i = 0;
        while (i < groups.size()) {
            if (groups.get(i).getGroupName().equals(groupName)) {
                selectedGroup.setGroupName(groups.get(i).getGroupName());
                selectedGroup.setGroupUserIDs( groups.get(i).getGroupUserIDs());    // potentially empty - need to do a clean run todo
                selectedGroup.setGroupID((groups.get(i).getGroupID()));
                Log.v("test getUsersInSelectedGroup: setGroupName", groups.get(i).getGroupName());
            }

            i++;
        }

        // then get the user objects by searching for those users (by email) and getting them into User objects
//        users = DBManager.getInstance().getUsers(selectedGroup.getGroupUsers());
        users = new ArrayList<>();

        DBManager.getInstance().getDb().collection("users").whereIn("userID",selectedGroup.getGroupUserIDs()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
//                    userList.forEach(x -> task.getResult().toObjects(User.class));
                    for (QueryDocumentSnapshot doc : task.getResult())
                        users.add(doc.toObject(User.class));
                    Log.d("userList in getUsersInSelectedGroup: ", users.toArray().toString());
                    setUserFields(users);
                }

            }
        });


        Log.v("getUsers - attempting to add User objects: ", users.toArray().toString());

        // then add the names to the userfields and unhide the number of users in the group

    }

    public void setUserFields(ArrayList<User> users)
    {

        clearUserFields();

        // I realize this is not the best practice but did not have time to make this dynamic using layout inflater and list iterator
        // the architecture still allows this to be dynamic, just the limited time to implement did not

        if (users.size() >= 2)
        {
            user1.setText(users.get(0).getDisplayName());
            user2.setText(users.get(1).getDisplayName());
        }
        if(users.size() >= 3) {
            user3.setText(users.get(2).getDisplayName());
            user3.setVisibility(View.VISIBLE);
            user3amount.setVisibility(View.VISIBLE);

        }
        if(users.size() >= 4) {
            user4.setText(users.get(3).getDisplayName());
            user4.setVisibility(View.VISIBLE);
            user4amount.setVisibility(View.VISIBLE);
        }
        if(users.size()>=5) {
            user5.setText(users.get(4).getDisplayName());
            user5.setVisibility(View.VISIBLE);
            user5amount.setVisibility(View.VISIBLE);
        }
    }
    void clearUserFields()
    {
        user3.setVisibility(View.GONE);
        user3amount.setVisibility(View.GONE);

        user4.setVisibility(View.GONE);
        user4amount.setVisibility(View.GONE);

        user5.setVisibility(View.GONE);
        user5amount.setVisibility(View.GONE);

        user1.setText("User");
        user2.setText("User");
        user3.setText("User");
        user4.setText("User");
        user5.setText("User");

        user1amount.setText("");
        user2amount.setText("");
        user3amount.setText("");
        user4amount.setText("");
        user5amount.setText("");
    }

}