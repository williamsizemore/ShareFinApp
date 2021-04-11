package com.example.sharefinapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
//        while (user == null) {
//            try {
//                wait(100);
//            }
//            catch (Exception e)
//            {
//                Log.e("test UserID is Empty", e.getStackTrace().toString());
//            }
//        }
        return user;
    }

    /* Not functioning correctly, needs to handle asynrchronous nature of db */
    public ArrayList<User> getUsers(List<String> userEmails){
        ArrayList<User> userList = new ArrayList<>();

        Task<QuerySnapshot> query = db.collection("users").whereArrayContainsAny("userEmail",userEmails).get();
//        while(!query.isComplete());

        userList.forEach(x -> query.getResult().toObjects(User.class));
        Log.v(" test getUsers - attempting to add User objects: ", userList.toArray().toString());

        return userList;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    /**
          Upload data with a known document ID to the FireStore database
          @param collectionPath - the collection to be added to
          @param document - the ID of the object/document for the collection
          @param data - the object to be uploaded as the document
     */
    public void insertData(String collectionPath, String document, Object data)
    {
        db.collection(collectionPath).document(document).set(data);
    }

    /**
        Upload data without a known document ID to the FireStore database
        @param collectionPath - the collection to be added to
        @param data - the object to be uploaded as the document
     */
    public void insertData(String collectionPath, Object data)
    {
        db.collection(collectionPath).add(data);
    }

    /* update a data field of type String */
    public void updateDataField(String collectionPath, DocumentReference documentReference, String field, String value)
    {
        db.collection(collectionPath).document(documentReference.getId()).update(field, value);
    }
    /* update a data field of type double */
    public void updateDataField(String collectionPath, DocumentReference documentReference, String field, double value)
    {

        db.collection(collectionPath).document(documentReference.getId()).update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("test updateDataField-double success: ","updated "+ documentReference + " " + field + " with value of " + value);
            }
        });
    }
    /* update a data field of type int */
    public void updateDataField(String collectionPath, DocumentReference  documentReference, String field, int value)
    {
        db.collection(collectionPath).document(documentReference.getId()).update(field, value);
    }
    /* update a data field of an Object */
    public void updateDataField(String collectionPath, DocumentReference  documentReference, String field, Object obj)
    {
        db.collection(collectionPath).document(documentReference.getId()).update(field, obj);
    }

    public String generateKey(String collectionPath)
    {
        String key = db.collection(collectionPath).document().getId();
        Log.v("test id generation", key);
        return key;

    }

    /**
     *
     * @param collectionPath    - name of the collection, i.e. 'users', 'groups','payments','bills'
     * @param documentIDName    - name of the ID field, i.e. billID, groupID, userID, paymentID, etc.
     * @param documentID        - the associated value of the document ID name
     * @return  - returns the ID to get the specific document
     */
    public void getDocumentReference(String collectionPath, String documentIDName,String documentID, double amount)
    {
        List<String> docRef = new ArrayList<>();
//        try {
        db.collection(collectionPath).whereEqualTo(documentIDName, documentID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds : queryDocumentSnapshots)
                    docRef.add(ds.getReference().toString());

                while (docRef.isEmpty())
                    Log.v("getDocumentReference"," docRef is empty: " + docRef.toString());
//                updateDataField("bills",docRef.get(0),"amountPaid",amount);
            }
        });


//        while (docRef == null || docRef.get(0).isEmpty())
//        {
//            getApplicationContext().wait(50);
//        }
//
//        } catch (InterruptedException e)
//        {
//            Log.e("DB Manager - getDocumentReference: ",e.getStackTrace().toString());
//        }
//        return docRef.get(0);
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