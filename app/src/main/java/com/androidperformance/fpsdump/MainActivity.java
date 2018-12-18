package com.androidperformance.fpsdump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnShow;
    private Button mBtnHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnShow = findViewById(R.id.btn_show);
        mBtnHide = findViewById(R.id.btn_hide);

        mBtnShow.setOnClickListener(this);
        mBtnHide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FloatingViewService.class);
        switch (v.getId()) {
            case R.id.btn_show:
                intent.putExtra(FloatingViewService.ACTION, FloatingViewService.SHOW);
                break;
            case R.id.btn_hide:
                intent.putExtra(FloatingViewService.ACTION, FloatingViewService.HIDE);
                break;
        }
        startService(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
