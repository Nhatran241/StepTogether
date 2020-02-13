package com.nssoft.app.steptogether.core.signup;

public class RegisterContract {
    public interface View {
        void showNotify(String msg);
        void showAnimation(String animation);
        void hideAnimation();
        void registerSuccess();
    }
    public interface Presenter {
        void checkSignUp(String username, String password, String confpassword);
    }
}
