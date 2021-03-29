package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class DBManager extends AppCompatActivity implements DbManagerInterface {
    private final FirebaseAuth fbAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user;


    // used to make this a singleton
    private static final DBManager dbMgrInstance = new DBManager();

    // make this object a singleton
    public static DBManager getInstance() {
        return dbMgrInstance;
    }

    DBManager() {
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
        String displayNAme = user.getDisplayName();

        User user = new User(userID, userEmail, displayNAme);

        db.collection("users").document(userEmail).set(user);
    }


    public void uploadData(String collectionPath, String document, Object data)
    {
        db.collection(collectionPath).document(document).set(data);
    }

    public String getCurrentUserEmail()
    {
        return user.getEmail();
    }

    public boolean isValidUserEmail(String userEmail)
    {
        boolean exists = db.collection("users").document(userEmail).get().isSuccessful();
        return exists;
    }

}