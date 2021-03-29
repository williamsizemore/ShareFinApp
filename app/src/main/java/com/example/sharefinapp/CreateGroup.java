package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.FirebaseUiException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


    }

    public void createGroup(View v)
    {
        ArrayList<EditText> userFieldList = (ArrayList<EditText>) getEditTextFields();
        ArrayList<String> users = new ArrayList<>();

        // loop through the editText fields to get just the filled in fields and add them to a users list
        for (int i = 0; i < userFieldList.size(); i++)
        {
            if (!userFieldList.get(i).getText().toString().isEmpty())
                users.add(userFieldList.get(i).getText().toString());
        }
        users.add(DBManager.getInstance().getCurrentUserEmail());

        Log.v("users List in Groups",users.toString());

        //TODO - add to the database
        //      this might have to be done through Strings instead of USERID... idk
    }

    // get the textFields for the user emails in the group creation screen
    public ArrayList<EditText> getEditTextFields()
    {
        ArrayList<EditText> userEditTexts = new ArrayList<>();
        LinearLayout userFieldsLayout = (LinearLayout) findViewById(R.id.addUsersToGroupLayoutParent);
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