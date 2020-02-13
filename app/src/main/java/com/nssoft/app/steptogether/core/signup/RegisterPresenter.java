package com.nssoft.app.steptogether.core.signup;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nssoft.app.steptogether.animation.MyAnimation;

public class RegisterPresenter implements RegisterContract.Presenter {
    RegisterContract.View mView;
    private FirebaseAuth mAuth;

    public RegisterPresenter(RegisterContract.View mView){
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void checkSignUp(String username,String password,String confpassword) {
        if (!username.isEmpty() && !password.isEmpty() && !confpassword.isEmpty() &&password.equals(confpassword)) {
            mView.showAnimation(MyAnimation.AnimationLoading);
            mAuth.createUserWithEmailAndPassword(username.trim(), password.trim())
                    .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mView.hideAnimation();
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mView.showNotify("Please check email for verification.");
                                                    mAuth.signOut();
                                                    mView.registerSuccess();
                                                }
                                            }
                                        });
                            } else {

                                mView.showNotify("Oops"+ task.getException());

                            }
                        }
                    });
        }else {
            mView.showNotify("Oops Please enter your email and password first");
        }
    }
}
