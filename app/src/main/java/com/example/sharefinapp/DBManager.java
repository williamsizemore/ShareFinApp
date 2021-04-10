package com.example.sharefinapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class DBManager extends AppCompatActivity {
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
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return user;
    }

    public User getUser(String userEmail)
    {
        User user = new User();
        Log.v("test getUser() called", "created user object, starting query");
        db.collection("users").whereEqualTo("userEmail",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                user.setUserID(queryDocumentSnapshots.toObjects(User.class).get(0).getUserID());
                user.setUserEmail(queryDocumentSnapshots.toObjects(User.class).get(0).getUserEmail());
                user.setDisplayName(queryDocumentSnapshots.toObjects(User.class).get(0).getDisplayName());
                Log.v("test getting user of userID: ", user.getUserID());
            }
        });
        while (user == null) {
            try {
                wait(100);
            }
            catch (Exception e)
            {
                Log.e("test UserID is Empty", e.getStackTrace().toString());
            }
        }
        return user;
    }
    public ArrayList<User> getUsers(List<String> userEmails){
        ArrayList<User> userList = new ArrayList<>();

        Task<QuerySnapshot> query = db.collection("users").whereArrayContainsAny("userEmail",userEmails).get();
        while(!query.isComplete());

        userList.forEach(x -> query.getResult().toObjects(User.class));
        Log.v(" test getUsers - attempting to add User objects: ", userList.toArray().toString());

        return userList;
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

    public String generateKey(String collectionPath)
    {
        String key = db.collection(collectionPath).document().getId();
        Log.v("test id generation", key);
        return key;

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

    /* checks to see if the user exists in the and returns the message result */
    public boolean isValidData(String collectionPath, String field, String value)
    {
        final Boolean bool ;

        bool = db.collection(collectionPath).whereEqualTo(field,value).limit(1).get().isSuccessful();

        return isValidDataExecute(bool);
    }

    private boolean isValidDataExecute(boolean bool)
    {
        return bool;
    }

    // get all groups associated with the user // WARNING: query is asynchronous and may return before populating return values
    public ArrayList<Object> getObjects(String collectionPath, String field, String fieldValue) {
        ArrayList<Object> objects = new ArrayList<>();

        db.collection(collectionPath).whereArrayContains(field, fieldValue).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                    {
                        objects.add(doc.toObject(Object.class));
                    }
                }
                else
                    Log.d("test getGroups Query", String.valueOf(task.getException()));
            }
        });
        return objects;
    }

    // get the specified group associated with the current user
    public Group getGroup(String groupName)
    {
        ArrayList<Group> group = new ArrayList<>();
        db.collection("groups").whereArrayContains("groupUsers",getCurrentUserEmail()).whereEqualTo("groupName", groupName).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    group.add(doc.toObject(Group.class));
                }

            }
        });
        return group.get(0);
    }

}