package com.nssoft.app.steptogether.core.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nssoft.app.steptogether.animation.MyAnimation;

public class SocialLoginPresenter implements SocialLoginContract.Presenter {
    private static final int RC_SIGN_IN = 1000;
    SocialLoginContract.View mView;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;

    public SocialLoginPresenter(final SocialLoginContract.View mView, String clientid){
        this.mView = mView;
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientid)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient((Activity) mView, gso);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = new LoginButton((Context) mView);
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                mView.hideAnimation();
            }

            @Override
            public void onError(FacebookException error) {
                mView.hideAnimation();
            }
        });

    }

    @Override
    public void onStart() {
        if (mAuth.getCurrentUser() != null) {
          mView.loginSuccess();
        }
    }

    @Override
    public void checkGoogleLogin(Activity loginview) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            loginview.startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    public void checkFacebookLogin() {
            loginButton.performClick();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mView.showAnimation(MyAnimation.AnimationLoading);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mView.loginSuccess();
                        } else {

                        }
                        mView.hideAnimation();

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        mView.showAnimation(MyAnimation.AnimationLoading);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mView.loginSuccess();
                        } else {
                        }
                        mView.hideAnimation();
                    }
                });
    }


}
