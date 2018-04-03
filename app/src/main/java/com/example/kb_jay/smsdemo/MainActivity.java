package com.example.kb_jay.smsdemo;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * * 实现监听短信
 *
 * @author kb_jay
 *         created at 2018/4/3 下午1:37
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTvTest = this.findViewById(R.id.tv_test);
        SmsObserver smsObserver = new SmsObserver(this, new Handler(), new SmsObserver.SmsListener() {
            @Override
            public void onResult(String msg) {
                mTvTest.setText(msg);
            }
        });

        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);

    }
}
