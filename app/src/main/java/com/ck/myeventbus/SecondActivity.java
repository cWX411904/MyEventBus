package com.ck.myeventbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ck.myeventbus.core.Hermes;
import com.ck.myeventbus.service.HermesService;


public class SecondActivity extends AppCompatActivity {

    IUserManager iUserManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Hermes.getDefault().connect(this, HermesService.class);
    }

    public void userManager(View view) {
//        EventBus.getDefault().post(new Friend("ck", "153161"));

        //获得进程A的UserManager的代理
        iUserManager = Hermes.getDefault().getInstance(IUserManager.class);
        iUserManager.getUser();
    }

    public void send2(View view) {

    }
}
