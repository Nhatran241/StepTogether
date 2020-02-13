package com.nssoft.app.steptogether.core.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public MainPresenter(MainContract.View mView){
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getProfile();
    }

    private void getProfile(){
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("nhattest", "onComplete: "+task.getResult());
                } else {
                    mView.newUser();
                }
            }
        });
    }
}
