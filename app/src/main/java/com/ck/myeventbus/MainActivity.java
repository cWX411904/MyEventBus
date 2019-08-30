package com.ck.myeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ck.myeventbus.core.Hermes;


/**
 * 相当于阿里巴巴
 */
public class MainActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(this);
        text= (TextView) findViewById(R.id.text_content);

        Hermes.getDefault().init(this);
        Hermes.getDefault().register(UserManager.class);
    }

    public void change(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    /**
     * 写了这些注解的就是岗位，
     * 需要接收哪些消息
     */
    @Subscribe(threadMode = ThreadMode.PostThread)
    public void receive(Friend friend) {
        text.setText(friend.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
