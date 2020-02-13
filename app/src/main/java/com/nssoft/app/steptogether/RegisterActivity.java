package com.nssoft.app.steptogether;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.nssoft.app.steptogether.animation.MyAnimation;
import com.nssoft.app.steptogether.core.signup.RegisterContract;
import com.nssoft.app.steptogether.core.signup.RegisterPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    RegisterPresenter mPresenter;
    MyAnimation myAnimation;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_confpassword)
    EditText edConfpassword;
    @BindView(R.id.btn_signin)
    Button btnSignin;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
        initView();

    }


    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        ButterKnife.bind(this);
        mPresenter = new RegisterPresenter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(new Fade());
        }
        myAnimation = MyAnimation.getInstance();
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    String checkMe = String.valueOf(source.charAt(i));
                    Pattern pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789_@.-]*");
                    Matcher matcher = pattern.matcher(checkMe);
                    boolean valid = matcher.matches();
                    if (!valid) {
                        return "";
                    }
                }
                return null;
            }
        };
        edUsername.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void showNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAnimation(String animation) {
        myAnimation.showAnimation(this, animation);
    }

    @Override
    public void hideAnimation() {
        myAnimation.hideAnimation();
    }

    @Override
    public void registerSuccess() {
        finish();
    }

    @OnClick({R.id.btn_signin, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                mPresenter.checkSignUp(edUsername.getText().toString(), edPassword.getText().toString(), edConfpassword.getText().toString());
                break;
            case R.id.iv_back:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
                break;

        }

    }
}
