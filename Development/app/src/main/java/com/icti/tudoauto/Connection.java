package com.icti.tudoauto;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Connection {
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser mUser;
    private Connection(){

    }

    public static FirebaseAuth getFirebaseAuth(){
        if(mAuth==null){
            inicializaFirebaseAuth();
        }

        return mAuth;
    }

    private static void inicializaFirebaseAuth(){
        mAuth = mAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    mUser = user;
                }
            }
        };
        mAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return mUser;
    }
    public static void logOut(){
        mAuth.signOut();
    }
}


