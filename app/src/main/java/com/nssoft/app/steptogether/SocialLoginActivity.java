package com.nssoft.app.steptogether;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nssoft.app.steptogether.animation.MyAnimation;
import com.nssoft.app.steptogether.core.login.SocialLoginContract;
import com.nssoft.app.steptogether.core.login.SocialLoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialLoginActivity extends AppCompatActivity implements SocialLoginContract.View {

    @BindView(R.id.btn_signin)
    Button btnSignin;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.btn_googlesign)
    RelativeLayout btnGooglesign;
    @BindView(R.id.btn_fbsign)
    RelativeLayout btnFbsign;
    private SocialLoginPresenter mSocialLoginPresenter;
    private MyAnimation myAnimation;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sociallogin);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager =findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout =  findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);
        mSocialLoginPresenter = new SocialLoginPresenter(this,getString(R.string.default_web_client_id));
        myAnimation = MyAnimation.getInstance();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSocialLoginPresenter.onActivityResult(requestCode,resultCode,data);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mSocialLoginPresenter.onStart();
    }

    @Override
    public void showNotify(String msg) {
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showAnimation(String animation) {
        myAnimation.showAnimation(this,animation);
    }

    @Override
    public void hideAnimation() {
        myAnimation.hideAnimation();
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @OnClick({R.id.btn_signin, R.id.btn_signup, R.id.btn_googlesign, R.id.btn_fbsign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(view,"imageTransition"));
                    startActivity(new Intent(this,EmailLoginActivity.class), options.toBundle());
                }else {
                    startActivity(new Intent(this,EmailLoginActivity.class));
                }
                break;
            case R.id.btn_signup:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(view,"imageTransition"));
                    startActivity(new Intent(this,RegisterActivity.class), options.toBundle());
                }else {
                    startActivity(new Intent(this,RegisterActivity.class));
                }
                break;
            case R.id.btn_googlesign:
                mSocialLoginPresenter.checkGoogleLogin(this);
                break;
            case R.id.btn_fbsign:
                mSocialLoginPresenter.checkFacebookLogin();
                break;
        }
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        int[] icons = new int[]{R.drawable.ic_run,R.drawable.ic_network};

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
            ImageView icon = rootView.findViewById(R.id.iv_icon);
            icon.setImageResource(icons[getArguments().getInt(ARG_SECTION_NUMBER)-1]);
            return rootView;
        }


    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            return 2;
        }
        /**
         @Override
         public CharSequence getPageTitle(int position) {
         switch (position) {
         case 0:
         return "SECTION 1";
         case 1:
         return "SECTION 2";
         case 2:
         return "SECTION 3";
         }
         return null;
         }
         */

    }
}
