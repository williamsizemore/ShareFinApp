package com.example.sharefinapp;

import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public interface DbManagerInterface {

    public FirebaseUser getUser() ;

    public FirebaseFirestore getDb();
}
