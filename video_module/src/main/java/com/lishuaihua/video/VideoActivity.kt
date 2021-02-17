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
        binding.editText.setText(path)

        binding.jackVideoView.setHudView(binding.hudView)

        //设置播放地址
        binding.jackVideoView.setVideoPath(path)
        //开始播放
        binding.jackVideoView.start()
    }


    /**
     * 播放输入的流路径
     */
    fun play(view: View?) {
        path = binding.editText.getText().toString()
        binding.jackVideoView.setVideoPath(path)
        if (binding.jackVideoView.isPlaying) {
            //暂停播放
            binding.jackVideoView.pause()
            binding.jackVideoView.start()
        } else {
            //开始播放
            binding.jackVideoView.start()
        }
    }

    /**
     *    播放流或者暂停流播放
     */
    fun toggle(view: View?) {
        if (binding.jackVideoView.isPlaying) {
            //暂停播放
            binding.jackVideoView.pause()
        } else {
            //开始播放
            binding.jackVideoView.start()
        }
    }

    /**
     *    全屏播放
     */
    fun fullScreen(view: View?) {
        binding.jackVideoView.fullScreen(this)
        updateToolbar()
    }

    private fun updateToolbar() {
        if (binding.jackVideoView.isFullState) {
            Log.d("updateToolbar", "isFullState=" + binding.jackVideoView.isFullState)
            Log.d("updateToolbar", "navigationBar=" + binding.navigationBar.getVisibility())

            if (binding.navigationBar.getVisibility() == View.VISIBLE) {
                binding.navigationBar.setVisibility(View.GONE)
                binding.llPath.setVisibility(View.GONE)
            } else {
                binding.navigationBar.setVisibility(View.VISIBLE)
                binding.llPath.setVisibility(View.VISIBLE)
            }
        } else {
            binding.navigationBar.visibility = View.VISIBLE
            binding.llPath.visibility = View.VISIBLE
        }

    }

    /**
     * 视频播放信息
     */
    fun showPlayInfo(view: View?) {
        if (binding.hudView.getVisibility() == View.VISIBLE) {
            binding.hudView.setVisibility(View.GONE)
        } else {
            binding.hudView.setVisibility(View.VISIBLE)
        }
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
        if (!binding.jackVideoView.isFullState) {
            //退出全屏
            binding.jackVideoView.fullScreen(this)
            binding.jackVideoView.exitFullScreen(this)
            return
        }
        super.onBackPressed()
    }
}