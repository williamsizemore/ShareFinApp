package com.example.sharefinapp;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharefinapp.ActivityFeed;
import com.example.sharefinapp.FirebaseDBManager;
import com.example.sharefinapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //    private LoginViewModel loginViewModel;
    private static final int RC_SIGN_IN = 123;
    private Button startLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startLogin = findViewById(R.id.startLoginButton);

        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setSignOnIntent();
            }
        });
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
            {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                FirebaseDBManager dbManager = new FirebaseDBManager(user);
                Intent activityFeedIntent = new Intent(this, ActivityFeed.class);
//                activityFeedIntent.putExtra("UserAuth", (Parcelable) dbManager);
                startActivity(activityFeedIntent);
            }
        }
    }
    // sign out the user and exit the application
    public void signOut()
    {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Signed out. Come back Soon!", Toast.LENGTH_LONG);
                        toast.show();
                        finishAndRemoveTask();  // close the app after signing out
                    }
                });
    }



}