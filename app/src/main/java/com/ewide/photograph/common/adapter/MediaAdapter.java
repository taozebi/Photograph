package com.ewide.photograph.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ewide.photograph.R;
import com.ewide.photograph.common.util.Utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * MediaAdapter
 */
public class MediaAdapter extends BaseAdapter {

    private static LruCache<String, Bitmap> lruCache;

    private Context mContext;
    private List<String> mImages;
    private LayoutInflater mInflater;
    private File mDir;

    private int mWidth = 0;

    private int layoutWidth = 0;

    private boolean isCamera;

    private int count = 4;

    private static Bitmap videoWatermark;

    public MediaAdapter(Context context, File fileDir, boolean isCamera, int count) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDir = fileDir;
        this.isCamera = isCamera;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        layoutWidth = (mWidth - 10) / (4 + 1) - 10;
        init();
    }

    public boolean isCamera() {
        return isCamera;
    }

    public void setCamera(boolean isCamera) {
        if (this.isCamera != isCamera) {
            this.isCamera = isCamera;
            init();
        }
    }

    private void init() {
        if (lruCache == null) {
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            int maxSize = maxMemory / 8;
            lruCache = new LruCache<String, Bitmap>(maxSize);
        }
        File[] files = mDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".mp4") || pathname.getName().endsWith(".jpg")
                        || pathname.getName().endsWith(".png")) {
                    return true;
                }
                return false;
            }
        });
        mImages = new ArrayList<String>();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    mImages.add(f.getName());
                }
            }
        }
        Collections.sort(mImages, new MediaFilter(mDir));
        if (isCamera) {
            mImages.add("_camera");
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImages != null ? mImages.size() : 0;
    }

    public int getImageSize() {
        if (mImages != null) {
            if (isCamera) {
                return mImages.size() - 1;
            } else {
                return mImages.size();
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.pictureItemLl);
//        	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,layoutWidth);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, layoutWidth);
            viewHolder.layout.setLayoutParams(lp);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.pic_iv);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindView(position, viewHolder);

        return convertView;
    }

    private void bindView(int position, ViewHolder viewHolder) {
        String source = mDir + "/" + mImages.get(position);
        if (!isCamera) {
            Bitmap bitmap = lruCache.get(source);
            if (bitmap != null) {
                viewHolder.imageView.setImageBitmap(bitmap);
            } else {
                viewHolder.imageView.setImageResource(R.drawable.icon_photo);
                LoadImageTask imageTask = new LoadImageTask(viewHolder.imageView);
                imageTask.execute(source);
            }
        } else {
            if (position < getCount() - 1) {
                Bitmap bitmap = lruCache.get(source);
                if (bitmap != null) {
                    viewHolder.imageView.setImageBitmap(bitmap);
                } else {
                    viewHolder.imageView.setImageResource(R.drawable.icon_photo);
                    LoadImageTask imageTask = new LoadImageTask(viewHolder.imageView);
                    imageTask.execute(source);
                }
            } else {
//				int height = viewHolder.imageView.getHeight();
//				int width = viewHolder.imageView.getWidth();
//				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(200,200);
//				viewHolder.imageView.setLayoutParams(lp);
                viewHolder.imageView.setImageResource(R.drawable.icon_add_pic);
            }
        }
        viewHolder.imageView.setTag(source);
    }

    final class ViewHolder {
        LinearLayout layout;
        ImageView imageView;
    }

    class LoadImageTask extends AsyncTask<String, Integer, Bitmap> {

        private String source;

        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            super();
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            source = params[0];
            Bitmap bitmap;
            if (source.endsWith(".mp4")) {
                bitmap = Utils.getVideoThumbnail(source, 200, 200, Thumbnails.MINI_KIND);
                if (videoWatermark == null) {
                    videoWatermark = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_video).copy(Config.ARGB_8888, true);
                }
                // 创建新缩略图
                Bitmap newThumb = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(newThumb);
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.drawBitmap(videoWatermark, (bitmap.getWidth() - videoWatermark.getWidth()) / 2, (bitmap.getHeight() - videoWatermark.getHeight()) / 2, null);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
                bitmap = newThumb;
            } else {
                bitmap = Utils.getImageThumbnail(source, 200, 200);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (source != null && result != null) {
                lruCache.put(source, result);
                if (source.equals(imageView.getTag())) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }


    class MediaFilter implements Comparator<String> {

        private File mDir;

        public MediaFilter(File mDir) {
            this.mDir = mDir;
        }

        @Override
        public int compare(String lhs, String rhs) {
            String source1 = mDir + "/" + lhs;
            String source2 = mDir + "/" + rhs;
            File file1 = new File(source1);
            File file2 = new File(source2);
            long time1 = file1.lastModified();
            long time2 = file2.lastModified();
            return time1 > time2 ? 1 : -1;
        }
    }
}

