package com.ewide.photograph.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.ewide.photograph.R;
import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.common.listener.CompressCallBack;
import com.ewide.photograph.common.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

import static java.util.regex.Pattern.matches;

public class Utils {

    public static String CONFIG_PATH = "/config";//配置文件根路径

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //一些工具方法
    protected int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    protected int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * MD5 加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取MetaData信息
     *
     * @param name
     * @param def
     * @return
     */
    public static String getMetaDataValue(Context context, String name,
                                          String def) {
        String value = getMetaDataValue(context, name);
        return (value == null) ? def : value;
    }

    private static String getMetaDataValue(Context context, String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name
                    + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    // 获取SD卡根路径
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return "";
        }
        return sdDir.getPath() + "/";
    }

    /**
     * 获取应用根目录
     *
     * @param context
     * @return
     */
    public static String getAppDir(Context context, String appName) {
        if (getSDPath().equals("")) {
            return "";
        }
        String rootPath = getSDPath() + appName;
        File file = new File(rootPath);
        if (file.exists()) {
            return rootPath;
        } else {
            file.mkdirs();
            return rootPath;
        }
    }

    /**
     * 保存Bitmap到本地文件
     *
     * @param context     上下文
     * @param appName     App名称
     * @param fileDirName 文件保存父目录
     * @param fileName    文件名称
     * @param bitmap      图片
     */
    public void saveBitmapToFile(Context context, String appName, String fileDirName, String fileName, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(getAppDir(context, appName), fileDirName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String name = fileName + ".png";
        File file = new File(appDir, name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
//		try {
//			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
        // 通知图库更新
//		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width,
                                           int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public static int copyFile(String oldPath, String newPath) {
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return 0;
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return -1;
        } finally {
            if (fs != null) {
                try {
                    fs.flush();
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param fileName String 要复制的文件名称 如：readmachine.db
     * @param toPath   String 复制后路径 如：f:/fqf.txt
     */
    public static void copyFromAssets(String fileName, String toPath, Context context) {
        try {
            AssetManager sm = context.getAssets();
            InputStream fosfrom = sm.open(fileName);
            // InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toPath);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
        }
    }

    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dir 要删除的目录
     */
    public static boolean deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!deleteDirectory(entries[i])) {
                    return false;
                }
            } else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }
        if (!dir.delete()) {
            return false;
        }
        return true;
    }

    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    /**
     * 创建指定目录
     *
     * @param dirPath
     * @return
     */
    public static boolean createDirectory(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            return false;
        } else {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     *                return 生成压缩图片后的图片路径
     */
    public static File saveBitmapToSDCard(String bitDir, String bitName, Bitmap mBitmap) {
        File dir = new File(bitDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(bitDir + File.separator + bitName + ".png");
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("在保存图片时出错：" + e.toString());
            e.printStackTrace();
            return null;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return f;
    }

    /**
     * 拷贝配置文件数据
     *
     * @param assetDir 文件夹名称
     * @param dir      目录
     * @param context
     */
    public static void copyAssets(String assetDir, String dir, Context context) {
        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdir();

        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        copyAssets(fileName, dir + fileName + File.separator,
                                context);
                    } else {
                        copyAssets(assetDir + File.separator + fileName, dir
                                + File.separator + fileName, context);
                    }
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length()) {
                    in = context.getAssets().open(
                            assetDir + File.separator + fileName);
                } else {
                    in = context.getAssets().open(fileName);
                }
                OutputStream out = new FileOutputStream(outFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (Exception e) {
                Log.e("eeeeeeeee", e.getMessage());
            }
        }
    }

    /**
     * 数据保留小数位数格式转换
     *
     * @param num
     * @param maximumFractionDigits 保留的小数位数
     * @return
     */
    public static String numberFormat(double num, int maximumFractionDigits) {
        StringBuffer formatStr = new StringBuffer("0.");
        for (int i = 0; i < maximumFractionDigits; i++) {
            formatStr.append("0");
        }
        String pattern = formatStr.toString();
        if (maximumFractionDigits == 0) {
            pattern = "0";
        }
        DecimalFormat g = new DecimalFormat(pattern);
        g.setGroupingUsed(false);
        return g.format(num);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 根据像素转换成dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @param formatStr 所需日期对应的格式, 默认"yyyy-MM-dd HH:mm:ss"
     * @return str
     */
    public static String DateToStr(Date date, String formatStr) {
        if (TextUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str 需要转换的日期字符串
     *            * @param  formatStr 传入日期字符串对应的格式, eg:"yyyy-MM-dd HH:mm:ss"
     * @return date
     */
    public static Date StrToDate(String str, String formatStr) {

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算两个时间的差值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param format    时间格式
     * @return 返回long类型数组{天,小时,分钟,秒} long[]{day,hour,min,sec}
     */
    public long[] dateDiff(String startTime, String endTime, String format) {
        long[] results = new long[]{0, 0, 0, 0};
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");

            results[0] = day;
            results[1] = hour;
            results[2] = min;
            results[3] = sec;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;

    }

    /**
     * 比较两个日期的先后
     *
     * @param target
     * @param source
     * @return target 在 source之前则返回false, target在source之后 则返回true;
     */
    public static boolean isDateBefore(Date target, Date source) {
        long d1 = target.getTime();
        long d2 = source.getTime();
        if (d1 > d2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前的日期字符串
     * * @param  formatStr 所需日期字符串对应的格式, eg:"yyyy-MM-dd HH:mm:ss"
     *
     * @return date
     */
    public static String getDateStr(String formatStr) {
        String date = "";
        if (TextUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        date = format.format(new Date());
        return date;
    }

    /**
     * http://www.jihaoba.com/tools/haoduan/
     * 现有手机号段
     * 移动号段：139 138 137 136 135 134 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * 联通号段：130 131 132 155 156 166 185 186 145 175 176
     * 电信号段：133 153 177 173 180 181 189 191 199
     * 虚拟运营商号段：170 171
     *
     * @param phoneNum 电话号码
     * @return true 是电话号码; false 不是电话号码;
     */
    public static boolean isPhoneNumber(String phoneNum) {
        String pattern = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][0,1,3,5,6,7,8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
//		String pattern="^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|166|198|199|147)+\\PrintBitmap{8})?$";
        return phoneNum.matches(pattern);
    }

    /*** 验证电话号码
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTelephone(String str) {
        String regex = "^0(10|2[0-5789]-|\\d{3})-?\\d{7,8}$";
        Log.e("TAG", "phoneNum4:" + matches(regex, str));
        return matches(regex, str);
    }

    /**
     * 是否是固定电话或者手机号码
     * @param telNum 判断的号码
     * @return true 是固定电话或者手机号码, false 都不是
     */
    public static boolean isTelphoneOrMobile(String telNum){
        if(isPhoneNumber(telNum)){
            return true;
        }
        if(isTelephone(telNum)){
            return true;
        }
        return false;
    }


    // MD5加密，32位
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    /*
     * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。不需要权限，平板设备通用。
     * 获取成功率也较高，缺点是设备恢复出厂设置会重置。另外就是某些厂商的低版本系统会有bug，返回的都是相同的AndroidId
     *
     * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。
     * 不需要权限，通用性也较高，但我测试发现红米手机返回的是 0123456789ABCDEF 明显是一个顺序的非随机字符串。也不一定靠谱
     *
     * AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，如果刷机和恢复出厂设置会导致设备标识符重置这一点可以接受的话
     * ，那么将他们组合使用时，唯一性就可以应付绝大多数设备了
     * */
    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            String strMd5 = MD5(id);
            return strMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    /**
     * @param context
     * @param file    要压缩的图片
     */
    public static void compress(Context context, File file, CompressCallBack compressCallBack) {

        String fileDir = Utils.getAppDir(context, context.getString(R.string.app_name)) + "/themb";
        File thembFile = new File(fileDir);
        if (!thembFile.exists()) {
            thembFile.mkdirs();
        }
        Luban.with(context)
                .load(file)
                .ignoreBy(100)
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        String[] split = filePath.split("/");


                        return split[split.length - 1];
                    }
                })
                .setTargetDir(fileDir)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        compressCallBack.comPressStart();
                    }

                    @Override
                    public void onSuccess(File file) {
                        String absolutePath = file.getAbsolutePath();
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        compressCallBack.comPressSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        compressCallBack.comPressFiled(e);
                    }
                }).launch();
    }




    public static Bitmap getPrintTemplateFromAssertOrConfig(String asserFilePath) {

        //如果有配置模板优先配置模板
        String filePath = getPrintTemplateDownloadPath() + "/" + asserFilePath;
        File printTemplateFile = new File(filePath);

        //有效文件
        if (printTemplateFile.isFile() && printTemplateFile.length() > 100) {
            Bitmap template = getBitMapFromTemplateFile(filePath);
            if (null != template) {
                return template;
            }
        }

        //从assert目录读取
        InputStream in = null;
        Bitmap template = null;

        try {
            in = MRApplication.getInstance().getAssets().open(asserFilePath);
            template = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            LogUtils.e( "getBitMapFromAsser fail.", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    LogUtils.e( "getBitMapFromAsser fail.", e);
                }
            }
        }

        return template;
    }



    public static String getPrintTemplateDownloadPath() {


        return MRApplication.getInstance().getDir("template", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static Bitmap getBitMapFromTemplateFile(String templateFilePath) {
        InputStream in = null;
        Bitmap template = null;

        try {
            in = new FileInputStream(new File(templateFilePath));
            template = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            LogUtils.e("getBitMapFromAsser fail.", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    LogUtils.e( "getBitMapFromAsser fail.", e);
                }
            }
        }

        return template;
    }
}
