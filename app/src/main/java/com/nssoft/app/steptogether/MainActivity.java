package com.nssoft.app.steptogether;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.nssoft.app.steptogether.core.main.MainContract;
import com.nssoft.app.steptogether.core.main.MainPresenter;
import com.nssoft.app.steptogether.fragment.EditProfile;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter mainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FirebaseAuth.getInstance().signOut();

    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mainPresenter = new MainPresenter(this);
    }

    @Override
    public void newUser() {
        getSupportFragmentManager().beginTransaction().add(R.id.main,new EditProfile(),"editprofile").commitNow();
    }
}
