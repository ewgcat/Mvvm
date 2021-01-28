package com.lishuaihua.baselib.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.lishuaihua.baselib.util.Logger.Companion.e
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件存储工具类
 */
object FileUtils {
    /**
     * app文件存放的根目录
     */
    const val ROOT_STORAGE_PATH = "gailen"

    /**
     * 图片文件存放的根目录
     */
    const val FILE_IMAGE = "image"

    /**
     * 创建文件夹
     *
     * @param fileName
     * @return
     */
    fun createFile(fileName: String?): File {
        val storage: File
        storage = if (sdCardExist()) {
            File(
                Environment.getExternalStorageDirectory().path,
                ROOT_STORAGE_PATH
            )
        } else {
            File(
                Environment.getExternalStorageDirectory().path,
                ROOT_STORAGE_PATH
            )
        }
        if (storage != null && !storage.exists()) {
            storage.mkdirs()
        }
        val file = File(storage, fileName)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /*
     * 保存文件，文件名为当前日期
     */
    fun saveBitmap(context: Context, bitmap: Bitmap?, bitName: String): Boolean {
        if (bitmap == null) {
            return false
        }
        val fileName = Environment.getExternalStorageDirectory().path + "/DCIM/" + bitName
        var isSave = false
        val file = File(fileName)
        if (file.exists()) {
            file.delete()
        }
        val out: FileOutputStream
        try {
            out = FileOutputStream(file)
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                isSave = true
                out.flush()
                out.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            e(e.message)
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return isSave
        } catch (e: IOException) {
            e(e.message)
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return isSave
        }

        // 其次把文件插入到系统图库
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        val uri =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // 最后通知图库更新
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$fileName")
            )
        )
        return isSave
    }

    fun getFile(filepath: String?): Int {
        var count = 0
        val file = File(filepath)
        if (!file.exists()) {
            return 0
        }
        val listfile = file.listFiles()
        for (i in listfile.indices) {
            if (!listfile[i].isDirectory) {
                count++
            } else {
                getFile(listfile[i].toString())
            }
        }
        return count
    }

    /**
     * 判断sd卡是否存在
     *
     * @return
     */
    fun sdCardExist(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED //判断sd卡是否存在
    }

    /**
     * 清除文件
     * 建议在自线程中处理
     */
    fun clearSDFile() {
        deleteDirectory(Environment.getExternalStorageDirectory().absoluteFile.toString() + File.pathSeparator + ROOT_STORAGE_PATH)
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的路径+文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(sPath: String?): Boolean {
        var flag = false
        val file = File(sPath)
        // 路径为文件且不为空则进行删除
        if (file.isFile && file.exists()) {
            file.delete()
            flag = true
        }
        return flag
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    fun deleteDirectory(sPath: String): Boolean {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        var sPath = sPath
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator
        }
        val dirFile = File(sPath)
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory) {
            return false
        }
        var flag: Boolean? = true
        // 删除文件夹下的所有文件(包括子目录)
        val files = dirFile.listFiles()
        for (i in files.indices) {
            // 删除子文件
            if (files[i].isFile) {
                flag = deleteFile(files[i].absolutePath)
                if (!flag) break
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].absolutePath)
                if (!flag) break
            }
        }
        if (!flag!!) return false
        // 删除当前目录
        return if (dirFile.delete()) {
            true
        } else {
            false
        }
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(file)
                size = fis.available().toLong()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            e("获取文件大小", "文件不存在!")
        }
        return size
    }

    /**
     * 根据路径获取bitmap（压缩后）
     *
     * @param srcPath 图片路径
     * @param width   最大宽（压缩完可能会大于这个，这边只是作为大概限制，避免内存溢出）
     * @param height  最大高（压缩完可能会大于这个，这边只是作为大概限制，避免内存溢出）
     * @param size    图片大小，单位kb
     * @return 返回压缩后的bitmap
     */
    fun compressBitmapAndSave(
        srcPath: String?,
        width: Float,
        height: Float,
        size: Int,
        desPath: String
    ) {
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(srcPath, newOpts)
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        val scaleW = (w / width).toInt()
        val scaleH = (h / height).toInt()
        var scale = if (scaleW < scaleH) scaleH else scaleW
        if (scale <= 1) {
            scale = 1
        }
        newOpts.inSampleSize = scale // 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        val bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
        // 压缩好比例大小后再进行质量压缩
        val bitmap1 = compressImage(bitmap, size)
        saveBitmapToFile(bitmap1, desPath)
    }

    /**
     * 图片质量压缩
     *
     * @param image 传入的bitmap
     * @param size  压缩到多大，单位kb
     * @return 返回压缩完的bitmap
     */
    fun compressImage(image: Bitmap, size: Int): Bitmap? {
        val baos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        image.compress(Bitmap.CompressFormat.JPEG, options, baos)
        // 循环判断如果压缩后图片是否大于size,大于继续压缩
        while (baos.toByteArray().size / 1024 > size) {
            // 重置baos即清空baos
            baos.reset()
            // 每次都减少10
            options -= 10
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        val isBm = ByteArrayInputStream(baos.toByteArray())
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * bitmap保存为文件
     *
     * @param bm       bitmap
     * @param filePath 文件路径
     * @return 返回保存结果 true：成功，false：失败
     */
    fun saveBitmapToFile(bm: Bitmap?, filePath: String): Boolean {
        try {
            val file = File(filePath)
            file.deleteOnExit()
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            val bos = BufferedOutputStream(FileOutputStream(file))
            var b = false
            b = if (filePath.toLowerCase().endsWith(".png")) {
                bm!!.compress(Bitmap.CompressFormat.PNG, 100, bos)
            } else {
                bm!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            }
            bos.flush()
            bos.close()
            return b
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return false
    }

    /**
     * 存放拍摄图片的文件夹
     */
    private const val FILES_NAME = "/MyPhoto"

    /**
     * 获取的时间格式
     */
    const val TIME_STYLE = "yyyyMMddHHmmss"

    /**
     * 图片种类
     */
    const val IMAGE_TYPE = ".png"

    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private fun getPhoneRootPath(context: Context): String {
        // 是否有SD卡
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            context.externalCacheDir!!.path
        } else {
            // 获取apk包下的缓存路径
            context.cacheDir.path
        }
    }

    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    fun getPhotoFileName(context: Context): String {
        val file = File(getPhoneRootPath(context) + FILES_NAME)
        // 判断文件是否已经存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs()
        }
        // 设置图片文件名称
        val format = SimpleDateFormat(TIME_STYLE, Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        val time = format.format(date)
        val photoName = "/" + time + IMAGE_TYPE
        return file.toString() + photoName
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    fun savePhotoToSD(mbitmap: Bitmap?, context: Context): String? {
        var outStream: FileOutputStream? = null
        val fileName = getPhotoFileName(context)
        return try {
            outStream = FileOutputStream(fileName)
            // 把数据写入文件，100表示不压缩
            mbitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            fileName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                outStream?.close()
                mbitmap?.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    fun getCompressPhoto(path: String?): Bitmap {
        var options: BitmapFactory.Options? = BitmapFactory.Options()
        options!!.inJustDecodeBounds = false
        options.inSampleSize = 100 // 图片的大小设置为原来的十分之一
        val bmp = BitmapFactory.decodeFile(path, options)
        options = null
        return bmp
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath 原图路径
     * @param context    上下文
     * @return 返回修复完毕后的图片路径
     */
    fun amendRotatePhoto(originpath: String?, context: Context): String? {

        // 取得图片旋转角度
        val angle = readPictureDegree(originpath)

        // 把原图压缩后得到Bitmap对象
        val bmp = getCompressPhoto(originpath)

        // 修复图片被旋转的角度
        val bitmap = rotaingImageView(angle, bmp)

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, context)
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    fun readPictureDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path!!)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap? {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: OutOfMemoryError) {
        }
        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap != returnBm) {
            bitmap.recycle()
        }
        return returnBm
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    fun deleteFile(file: File) {
        if (file.exists()) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                val files = file.listFiles()
                for (i in files.indices) {
                    deleteFile(files[i])
                }
            }
            file.delete()
        } else {
            e("delete===", "delete file no exists " + file.absolutePath)
        }
    }

    fun getAppRootPath(context: Context): File {
        return if (sdCardIsAvailable()) {
            Environment.getExternalStorageDirectory()
        } else {
            context.filesDir
        }
    }

    fun sdCardIsAvailable(): Boolean {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().canWrite()
        } else false
    }
}