package com.nssoft.app.steptogether.core.login;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nssoft.app.steptogether.animation.MyAnimation;

public class EmailLoginPresenter implements EmailLoginContract.Presenter {
    EmailLoginContract.View mView;
    private FirebaseAuth mAuth;


    public EmailLoginPresenter(EmailLoginContract.View mView){
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void checkLogin(String username,String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            mView.showAnimation(MyAnimation.AnimationLoading);
            mAuth.signInWithEmailAndPassword(username.trim(), password.trim())
                    .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            mView.hideAnimation();
                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()) {
                                    mView.loginSuccess();
                                }else {
                                    mView.showNotify("Please check email for verification.");
                                }
                            } else {
                                mView.showNotify(task.getException().toString());
                            }
                        }
                    });
        }else {
            mView.showNotify("Oops Please enter your email and password first");
        }
    }

    @Override
    public void forgotPassword(String email) {
        mView.showAnimation(MyAnimation.AnimationLoading);
        if(!email.trim().isEmpty()) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mView.hideAnimation();
                            if (task.isSuccessful()) {
                                mView.showNotify("Email sent . Please check you email");
                            } else {
                                mView.showNotify("Oops" + task.getException());
                            }
                        }
                    });
        }else {
            mView.hideAnimation();
            mView.showNotify("Oops please enter your email");
        }

    }


}
