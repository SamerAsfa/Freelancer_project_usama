//package com.example.myapplication.myapplication.ui;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.hardware.Camera.CameraInfo;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.FrameLayout.LayoutParams;
//
//import androidx.annotation.NonNull;
//
//
//import com.huawei.hms.ml.camera.CameraConfig;
//import com.huawei.hms.ml.camera.CameraManager;
//import com.huawei.hms.ml.common.utils.KeepOriginal;
//import com.huawei.hms.ml.common.utils.SmartLog;
//import com.huawei.hms.mlsdk.livenessdetection.OnMLLivenessDetectCallback;
//
//import java.io.IOException;
//import java.util.concurrent.CountDownLatch;
//
//public class Live extends FrameLayout {
//    @KeepOriginal
//    public static final int DETECT_MASK = 1;
//    private static final CountDownLatch a = new CountDownLatch(1);
//    private Activity b;
//    private CameraConfig c;
//    private Rect d;
//    private OnMLLivenessDetectCallback e;
//    private CameraManager f;
//    private com.huawei.hms.mlsdk.livenessdetection.ui.a g;
//    private Handler h = new Handler();
//    private Runnable i = new d(this);
//    private SurfaceView j;
//    private boolean k = false;
//    private boolean l = false;
//    private Point m;
//
//    public Live(@NonNull Context context) {
//        super(context);
//    }
//
//    private void a(SurfaceHolder var1) {
//        Live var10000 = this;
//        Live var10001 = this;
//        Live var10002 = this;
//        SmartLog.i("TimeDelay", com.huawei.hms.mlsdk.livenessdetection.l.a.a("initCamera: ").append(System.currentTimeMillis()).toString());
//
//        label41:
//        {
//            label40:
//            {
//                label45:
//                {
//                    boolean var7;
//                    try {
//                        var10002.f.initCamera(var1);
//                        var10001.f.preSetCameraCallback();
//                    } catch (IOException var4) {
//                        var7 = false;
//                        break label40;
//                    } catch (Exception var5) {
//                        var7 = false;
//                        break label45;
//                    }
//
//                    var7 = true;
//
//                    try {
//                        var10000.l = var7;
//                        break label41;
//                    } catch (Exception var3) {
//                        var7 = false;
//                    }
//                }
//
//                SmartLog.e("MLLivenessSurfaceView", "initCamera occur Exception");
//                this.e.onError(11402);
//                return;
//            }
//
//            SmartLog.e("MLLivenessSurfaceView", "initCamera occur IOException");
//            this.e.onError(11402);
//            return;
//        }
//
//        if (CameraManager.CameraState.CAMERA_INITIALED != this.f.getCameraState()) {
//            SmartLog.e("MLLivenessSurfaceView", "CAMERA OPEN Failed");
//            this.e.onError(11402);
//        }
//
//        a var6;
//        if ((var6 = this.g) != null) {
//            var6.a();
//        }
//
//        if (CameraManager.CameraState.PREVIEW_STARTED != this.f.getCameraState()) {
//            SmartLog.e("MLLivenessSurfaceView", "CAMERA Preview Failed");
//            this.e.onError(11402);
//        }
//
//    }
//
//    private void setPreviewSite(int var1) {
//        if (this.d == null) {
//            SmartLog.e("MLLivenessSurfaceView", "faceFrameRect is null");
//        } else {
//            int var10003 = var1;
//            DisplayMetrics var5;
//            DisplayMetrics var10004 = var5 = new DisplayMetrics;
//            var5.<init> ();
//            this.b.getWindowManager().getDefaultDisplay().getMetrics(var5);
//            int var2 = Math.min(var10004.heightPixels, var5.widthPixels);
//            var1 = Math.max(var10004.heightPixels, var5.widthPixels);
//            int var3 = var10003 / 2;
//            Rect var4;
//            int var6;
//            var3 = (var6 += ((var4 = this.d).bottom - (var6 = var4.top)) / 2) - var3;
//            SmartLog.d("MLLivenessSurfaceView", "rectCenterY:" + var6 + ",centerY:" + this.d.centerY());
//            LayoutParams var7;
//            (var7 = (LayoutParams) this.j.getLayoutParams()).topMargin = var3;
//            this.j.setLayoutParams(var7);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  screenHeight: " + var1);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  widthPixels: " + var2);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  faceFrameRect.top: " + this.d.top);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  faceFrameRect.left: " + this.d.left);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  faceFrameRect.right: " + this.d.right);
//            SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewInfo:  faceFrameRect.bottom: " + this.d.bottom);
//        }
//    }
//
//    @KeepOriginal
//    public void onCreate(Bundle var1) {
//        this.j = new SurfaceView(this.b);
//        this.addView(this.j);
//        int var10000 = Camera.getNumberOfCameras();
//        Activity var4 = this.b;
//        byte var2;
//        if (var10000 > 1) {
//            var2 = 1;
//        } else {
//            var2 = 0;
//        }
//
//        Activity var9 = var4;
//        Camera.CameraInfo var5;
//        Camera.CameraInfo var10002 = var5 = new Camera.CameraInfo;
//        var10002.<init> ();
//        Camera.getCameraInfo(var2, var10002);
//        SmartLog.i("DecodeHelper", " cameraId: " + var2);
//        int var7 = 0;
//        Object var3;
//        if ((var3 = var9.getSystemService("window")) != null && var3 instanceof WindowManager) {
//            var7 = ((WindowManager) var3).getDefaultDisplay().getRotation();
//            SmartLog.i("DecodeHelper", " calculateCameraDisplayOrientation: " + var7);
//        }
//
//        short var8;
//        if (var7 != 1) {
//            if (var7 != 2) {
//                if (var7 != 3) {
//                    var8 = 0;
//                } else {
//                    var8 = 270;
//                }
//            } else {
//                var8 = 180;
//            }
//        } else {
//            var8 = 90;
//        }
//
//        int var6;
//        if (var5.facing == 1) {
//            var6 = (360 - (var5.orientation + var8) % 360) % 360;
//            SmartLog.i("DecodeHelper", "facingCAMERA_FACING_FRONT");
//        } else {
//            var6 = (var5.orientation - var8 + 360) % 360;
//            SmartLog.i("DecodeHelper", "facingCAMERA_FACING_BACK");
//        }
//
//        SmartLog.i("DecodeHelper", "CameraOrientationResult : " + var6);
//        if (this.c == null) {
//            this.c = (new CameraConfig.Factory()).setCameraFacing(1).setCameraMode(2).setCameraOrientation(var6).setCameraExpectSize(new Point(640, 480)).setRecordingHint(false).create();
//        }
//
//        this.f = new CameraManager(this.b.getApplicationContext(), this.c);
//        SmartLog.i("MLLivenessSurfaceView", "MLLivenessDetectViewCameraRotation : " + var6);
//        this.f.setCameraSizeListener(new e(this));
//        this.g = new a(this.b, this.f, new f(this));
//        this.g.c();
//        this.h.postDelayed(this.i, 60000L);
//    }
//
//    @KeepOriginal
//    public void onResume() {
//        SmartLog.i("MLLivenessSurfaceView", "onResume");
//        SurfaceHolder var1 = this.j.getHolder();
//        if (this.k) {
//            this.a(var1);
//        } else {
//            var1.addCallback(new g(this));
//        }
//
//    }
//
//    @KeepOriginal
//    public void onStart() {
//        SmartLog.i("MLLivenessSurfaceView", "onStart");
//    }
//
//    @KeepOriginal
//    public void onPause() {
//        SmartLog.i("MLLivenessSurfaceView", "onPause");
//        CameraManager var1;
//        if ((var1 = this.f) != null && this.l) {
//            var1.onPause();
//        }
//
//    }
//
//    @KeepOriginal
//    public void onStop() {
//        SmartLog.i("MLLivenessSurfaceView", "onStop");
//    }
//
//    @KeepOriginal
//    public void onDestroy() {
//        SmartLog.i("MLLivenessSurfaceView", "onDestroy");
//        Handler var1;
//        if ((var1 = this.h) != null) {
//            var1.removeCallbacksAndMessages((Object) null);
//        }
//
//        a var2;
//        if ((var2 = this.g) != null) {
//            var2.b();
//            this.g = null;
//        }
//
//        CameraManager var3;
//        if ((var3 = this.f) != null) {
//            var3.onDestroy();
//            this.f = null;
//        }
//
//    }
//
//    public void b() {
//        this.m = this.f.getCameraSize();
//        SurfaceView var1;
//        if ((var1 = this.j) == null) {
//            SmartLog.w("MLLivenessSurfaceView", "postPreviewSize view not ready");
//        } else if (this.m == null) {
//            SmartLog.w("MLLivenessSurfaceView", "framePoint not ready");
//        } else {
//            var1.post(new h(this));
//        }
//    }
//
//    public int getCameraOrientation() {
//        CameraConfig var1;
//        return (var1 = this.c) != null ? var1.getCameraOrientation() : 0;
//    }
//
//    public void c() {
//        this.f.refreshCameraOrientation(90);
//        SmartLog.d("MLLivenessSurfaceView", " refreshCameraOrientation " + this.c.getCameraOrientation());
//    }
//
//    @KeepOriginal
//    public static final class Builder {
//        private static final int TYPE_SILENT = 0;
//        private Activity context;
//        private int type = 0;
//        private Rect faceFrameRect;
//        private int options;
//        private OnMLLivenessDetectCallback detectCallback;
//
//        public Builder() {
//        }
//
//        public Live.Builder setType(int var1) {
//            this.type = var1;
//            return this;
//        }
//
//        @KeepOriginal
//        public Live.Builder setOptions(int var1) {
//            this.options = var1;
//            return this;
//        }
//
//        @KeepOriginal
//        public Live.Builder setFaceFrameRect(Rect var1) {
//            this.faceFrameRect = var1;
//            return this;
//        }
//
//        @KeepOriginal
//        public Live.Builder setContext(Activity var1) {
//            this.context = var1;
//            return this;
//        }
//
//        @KeepOriginal
//        public Live.Builder setDetectCallback(OnMLLivenessDetectCallback var1) {
//            this.detectCallback = var1;
//            return this;
//        }
//
//        @KeepOriginal
//        public Live build() {
//            MLLivenessCapture.getInstance().setConfig((new com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureConfig.Builder()).setOptions(this.options).setType(this.type).build());
//            return new Live(this.context, this.faceFrameRect, this.detectCallback, (d) null);
//        }
//    }
//}
