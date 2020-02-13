package com.nssoft.app.steptogether.core.login;

public class EmailLoginContract {
    public interface View {
        void showNotify(String msg);
        void showAnimation(String animation);
        void hideAnimation();
        void loginSuccess();
    }
    public interface Presenter {
        void checkLogin(String username, String password);
        void forgotPassword(String email);
    }
}
