package com.androidperformance.fpsdump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FloatingViewService extends Service {
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    public static final String CHANGE_COLOR = "change_color";
    private FloatingView mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = new FloatingView(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                mFloatingView.show();
            } else if (HIDE.equals(action)) {
                mFloatingView.hide();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainActivity.MessageEvent event) {
        Log.v("Gracker","Get event");
        mFloatingView.changeColor(event.isChecked);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
