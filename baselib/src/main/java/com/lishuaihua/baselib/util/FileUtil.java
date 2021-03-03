package com.lishuaihua.baselib.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.lishuaihua.baselib.bus.LiveDataBus;
import com.lishuaihua.baselib.constant.Constant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {


    /**
     * 创建文件夹
     * @param fileName
     * @return
     */
    public static File createFile(String fileName) {
        File storage= new File(Environment.getExternalStorageDirectory().getPath());
        if (!storage.exists()) {
            storage.mkdirs();
        }
        File file = new File(storage, fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    public void copyFile(Context context,String from, String to) {
        InputStream inputStream =null;
        OutputStream outputStream =null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(from);
            if (oldfile.exists()) {
                inputStream = context.getResources().getAssets().open(from);//将assets中的内容以流的形式展示出来
                outputStream = new BufferedOutputStream(new FileOutputStream(to));//to为要写入sdcard中的文件名称
                byte[] buffer = new byte[1024];
                while ((byteread = inputStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    outputStream.write(buffer, 0, byteread);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 将assets中的文件拷贝到app的缓存目录，并且返回拷贝之后文件的绝对路径
     * @param context
     * @param fileName
     * @return
     */
    public static String copyAssetToCache(Context context, String fileName) {

        File cacheDir = context.getCacheDir();//app的缓存目录
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();//如果没有缓存目录，就创建
        }
        File outPath = new File(cacheDir, fileName);//创建输出的文件位置
        if (outPath.exists()) {
            outPath.delete();//如果该文件已经存在，就删掉
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            boolean res = outPath.createNewFile();//创建文件，如果创建成功，就返回true
            if (res) {
                is = context.getAssets().open(fileName);//拿到main/assets目录的输入流，用于读取字节
                fos = new FileOutputStream(outPath);//读取出来的字节最终写到outPath
                byte[] buf = new byte[is.available()];//缓存区
                int byteCount;
                while ((byteCount = is.read(buf)) != -1) {//循环读取
                    fos.write(buf, 0, byteCount);
                }
                LiveDataBus.Companion.get().with(Constant.Action,Integer.class).postValue(Constant.CopySuccess);
                return outPath.getAbsolutePath();
            } else {
                Toast.makeText(context, "插件apk创建失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



}
