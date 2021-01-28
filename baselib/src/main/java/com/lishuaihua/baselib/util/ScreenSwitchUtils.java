package com.lishuaihua.baselib.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

import com.gialen.baselib.util.Logger;

/**
 * Created by lishuaihua on 2017/9/7.
 */
public class ScreenSwitchUtils {

    private static final String TAG = ScreenSwitchUtils.class.getSimpleName();

    private volatile static ScreenSwitchUtils mInstance;

    private Activity mActivity;

    // 是否是竖屏

    private int currentOrientiton = 3;
    public final static int TOP_ORIENTATION = 1;
    public final static int LEFT_ORIENTATION = 2;
    public final static int BOTTOM_ORIENTATION = 3;
    public final static int RIGHT_ORIENTATION = 4;

    private SensorManager sm;
    private OrientationSensorListener listener;
    private Sensor sensor;

//    private SensorManager sm1;
//    private Sensor sensor1;
//    private OrientationSensorListener1 listener1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {
                        if (currentOrientiton != RIGHT_ORIENTATION) {
                            currentOrientiton = RIGHT_ORIENTATION;
                            Logger.i("ScreenSwitchUtils", "反转横屏");
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        }
                    } else if (orientation > 135 && orientation < 225) {
                        if (currentOrientiton != TOP_ORIENTATION) {
                            currentOrientiton = TOP_ORIENTATION;
                            Logger.i("ScreenSwitchUtils", "反转竖屏");
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                        }
                    } else if (orientation > 225 && orientation < 315) {
                        if (currentOrientiton != LEFT_ORIENTATION) {
                            currentOrientiton = LEFT_ORIENTATION;
                            Logger.i("ScreenSwitchUtils", "横屏");
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        }
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        if (currentOrientiton != BOTTOM_ORIENTATION) {
                            currentOrientiton = BOTTOM_ORIENTATION;
                            Logger.i("ScreenSwitchUtils", "竖屏");
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        }
                    }
                    break;
                default:
                    break;
            }

        }

        ;
    };

    /**
     * 返回ScreenSwitchUtils单例
     **/
    public static ScreenSwitchUtils init(Context context) {
        if (mInstance == null) {
            synchronized (ScreenSwitchUtils.class) {
                if (mInstance == null) {
                    mInstance = new ScreenSwitchUtils(context);
                }
            }
        }
        return mInstance;
    }

    private ScreenSwitchUtils(Context context) {

        // 注册重力感应器,监听屏幕旋转
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);

    }

    /**
     * 开始监听
     */
    public void start(Activity activity) {
        mActivity = activity;
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 停止监听
     */
    public void stop() {
        sm.unregisterListener(listener);
    }


    public int getCurrentOrientiton() {
        return this.currentOrientiton;
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }

}