package com.lishuaihua.baselib.util

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Message
import com.lishuaihua.baselib.util.Logger.Companion.i

/**
 * Created by lishuaihua on 2017/9/7.
 */
class ScreenSwitchUtils private constructor(context: Context) {
    private var mActivity: Activity? = null

    // 是否是竖屏
    var currentOrientiton = 3
        private set
    private val sm: SensorManager
    private val listener: OrientationSensorListener
    private val sensor: Sensor

    //    private SensorManager sm1;
    //    private Sensor sensor1;
    //    private OrientationSensorListener1 listener1;
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                888 -> {
                    val orientation = msg.arg1
                    if (orientation > 45 && orientation < 135) {
                        if (currentOrientiton != RIGHT_ORIENTATION) {
                            currentOrientiton = RIGHT_ORIENTATION
                            i("ScreenSwitchUtils", "反转横屏")
                            mActivity!!.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        }
                    } else if (orientation > 135 && orientation < 225) {
                        if (currentOrientiton != TOP_ORIENTATION) {
                            currentOrientiton = TOP_ORIENTATION
                            i("ScreenSwitchUtils", "反转竖屏")
                            mActivity!!.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        }
                    } else if (orientation > 225 && orientation < 315) {
                        if (currentOrientiton != LEFT_ORIENTATION) {
                            currentOrientiton = LEFT_ORIENTATION
                            i("ScreenSwitchUtils", "横屏")
                            mActivity!!.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        }
                    } else if (orientation > 315 && orientation < 360 || orientation > 0 && orientation < 45) {
                        if (currentOrientiton != BOTTOM_ORIENTATION) {
                            currentOrientiton = BOTTOM_ORIENTATION
                            i("ScreenSwitchUtils", "竖屏")
                            mActivity!!.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * 开始监听
     */
    fun start(activity: Activity?) {
        mActivity = activity
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    /**
     * 停止监听
     */
    fun stop() {
        sm.unregisterListener(listener)
    }

    /**
     * 重力感应监听者
     */
    inner class OrientationSensorListener(private val rotateHandler: Handler?) :
        SensorEventListener {
        override fun onAccuracyChanged(arg0: Sensor, arg1: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            val values = event.values
            var orientation = ORIENTATION_UNKNOWN
            val X = -values[_DATA_X]
            val Y = -values[_DATA_Y]
            val Z = -values[_DATA_Z]
            val magnitude = X * X + Y * Y
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                val OneEightyOverPi = 57.29577957855f
                val angle = Math.atan2(-Y.toDouble(), X.toDouble()).toFloat() * OneEightyOverPi
                orientation = 90 - Math.round(angle)
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360
                }
                while (orientation < 0) {
                    orientation += 360
                }
            }
            rotateHandler?.obtainMessage(888, orientation, 0)?.sendToTarget()
        }

        val _DATA_X = 0
        val _DATA_Y = 1
        val _DATA_Z = 2
        val ORIENTATION_UNKNOWN = -1

    }

    companion object {
        private val TAG = ScreenSwitchUtils::class.java.simpleName

        @Volatile
        private var mInstance: ScreenSwitchUtils? = null
        const val TOP_ORIENTATION = 1
        const val LEFT_ORIENTATION = 2
        const val BOTTOM_ORIENTATION = 3
        const val RIGHT_ORIENTATION = 4

        /**
         * 返回ScreenSwitchUtils单例
         */
        fun init(context: Context): ScreenSwitchUtils? {
            if (mInstance == null) {
                synchronized(ScreenSwitchUtils::class.java) {
                    if (mInstance == null) {
                        mInstance = ScreenSwitchUtils(context)
                    }
                }
            }
            return mInstance
        }
    }

    init {

        // 注册重力感应器,监听屏幕旋转
        sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        listener = OrientationSensorListener(mHandler)
    }
}