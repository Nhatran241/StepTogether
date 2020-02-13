package com.nssoft.app.steptogether.core.login;

import android.app.Activity;
import android.content.Intent;

public class SocialLoginContract {
    public interface View {
        void showNotify(String msg);
        void showAnimation(String animation);
        void hideAnimation();
        void loginSuccess();
    }
    public interface Presenter {
        void onStart();
        void checkGoogleLogin(Activity activity);
        void checkFacebookLogin();
        void onActivityResult(int requestCode, int resultCode, Intent data);

    }
}
