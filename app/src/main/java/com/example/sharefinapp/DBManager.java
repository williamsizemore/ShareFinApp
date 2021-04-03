package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class DBManager extends AppCompatActivity {
    private final FirebaseAuth fbAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user;
    public static final String users_collection = "users";

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
    }   // todo potentially remove?

    /*
          Upload data with a known document ID to the FireStore database
          @collectionPath - the collection to be added to
          @document - the ID of the object/document for the collection
          @data - the object to be uploaded as the document
     */
    public void insertData(String collectionPath, String document, Object data)
    {
        db.collection(collectionPath).document(document).set(data);
    }

    /*
        Upload data without a known document ID to the FireStore database
        @collectionPath - the collection to be added to
        @data - the object to be uploaded as the document
     */
    public void insertData(String collectionPath, Object data)
    {
        db.collection(collectionPath).add(data);
    }

    public String getCurrentUserEmail()
    {
        return user.getEmail();
    }
    public String getCurrentUserID()
    {
        return user.getUid();
    }
    public String getCurrentUserDisplayName()
    {
        return user.getDisplayName();
    }

    public boolean isValidUserEmail(String userEmail)
    {
        boolean exists = db.collection("users").document(userEmail).get().isSuccessful();
        return exists;
    }

//    public Query queryDb(String collection, String whereEqualTo)
//    {
////        todo implement
//
//        return new Query;
//    }

}