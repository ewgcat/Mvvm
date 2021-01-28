package com.lishuaihua.baselib.util

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.ListView
import androidx.core.widget.NestedScrollView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object ScreenShotUtils {
    private const val TAG = "ScreenShotUtils"

    // 获取指定Activity的截屏，保存到png文件
    fun takeScreenShot(activity: Activity, navigationBarHeight: Int): Bitmap {
        // View是你需要截图的View
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache

        // 获取状态栏高度
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        println(statusBarHeight)

        // 获取屏幕长和高
        val width = activity.windowManager.defaultDisplay.width
        val height = activity.windowManager.defaultDisplay
            .height
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        val b = Bitmap.createBitmap(
            b1, 0, statusBarHeight + navigationBarHeight, width, height
                    - statusBarHeight - navigationBarHeight
        )
        view.destroyDrawingCache()
        return b
    }

    // 保存到sdcard
    fun savePic(b: Bitmap, strFileName: String?) {
        var fos: FileOutputStream? = null
        Log.d("TAG", "savePic() returned: ")
        try {
            fos = FileOutputStream(strFileName)
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos)
                Log.d("TAG", "savePic() returned:    " + b.height)
                fos.flush()
                fos.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 截取当前屏幕
    fun shootScreenView(a: Activity, picpath: String?) {
        savePic(takeScreenShot(a, 0), picpath)
    }

    /**
     * 保存bitmap到SD卡
     * @param path 保存的名字
     * @param mBitmap 图片对像
     * return 生成压缩图片后的图片路径
     */
    fun saveMyBitmap(mBitmap: Bitmap?, path: String?): Boolean {
        val f = File(path)
        try {
            f.createNewFile()
        } catch (e: IOException) {
            println("在保存图片时出错：$e")
            return false
        }
        var fOut: FileOutputStream? = null
        fOut = try {
            FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }
        try {
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        } catch (e: Exception) {
            return false
        }
        try {
            fOut?.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        try {
            fOut?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    // 程序入口 截取ScrollView
    fun shotNestedScrollView(scrollView: NestedScrollView, picpath: String?): Boolean {
        return saveMyBitmap(getNestedScrollViewBitmap(scrollView, picpath), picpath)
    }

    /**
     * 截取scrollview的屏幕
     */
    fun getNestedScrollViewBitmap(scrollView: NestedScrollView, picpath: String?): Bitmap? {
        val bitmap: Bitmap
        // 获取ScrollView实际高度
        scrollView.getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"))
        val height = scrollView.height
        val h = scrollView.getChildAt(0).height
        Log.d(TAG, "实际高度:$h")
        Log.d(TAG, " 高度:$height")
        if (h == 0 || height == 0) {
            return null
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        // 测试输出
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(picpath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
            }
        } catch (e: IOException) {
        }
        Log.d("TAG", "getScrollViewBitmap() returned: " + bitmap.height)
        return bitmap
    }

    // 截屏 ListView
    fun shootListView(listView: ListView, picpath: String?) {
        savePic(getListViewBitmap(listView, picpath), picpath)
    }

    /**
     * 截图listview
     */
    fun getListViewBitmap(listView: ListView, picpath: String?): Bitmap {
        var h = 0
        val bitmap: Bitmap
        // 获取listView实际高度
        for (i in 0 until listView.childCount) {
            h += listView.getChildAt(i).height
        }
        Log.d(TAG, "实际高度:$h")
        Log.d(TAG, "list 高度:" + listView.height)
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(
            listView.width, h,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        listView.draw(canvas)
        // 测试输出
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(picpath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
            }
        } catch (e: IOException) {
        }
        return bitmap
    }

    private fun getViewBitmapWithoutBottom(v: View?): Bitmap? {
        if (null == v) {
            return null
        }
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache()
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.height, View.MeasureSpec.EXACTLY)
            )
            v.layout(
                v.x.toInt(), v.y.toInt(), v.x.toInt() + v.measuredWidth, v.y
                    .toInt() + v.measuredHeight
            )
        } else {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        }
        val bp = Bitmap.createBitmap(
            v.drawingCache,
            0,
            0,
            v.measuredWidth,
            v.measuredHeight - v.paddingBottom
        )
        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
        return bp
    }

    fun getViewBitmap(v: View?): Bitmap? {
        if (null == v) {
            return null
        }
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache()
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.height, View.MeasureSpec.EXACTLY)
            )
            v.layout(
                v.x.toInt(), v.y.toInt(), v.x.toInt() + v.measuredWidth, v.y
                    .toInt() + v.measuredHeight
            )
        } else {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        }
        val b = Bitmap.createBitmap(v.drawingCache, 0, 0, v.measuredWidth, v.measuredHeight)
        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
        return b
    }

    /**
     * 获取 WebView 视图截图
     * @param context
     * @param view
     * @return
     */
    fun getWebViewBitmap(context: Context, view: WebView?): Bitmap? {
        if (null == view) return null
        view.scrollTo(0, 0)
        view.buildDrawingCache(true)
        view.isDrawingCacheEnabled = true
        view.isVerticalScrollBarEnabled = false
        var b = getViewBitmapWithoutBottom(view)
        // 可见高度
        var vh = view.height
        // 容器内容实际高度
        val th = (view.contentHeight * view.scale).toInt()
        var temp: Bitmap? = null
        if (th > vh) {
            val w = getScreenWidth(context)
            val absVh = vh - view.paddingTop - view.paddingBottom
            do {
                val restHeight = th - vh
                if (restHeight <= absVh) {
                    view.scrollBy(0, restHeight)
                    vh += restHeight
                    temp = getViewBitmap(view)
                } else {
                    view.scrollBy(0, absVh)
                    vh += absVh
                    temp = getViewBitmapWithoutBottom(view)
                }
                b = mergeBitmap(vh, w, temp, 0f, view.scrollY.toFloat(), b, 0f, 0f)
            } while (vh < th)
        }
        // 回滚到顶部
        view.scrollTo(0, 0)
        view.isVerticalScrollBarEnabled = true
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        return b
    }

    /**
     * 拼接图片
     * @param newImageH
     * @param newImageW
     * @param background
     * @param backX
     * @param backY
     * @param foreground
     * @param foreX
     * @param foreY
     * @return
     */
    private fun mergeBitmap(
        newImageH: Int,
        newImageW: Int,
        background: Bitmap?,
        backX: Float,
        backY: Float,
        foreground: Bitmap?,
        foreX: Float,
        foreY: Float
    ): Bitmap? {
        if (null == background || null == foreground) {
            return null
        }
        val bitmap = Bitmap.createBitmap(newImageW, newImageH, Bitmap.Config.RGB_565)
        val cv = Canvas(bitmap)
        cv.drawBitmap(background, backX, backY, null)
        cv.drawBitmap(foreground, foreX, foreY, null)
        cv.save()
        cv.restore()
        return bitmap
    }

    /**
     * get the width of screen
     */
    fun getScreenWidth(ctx: Context): Int {
        var w = 0
        w = if (Build.VERSION.SDK_INT > 13) {
            val p = Point()
            (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(
                p
            )
            p.x
        } else {
            (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.width
        }
        return w
    }

    /**
     * webview截图
     * @param context
     * @param webView
     * @return
     */
    fun getFullWebViewSnapshot(context: Context, webView: WebView): Boolean {
        //重新调用WebView的measure方法测量实际View的大小（将测量模式设置为UNSPECIFIED模式也就是需要多大就可以获得多大的空间）
        webView.measure(
            View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        //调用layout方法设置布局（使用新测量的大小）
        webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)
        //开启WebView的缓存(当开启这个开关后下次调用getDrawingCache()方法的时候会把view绘制到一个bitmap上)
        webView.isDrawingCacheEnabled = true
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
        webView.buildDrawingCache()
        //根据测量结果创建一个大小一样的bitmap
        val picture = Bitmap.createBitmap(
            webView.measuredWidth,
            webView.measuredHeight, Bitmap.Config.ARGB_8888
        )
        //已picture为背景创建一个画布
        val canvas = Canvas(picture) // 画布的宽高和 WebView 的网页保持一致
        val paint = Paint()
        //设置画笔的定点位置，也就是左上角
        canvas.drawBitmap(picture, 0f, webView.measuredHeight.toFloat(), paint)
        //将webview绘制在刚才创建的画板上
        webView.draw(canvas)
        //将bitmap保存到SD卡
        return FileUtils.saveBitmap(
            context,
            picture,
            System.currentTimeMillis().toString() + ".JPEG"
        )
    }
}