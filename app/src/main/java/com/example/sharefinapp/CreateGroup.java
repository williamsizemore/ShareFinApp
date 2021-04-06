package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // add the back arrow

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
    public void createGroup(View v)
    {
        ArrayList<EditText> userFieldList = getEditTextFields();
        ArrayList<String> users = new ArrayList<>();
        EditText groupName = findViewById(R.id.editTextGroupName);


        if (isValidGroupInput(groupName,userFieldList)) {
            // loop through the editText fields to get just the filled in fields and add them to a users list
            for (int i = 0; i < userFieldList.size(); i++) {
                // if the field is is not empty then add it to the list
                if (!userFieldList.get(i).getText().toString().isEmpty())
                    users.add(userFieldList.get(i).getText().toString());

            }
            // add current user to the list for the group
            users.add(DBManager.getInstance().getCurrentUserEmail());
            Log.v("users List in Groups", users.toString());


            Group group = new Group(groupName.getText().toString(),users);
            DBManager.getInstance().insertData("groups",group);
            finish();
        }

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
            else if(!userFieldList.get(i).getText().toString().isEmpty())
                validCounter++;
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
            Log.v("Check UserFields", "Checking Field: " + userFieldsLayout.getChildAt(i).toString());
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