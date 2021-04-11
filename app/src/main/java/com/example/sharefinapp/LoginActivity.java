package com.example.sharefinapp;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //    private LoginViewModel loginViewModel;
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button startLogin = findViewById(R.id.startLoginButton);

        startLogin.setOnClickListener(view -> setSignOnIntent());
        setSignOnIntent();


    }

    public void setSignOnIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().
                        setAvailableProviders(providers).setTheme(R.style.Theme_ShareFinApp).build()
                    ,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // if sign-in was successful
            if (resultCode == RESULT_OK)
            {   Log.v("isNewUser", String.valueOf(response.isNewUser()));
                if (response.isNewUser() == true)
                {
                    addNewUserData();   // if its a new user, then add their info in the database
                }
                Intent activityFeedIntent = new Intent(this, ActivityFeed.class);

                startActivity(activityFeedIntent);
            }
        }
    }

    // add new user data to the users collection
    public void addNewUserData()
    {
        User user = new User(DBManager.getInstance().getCurrentUserID(), DBManager.getInstance().getCurrentUserEmail(),DBManager.getInstance().getCurrentUserDisplayName());
        String path = getString(R.string.users_path);

        // insert the user data with a unique id generated by firestore
        DBManager.getInstance().insertData(path,user);
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



}