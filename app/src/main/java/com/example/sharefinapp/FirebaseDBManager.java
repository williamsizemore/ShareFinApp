package com.example.sharefinapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;


public class FirebaseDBManager extends AppCompatActivity implements DbManagerInterface {
    private FirebaseAuth fbAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;


    FirebaseDBManager(FirebaseUser user) {
        this.user = user;
        fbAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public FirebaseFirestore getDb() {
        return db;
    }



}