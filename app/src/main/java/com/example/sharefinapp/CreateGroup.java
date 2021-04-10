package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateGroup extends AppCompatActivity {
    private final ArrayList<User> userList = new ArrayList<>();
    private final ArrayList<String> userIDList = new ArrayList<>();
    private  EditText groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Slide());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // add the back arrow
        groupName = findViewById(R.id.editTextGroupName);
    }

    // add the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void createGroup(View v)
    {
        ArrayList<EditText> userFieldList = getEditTextFields();
//        ArrayList<String> users = new ArrayList<>();



        if (isValidGroupInput(groupName,userFieldList))
        {
            List<String> userEmailsFromField = new ArrayList<>();
            // loop through the editText fields to get just the filled in fields and add them to a users list
            for (int i = 0; i < userFieldList.size(); i++)
            {
                // if the field is is not empty then add it to the list
                if (!userFieldList.get(i).getText().toString().isEmpty()) {
                    userEmailsFromField.add(userFieldList.get(i).getText().toString());
                }
            }
            getUsers(userEmailsFromField);


        }
    }

    public void getUsers(List<String> userEmails) {
        User user = new User();
        Log.v("test getUser() called", "created user object, starting query");
        DBManager.getInstance().getDb().collection("users").whereIn("userEmail", userEmails).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds : queryDocumentSnapshots)
                {
                    userList.add(ds.toObject(User.class));
                    Log.v("test getUserIDs() adding: ", ds.toObject(User.class).getDisplayName());
                }
                insertGroup();
            }
        });
    }
    public void insertGroup()
    {
        for (int i=0; i< userList.size(); i++) {
            userIDList.add(userList.get(i).getUserID());
            Log.v("test insertGroup() adding userID: ", userList.get(i).getUserID());
        }
        // add current user to the list for the group
        userIDList.add(DBManager.getInstance().getCurrentUserID());
        Log.v("test users List in Groups", userList.toArray().toString());

        String groupID = DBManager.getInstance().generateKey("groups");
        Group group = new Group(groupName.getText().toString(),userIDList,groupID, Calendar.getInstance().getTime());
        DBManager.getInstance().insertData("groups",groupID ,group);
        finish();
    }

    // todo add further verification checks against existing users
    public boolean isValidGroupInput(EditText groupName, ArrayList<EditText> userFieldList)
    {
        boolean validInput = true;

        if (groupName.getText().toString().isEmpty())
        {
            validInput = false;
            groupName.setError("Please enter a group name");
        }
        int validCounter = 0;
        for (int i = 0; i < userFieldList.size(); i++)
        {
            if (!userFieldList.get(i).getText().toString().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userFieldList.get(i).getText().toString()).matches()) {
                validInput = false;
                userFieldList.get(i).setError("Please enter a valid email address");
            }
            else if(!userFieldList.get(i).getText().toString().isEmpty())       // if the field is not empty
            {
//                boolean isValidEmail = DBManager.getInstance().isValidData("users","userEmail",userFieldList.get(i).getText().toString());    // check whether the user exists the in database - async db causing issues with this implementation
//                if (isValidEmail)
                    validCounter++;
//                else {
//                    validInput = false;
//                    Toast.makeText(this, userFieldList.get(i).getText().toString() + " is invalid",Toast.LENGTH_LONG).show();
//                }
            }
        }

        if (validCounter == 0)
        {
            validInput = false;
            Toast.makeText(this,"Please enter a user to add",Toast.LENGTH_LONG).show();
        }



        return validInput;
    }

    // get the textFields for the user emails in the group creation screen
    public ArrayList<EditText> getEditTextFields()
    {
        ArrayList<EditText> userEditTexts = new ArrayList<>();
        LinearLayout userFieldsLayout = findViewById(R.id.addUsersToGroupLayoutParent);
        for (int i = 0; i < userFieldsLayout.getChildCount(); i++)
        {
            Log.v("test Check UserFields", "Checking Field: " + userFieldsLayout.getChildAt(i).toString());
            if (userFieldsLayout.getChildAt(i) instanceof EditText)
                userEditTexts.add((EditText) userFieldsLayout.getChildAt(i));
        }
        return userEditTexts;
    }


    // not implemented - more dynamic method to add/remove fields in the group creation screen
    public void onDeleteUserFromGroupCreation(View v)
    {

    }


}