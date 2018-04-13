package com.mrchao.www.ringview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RingView rings4 = (RingView) findViewById(R.id.rings4);
        rings4.startAnima();
    }
}
