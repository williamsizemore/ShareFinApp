package com.example.sharefinapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class FirebaseDBManager extends AppCompatActivity implements DbManagerInterface {
    private final FirebaseAuth fbAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user;


    // used to make this a singleton
    private static final FirebaseDBManager dbMgrInstance = new FirebaseDBManager();

    // make this object a singleton
    public static FirebaseDBManager getInstance() {
        return dbMgrInstance;
    }

    FirebaseDBManager() {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        fbAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void addNewUserData()
    {
        String userID = user.getUid();
        String userEmail = user.getEmail();
        String firstName = user.getDisplayName();
        String lastName = null;
        User user = new User(userID, userEmail, firstName, lastName);

        db.collection("users").document(userEmail).set(user);



    }


}