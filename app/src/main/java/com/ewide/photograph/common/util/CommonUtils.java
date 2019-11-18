package com.ewide.photograph.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static double M_PI = Math.PI;

    /**
     * 判断包是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 安装应用程序
     *
     * @param context
     * @param apkFile
     */
    public static void installApp(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开应用程序
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 判断不是为null 和""
     */
    public static boolean isNullAnd(String str) {
        if ("".equals(str) || null == str) {
            return false;
        }
        return true;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\PrintBitmap{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * y验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证
     *
     * @param num
     * @return
     */
    public static boolean verForm(String num) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }

    /**
     * 得到设备 的唯一标识符
     *
     * @return
     */
    public static String getSzimei(Context mContext) {
        String szimei;
//        try {
//            szimei = ((TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE)).getDeviceId();
//        } catch (Exception e) {
//            return "";
//        }

        szimei = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return szimei;
    }

    /**
     * 创建一个请求参数map对象
     */
    public static Map<String, String> getMapParm() {
        Map<String, String> parm = new HashMap<String, String>();
        return parm;
    }

    public static Map<String, Integer> getMapParmInt() {
        Map<String, Integer> parm = new HashMap<String, Integer>();
        return parm;
    }
/*
    *//**
     * 调用客服qq
     *//*
    public static void toGetQQAuth(Activity context, String qq) {
        QQAuth mqqAuth = QQAuth.createInstance("1105537388", context);
        WPA mWPA = new WPA(context, mqqAuth.getQQToken());
        String ESQ = qq;
        int ret = mWPA.startWPAConversation(context, ESQ, "你好");
        if (ret != 0) { //如果ret不为0，就说明调用SDK出现了错误
            Toast.makeText(context.getApplicationContext(),
                    "抱歉，联系客服出现了错误~. error:" + ret,
                    Toast.LENGTH_LONG).show();
        }
    }*/


    /**
     * intent跳转
     */
    public static void startIntent(Context context, Class clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * intent跳转
     */
    public static void startIntent(Activity context, Class clazz, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startIntent(Fragment context, Class clazz, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context.getActivity(), clazz);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * intent跳转
     */
    public static void startIntent(Activity context, Class clazz, Bundle b, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.putExtras(b);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * intent跳转
     */
    public static void startIntent(Context context, Class clazz, Bundle b) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.putExtras(b);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到短信登陆
     */
    public static Intent toLoginIntent() {
        Intent intent = new Intent();
        intent.setAction("ui.user.LoginSmsActivity");
        return intent;
    }


    //经纬度转墨卡托
    public static List<Double> lonLat2Mercator(double x, double y) {
        List<Double> ls = new ArrayList<>();
        double xx = (x / 180.0) * 20037508.34;

        if (y > 85.05112) {
            y = 85.05112;
        }

        if (y < -85.05112) {
            y = -85.05112;
        }

        y = (Math.PI / 180.0) * y;
        double tmp = Math.PI / 4.0 + y / 2.0;
        double yy = 20037508.34 * Math.log(Math.tan(tmp)) / Math.PI;
        ls.add(xx);
        ls.add(yy);

        return ls;
    }

    //墨卡托转经纬度
    public static double[] Mercator2lonLat(double mercatorX, double mercatorY) {
        double[] xy = new double[2];
        double x = mercatorX / 20037508.34 * 180;
        double y = mercatorY / 20037508.34 * 180;
        y = 180 / M_PI * (2 * Math.atan(Math.exp(y * M_PI / 180)) - M_PI / 2);
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    /**
     * 字符串判断是否为“”
     */
    public static boolean isNoString(String str) {
        if ("".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

   /* *//**
     * 得到给出地址与现在地址间的距离
     *//*
    public static String getDistanceByLatLng(LatLng l1,int digit) {
        double jl;
        try{
           // LatLng nowlatlng = MapLongitudeUtil.getNowLatlng();
             //jl = MapLongitudeUtil.CalculatedDistance(l1, nowlatlng);
        }catch (Exception e){
            return "";
        }

        return BasicDataTypesUtil.doubleKeepTwoDecimalS(jl/digit);
    }*/

    /**
     * 创建一个主键
     */
    public static Long getPK() {
        long time = new Date().getTime();
        long l = (long) ((Math.random() * 9 + 1) * 100000);
        return time + l;
    }


    /**
     * 表情过滤
     */
    public static InputFilter emojiFilter() {
        return emojiFilter;
    }

    private static InputFilter emojiFilter = new InputFilter() {

        Pattern emoji = Pattern
                .compile(

                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\r\n]",

                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int arg1, int arg2,
                                   Spanned arg3, int arg4, int arg5) {
            // TODO Auto-generated method stub

            Matcher emojiMatcher = emoji.matcher(source);

            if (emojiMatcher.find()) {

                return "";

            }
            return null;
        }

    };

    public static void MyToast(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //车牌号判断规范
    public static boolean is_carNum(String text) {
        if (text.length() != 7) {
            return false;
        } else {
            List<String> head = new ArrayList<>();
            head.add("京");
            head.add("津");
            head.add("沪");
            head.add("渝");
            head.add("冀");
            head.add("豫");
            head.add("云");
            head.add("辽");
            head.add("黑");
            head.add("湘");
            head.add("皖");
            head.add("鲁");
            head.add("新");
            head.add("苏");
            head.add("浙");
            head.add("鄂");
            head.add("桂");
            head.add("甘");
            head.add("晋");
            head.add("蒙");
            head.add("陕");
            head.add("吉");
            head.add("闽");
            head.add("贵");
            head.add("粤");
            head.add("青");
            head.add("藏");
            head.add("川");
            head.add("宁");
            head.add("琼");
            head.add("WJ");
            //开头是否正确
            boolean IS_HEAD = false;
            for (int i = 0; i < head.size(); i++) {
                if (text.startsWith(head.get(i))) {
                    IS_HEAD = true;
                }
            }
            //开头第二个不可以为汉字
            boolean IS_SECOND_RIGHT = false;
            if ((text.charAt(1) >= 'A' && text.charAt(1) <= 'Z') || (text.charAt(1) <= '9' && text.charAt(1) >= '0')) {
                IS_SECOND_RIGHT = true;
            }
            if (text.length() <= 3) {
                return false;
            }
            //后面的数字串是否包含O  I
            String ends = text.substring(2, text.length());
            boolean IS_LEGALLNUM = false;
            if (ends.contains("I") || ends.contains("O")) {
                IS_LEGALLNUM = false;
            } else {
                IS_LEGALLNUM = true;
            }
            //是否为正确的区间
            boolean IS_IN_CHARGE = true;
            for (int i = 0; i < ends.length(); i++) {
                char cha = (ends.charAt(i));
                if ((cha >= 'A' && cha <= 'Z') || (cha <= '9' && cha >= '0')) {
                    IS_IN_CHARGE = true;
                } else {
                    //一旦发现不规范的字就中断循环
                    IS_IN_CHARGE = false;
                    break;
                }
            }
            return IS_HEAD && IS_LEGALLNUM && IS_IN_CHARGE && IS_SECOND_RIGHT;
        }
    }

    //字数限制
    public static String getLimitNumText(String text, int num) {
        String finalText = text.replace("\n", "  ").trim();
        if (text.length() < num) {
            return finalText;
        } else {
            return finalText.substring(0, num);
        }
    }

    //把本地json文件变成json字符串的方法
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void makeCallPhone(String phone, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    //装填数据到map的方法
    public static Map<String, String> putDataIntoMap(Map<String, String> map, String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return map;
        }
        map.put(key, value);
        return map;
    }

    //获取栈顶activity
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else
            return null;
    }
    public static boolean checkPasswordCode(String psw) {
        for (int i = 0; i < psw.length(); i++) {
            char cha = psw.charAt(i);
            if ((cha >= 'A' && cha <= 'Z') || (cha <= '9' && cha >= '0') || (cha >= 'a' && cha <= 'z')) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static void closeInputSoft(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setWebview(WebView webView_goto, String urlOrSignDate, boolean is_signData) {
        webView_goto.getSettings().setJavaScriptEnabled(true);
        webView_goto.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView_goto.setVerticalScrollBarEnabled(true);
        webView_goto.getSettings().setDefaultTextEncodingName("UTF-8");
        webView_goto.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView_goto.getSettings().setUseWideViewPort(true);
        webView_goto.getSettings().setLoadWithOverviewMode(true);//
        webView_goto.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView_goto.setWebChromeClient(new WebChromeClient());
        //这个方法让webview加载自带加载效果的页面，解决一直停留加载状态的问题
        webView_goto.getSettings().setDomStorageEnabled(true);
        webView_goto.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        if (TextUtils.isEmpty(urlOrSignDate)) {
            return;
        }
        if (!is_signData) {
            webView_goto.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            webView_goto.loadUrl(urlOrSignDate);
        } else {
            webView_goto.loadDataWithBaseURL(null, urlOrSignDate, "text/html", "utf-8", null);
        }
    }

    public static void setWebview(WebView webView_goto, String urlOrSignDate, boolean is_signData, WebChromeClient webChromeClient) {
        webView_goto.getSettings().setJavaScriptEnabled(true);
        webView_goto.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView_goto.setVerticalScrollBarEnabled(true);
        webView_goto.getSettings().setDefaultTextEncodingName("UTF-8");
        webView_goto.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView_goto.getSettings().setUseWideViewPort(true);
        webView_goto.getSettings().setLoadWithOverviewMode(true);//
        webView_goto.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView_goto.setWebChromeClient(new WebChromeClient());
        //这个方法让webview加载自带加载效果的页面，解决一直停留加载状态的问题
        webView_goto.getSettings().setDomStorageEnabled(true);
        webView_goto.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        if (TextUtils.isEmpty(urlOrSignDate)) {
            return;
        }
        if (!is_signData) {
            if (webChromeClient != null) {
                webView_goto.setWebChromeClient(webChromeClient);
            }
            webView_goto.loadUrl(urlOrSignDate);
        } else {
            webView_goto.loadDataWithBaseURL(null, urlOrSignDate, "text/html", "utf-8", null);
        }
    }


    public static void setLeftImageResouce(TextView textView, int resource,Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        } else {
            textView.setCompoundDrawables(context.getResources().getDrawable(resource), null, null, null);
        }

    }

    public static boolean is_email(String email) {
        if (!email.contains("@")) {
            return false;
        }
        if (!email.contains(".")) {
            return false;
        }
        if ((email.indexOf('@') != (email.lastIndexOf('@')))) {
            return false;
        }
        if ((email.indexOf('@') > (email.lastIndexOf('.')))) {
            return false;
        }


        return true;
    }

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     *    * EditText获取焦点并显示软键盘
     *    
     */
    public static void requstFoucous(Activity activity, EditText editText) {
        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().toString().length());
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    public static void showKeyboard(EditText editText, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = inputMethodManager.isActive();
          inputMethodManager.showSoftInput(editText,0);

//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hiddeKeybord(View editText, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}
