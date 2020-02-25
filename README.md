# FPSDump
这是一个用于显示 FPS 的小工具，数据是从 SurfaceFlinger 这边拿的 Flip 的数据，然后进行计算

# 编译和调试
由于获取这个需要系统签名，所以这个 App 不能直接在 Studio 上运行，需要在对应的平台签名后才可以运行。
如果需要调试UI，可以在 AndroidManifest.xml 中 去掉   android:sharedUserId="android.uid.system" ，然后就可以使用 AS安装了

## 悬浮窗权限
需要配置悬浮窗权限才可以正常显示 FPS

# 核心代码

```java
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

```