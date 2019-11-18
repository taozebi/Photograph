package com.ewide.photograph.common.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.common.adapter.MediaAdapter;
import com.ewide.photograph.common.listener.CompressCallBack;
import com.ewide.photograph.common.util.PhotoTask;
import com.ewide.photograph.common.util.PhotoUtils;
import com.ewide.photograph.common.util.T;
import com.ewide.photograph.common.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * MediaGridView
 * Created by Taoze on 2017/5/11.
 */
public class MediaGridView extends GridView {

    private Context mContext;
    private Activity mActivity;

    private LayoutInflater mInflater;

    private File mDir;

    private MediaAdapter adapter;

    private boolean isCamera;

    /**
     * 控制拍照或者视频上限
     */
    private int count = 4;

    private final String[] items = {"照片", "视频"};

    private String cachePath = "";


    private PhotoTask.PhotoHintListener photoHintListener;

    public PhotoTask.PhotoHintListener getPhotoHintListener() {
        return photoHintListener;
    }

    public void setPhotoHintListener(PhotoTask.PhotoHintListener photoHintListener) {
        this.photoHintListener = photoHintListener;
    }

    public MediaGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public String getCachePath() {
        return cachePath;
    }

    public MediaAdapter getMediaAdapter() {
        return adapter;
    }

    private void init() {
        this.adapter = new MediaAdapter(mContext, mDir, isCamera, count);
        setAdapter(adapter);
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final String fileName = (String) adapter.getItem(position);
                if (fileName.startsWith("_") || !isCamera) {
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示:");
                builder.setMessage("是否确认删除?");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(mDir + File.separator + fileName);
                                if (file.exists()) {
//									file.delete();
//									Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//									intent.setAction(Intent.ACTION_MEDIA_REMOVED);
//					                Uri uri = Uri.fromFile(file);
//					                intent.setData(uri);
//					                mContext.sendBroadcast(intent);
                                    DeleteImage(mDir + File.separator + fileName);
                                }
                                refresh(false);
                            }
                        });

                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                return true;
            }
        });

        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isCamera) {
                    String fileName = (String) adapter.getItem(position);
                    if (fileName.startsWith("_")) {
                        takePhoto();
                    } else {
                        showFile(position);
                    }
                } else {
                    showFile(position);
                }
            }
        });
    }

    /**
     * 拍照
     */
    public void takePhoto() {
//		if (adapter.getCount() <= count) {
//							chooseDialog();
        // 调用拍照（系统相机）
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.CHINA);
        String fileName1 = "";
        fileName1 = "IMG_" + dateFormat.format(new Date());
        File dic = new File(mDir.getAbsolutePath());
        if (!dic.exists()) {
            dic.mkdirs();
        }
        cachePath = mDir.getAbsolutePath() + File.separator + fileName1 + ".jpg";
        File file = new File(cachePath);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, com.ewide.photograph.BuildConfig.APPLICATION_ID + ".provider", file);

            Log.e(MRApplication.TAG, "takePhoto URI:" + uri.toString());
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//存储到SD卡
//		if (mFragment != null) {
//			mFragment.startActivityForResult(intent, 0);
//		} else {
        mActivity.startActivityForResult(intent, 0);
//		}
//		} else {
//			T.showShort(mContext, "限定只能上传" + count + "张照片,如需修改先长按图片删除");
//		}
    }

    private void showFile(int position) {
        String filePath = (String) adapter.getItem(position);
        Intent it = new Intent(Intent.ACTION_VIEW);
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
            File file = new File(mDir + File.separator + filePath);

//			Uri uri = Uri.parse("file://"+file.getPath());
            Uri uri = PhotoUtils.getImageContentUri(mContext, file);
            it.setDataAndType(uri, "image/*");
            ExifInterface exifInterface;
            try {
                exifInterface = new ExifInterface(filePath);
                exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mContext.startActivity(it);
        } else if (filePath.endsWith(".mp4")) {
            File file = new File(mDir + File.separator + filePath);
//			Uri mUri = null;
//			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//				mUri = FileProvider.getUriForFile(mContext, com.ewide.sjcb.BuildConfig.APPLICATION_ID + ".provider", file);
//			}else{
//				mUri = Uri.fromFile(file);
//			}
//			Uri mUri = Uri.parse("file://"+file.getPath());
            Uri mUri = PhotoUtils.getImageContentUri(mContext, file);
            it.setDataAndType(mUri, "video/*");
            mContext.startActivity(it);
        } else {
            T.showShort(mContext, "无法支持的文件格式");
        }

    }

    /**
     * 设置多媒体文件根目录
     *
     * @param file      目录
     * @param mFragment 是否是在BaseFragment中请求的
     * @param canCreate 是否支持新增功能
     */
    public void setScanDir(File file, Activity activity, boolean canCreate, int count) {
        this.mDir = file;
        this.isCamera = canCreate;
        this.mActivity = activity;
        this.count = count;
        init();
    }

    public void initRefresh() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cachePath = "";
                init();
            }
        });
    }


    /**
     * 刷新
     *
     * @param isCompress 是否压缩文件
     */
    public void refresh(boolean isCompress) {
        if (isCompress) {
            if (!TextUtils.isEmpty(cachePath)) {
                File file = new File(cachePath);
                if (file.isFile()) {
                    Utils.compress(mContext, file, new CompressCallBack() {
                        @Override
                        public void comPressStart() {
                        }

                        @Override
                        public void comPressSuccess(File file) {
                            PhotoTask photoTask = new PhotoTask(mContext, cachePath);
                            photoTask.setPhotoHintListener(getPhotoHintListener());
                            photoTask.start();
//							cachePath = "";
//							init();
                        }

                        @Override
                        public void comPressFiled(Throwable e) {
                            new PhotoTask(mContext, cachePath).start();
//							cachePath = "";
//							init();
                        }
                    });
                }
            }
        } else {
//            PhotoTask photoTask = new PhotoTask(mContext, cachePath);
//            photoTask.setPhotoHintListener(getPhotoHintListener());
//            photoTask.start();
			cachePath = "";
			init();
        }
    }

    /**
     * 选择拍照或者视频对话框
     */
    private void chooseDialog() {
        new AlertDialog.Builder(mContext)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {// 添加照片
                            // 调用拍照（系统相机）
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                            String fileName = "IMG_" + dateFormat.format(new Date());
                            File dic = new File(mDir.getAbsolutePath());
                            if (!dic.exists()) {
                                dic.mkdirs();
                            }
                            cachePath = mDir.getAbsolutePath() + File.separator + fileName + ".jpg";
                            File file = new File(cachePath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//存储到SD卡
//                            if (mFragment != null) {
//                                mFragment.startActivityForResult(intent, 0);
//                            } else {
                                ((Activity) mContext).startActivityForResult(intent, 0);
//                            }
                            //TODO　调用自定义相机（屏蔽）
//							Intent  intent = new Intent(mContext,CustomCameraActivity.class);
//							intent.putExtra(CustomCameraActivity.EXTRA_FILE_PATH, mDir.getPath());
//							if(mContext instanceof Activity){
//								((Activity) mContext).startActivityForResult(intent,0);
//							}
                        } else if (item == 1) {// 添加视频
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            File dic = new File(mDir.getAbsolutePath());
                            if (!dic.exists()) {
                                dic.mkdirs();
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                            String name = "VID_" + dateFormat.format(new Date()) + ".mp4";
                            File videofile = new File(mDir.getPath() + "/" + name);
                            Uri originalUri = Uri.fromFile(videofile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
//                            if (mFragment != null) {
//                                mFragment.startActivityForResult(intent, 0);
//                            } else {
                                ((Activity) mContext).startActivityForResult(intent, 0);
//                            }
                        } else if (item == 2) {
//							Intent intent=new Intent(mContext,ScrawlActivity.class);
//							intent.putExtra("save_Path", mDir.getPath());
//							intent.putExtra("drawMap", "drawMap");
//							((Activity) mContext).startActivityForResult(intent,0);
                        }
                    }
                }).create().show();

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private void DeleteImage(String imgPath) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = mContext.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }
        if (result) {
            adapter.notifyDataSetChanged();
            Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
        }
    }
}

