package com.lishuaihua.video

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.video.databinding.ActivityVideoBinding

@Route(path = "/video/video")
class VideoActivity : BaseActivity() {

    var path = "rtmp://58.200.131.2:1935/livetv/hunantv"
//    var path = "/mnt/sdcard/test.mp3";
//    var path = "/mnt/sdcard/storage/emulated/0/2651H.mp4";
//    var path = "/mnt/sdcard/videotest/2641H.mp4";

    override fun getLayoutResId(): Int = R.layout.activity_video
    private val binding: ActivityVideoBinding by viewbind()


    override fun doCreateView(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding.navigationBar.setTitle("视频流")
        binding.navigationBar.setBackClickListener(this)


        //设置播放地址
        binding.jackVideoView.setVideoPath(path)
        //开始播放
        binding.jackVideoView.start()
    }





    override fun onResume() {
        super.onResume()
        // 重新开始播放器
        binding.jackVideoView.resume()
        binding.jackVideoView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.jackVideoView.pause()
    }


    override fun onStop() {
        super.onStop()
        binding.jackVideoView.release(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}