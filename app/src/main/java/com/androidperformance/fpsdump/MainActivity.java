package com.androidperformance.fpsdump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button mBtnShow;
    private Button mBtnHide;
    private Switch fpsColorSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnShow = findViewById(R.id.btn_show);
        mBtnHide = findViewById(R.id.btn_hide);
        fpsColorSwitch = findViewById(R.id.textColor);

        mBtnShow.setOnClickListener(this);
        mBtnHide.setOnClickListener(this);
        fpsColorSwitch.setOnCheckedChangeListener(this);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.textColor :
                notifyFpsColorChanged(compoundButton.isChecked());
                break;
            default:
                break;
        }
    }

    private void notifyFpsColorChanged(boolean checked) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.isChecked = checked;
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class MessageEvent {
        public boolean isChecked;
    }

}
