package com.nssoft.app.steptogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nssoft.app.steptogether.adapter.ViewPagerStepAdapter;
import com.nssoft.app.steptogether.core.startservice.ServiceContract;
import com.nssoft.app.steptogether.core.startservice.ServicePresenter;
import com.nssoft.app.steptogether.fragment.FragmentStep;
import com.nssoft.app.steptogether.fragment.FragmentTogether;
import com.nssoft.app.steptogether.service.SensorService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements ServiceContract.View{

    @BindView(R.id.vp_step_activity)
    ViewPager vpStepActivity;
    @BindView(R.id.tl_step_activity)
    TabLayout tlStepActivity;
    
    ServicePresenter servicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        servicePresenter.startSensorService();
    }

    private void initView() {
        servicePresenter = new ServicePresenter(this);
        
        ViewPagerStepAdapter adapter = new ViewPagerStepAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentStep(), "Step");
        adapter.addFragment(new FragmentTogether(), "Together");

        vpStepActivity.setAdapter(adapter);
        tlStepActivity.setupWithViewPager(vpStepActivity);
        tlStepActivity.getTabAt(0).setIcon(R.drawable.ic_run);
        tlStepActivity.getTabAt(1).setIcon(R.drawable.ic_network);
    }

    @Override
    public void startSensorService(String notifi) {
        Toast.makeText(this, notifi, Toast.LENGTH_SHORT).show();
    }
}
