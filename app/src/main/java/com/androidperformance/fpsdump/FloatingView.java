package com.androidperformance.fpsdump;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FloatingView extends FrameLayout {
    private static final String TAG = "FloatingView";
    private static final int MSG_GET_FPS = 0;
    private static final int MES_STOP = 1;
    private final TextView mFpsText;
    private Handler mainHandler;
    private Context mContext;
    private View mView;
    private int mTouchStartX, mTouchStartY;
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;
    private FPSUtils mFpsUtils;
    private HandlerThread handlerThread;
    private Handler workHandler;
    private boolean working = false;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mParams.x += (int) event.getRawX() - mTouchStartX;
                    mParams.y += (int) event.getRawY() - mTouchStartY;
                    mWindowManager.updateView(mView, mParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };

    public FloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.floating_view, null);
        mWindowManager = FloatingManager.getInstance(mContext);
        mFpsText = mView.findViewById(R.id.fps);
        mFpsUtils = new FPSUtils();

        handlerThread = new HandlerThread("getFPS");
        handlerThread.start();
        mainHandler = new Handler();


        workHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_GET_FPS:
                        working = true;
                        while (working) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mFpsUtils.updateContent();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mFpsText.setText(String.format("fps = %s", mFpsUtils.getmFps()));
                                }
                            });
                        }
                        break;
                    case MES_STOP:
                        working = false;
                        break;
                }
            }
        };
    }

    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 100;
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mParams.format = PixelFormat.TRANSPARENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);

        Message msg = Message.obtain();
        msg.what = MSG_GET_FPS;
        workHandler.sendMessage(msg);
    }

    public void hide() {
        Message msg = Message.obtain();
        msg.what = MES_STOP;
        workHandler.sendMessage(msg);

        mWindowManager.removeView(mView);
    }
}
