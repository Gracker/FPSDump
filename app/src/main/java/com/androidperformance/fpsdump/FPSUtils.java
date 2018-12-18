package com.androidperformance.fpsdump;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

class FPSUtils {
    private int mLastPageFlipCount;
    private long mLastUpdateTime;
    private float mFps = 0;
    private IBinder mFlinger;

    FPSUtils() {
        mFlinger = (IBinder) LocalServiceManager.getService("SurfaceFlinger");
    }

    boolean updateContent() {
        try {
            if (mFlinger != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                mFlinger.transact(1013, data, reply, 0);
                final int pageFlipCount = reply.readInt();

                final long now = System.nanoTime();
                final int frames = pageFlipCount - mLastPageFlipCount;
                final long duration = now - mLastUpdateTime;
                mFps = (float) (frames * 1e9 / duration);
                mLastPageFlipCount = pageFlipCount;
                mLastUpdateTime = now;
                reply.recycle();
                data.recycle();
            }
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    float getmFps() {
        return mFps;
    }
}
