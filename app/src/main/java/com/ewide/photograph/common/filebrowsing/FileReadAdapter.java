package com.ewide.photograph.common.filebrowsing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewide.photograph.R;
import com.ewide.photograph.common.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewide.photograph.common.filebrowsing.FileReadWindows.MULTI_SELECT;
import static com.ewide.photograph.common.filebrowsing.FileReadWindows.TYPE_DIRECTORY;
import static com.ewide.photograph.common.filebrowsing.FileReadWindows.TYPE_FILE;

/**
 * author：Taozebi
 * date：2019/1/28 16:38
 * describe：
 */

public class FileReadAdapter  extends BaseAdapter {

    protected static final String FileReadWindows = null;
    private FileReadWindows activity;
    private LayoutInflater mInflater;
    // 文件夹图片
    private Bitmap mIcon2;
    // 其他文件
    private Bitmap mIcon3;
    private List<String> items;
    private List<String> paths;
    private int fileType;
    private int selectType;

    public Map<Integer, Boolean> selected = new HashMap<Integer, Boolean>();

    public FileReadAdapter(Activity activity, List<String> it, List<String> pa,
                           int fileType, int selectType) {
        this.activity = (FileReadWindows) activity;
        this.fileType = fileType;
        this.selectType = selectType;
        mInflater = LayoutInflater.from(activity);
        items = it;
        paths = pa;
        mIcon2 = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_folder);
        mIcon3 = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_fomat_unkown);
        initSelected();
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                count++;
            }
        }
        return count;
    }

    public String[] getSelectedPath() {
        String[] strs = null;
        List<String> pathArray = new ArrayList<String>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                pathArray.add(paths.get(i));
            }
        }
        if (pathArray.size() > 0) {
            strs = pathArray.toArray(new String[pathArray.size()]);
        }
        return strs;
    }

    private void initSelected() {
        for (int i = 0; i < items.size(); i++) {
            selected.put(i, false);
        }
    }

    public void setSelected(int position) {
        selected.put(position, !selected.get(position));
        notifyDataSetChanged();
    }

    public void setSingleSelected(int position) {
        boolean flag = selected.get(position);
        for (int i = 0; i < selected.size(); i++) {
            selected.put(i, false);
        }
        selected.put(position, !flag);
        notifyDataSetChanged();
    }

    public void setSelectedAll(boolean isAll) {
        if (isAll) {
            for (int i = 0; i < selected.size(); i++) {
                File file = new File(paths.get(i));
                if (file.isFile() && fileType == TYPE_FILE) {
                    selected.put(i, true);
                } else if (file.isDirectory() && fileType == TYPE_DIRECTORY) {
                    selected.put(i, true);
                }
            }
        } else {
            for (int i = 0; i < selected.size(); i++) {
                selected.put(i, false);
            }
        }
        notifyDataSetChanged();

    }

    public boolean isSelected() {
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("static-access")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_file_row, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            holder.checkLl = (LinearLayout) convertView.findViewById(R.id.checkLl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(paths.get(position));
        if (fileType == TYPE_FILE) {
            if (f.isDirectory()) {
                holder.checkLl.setVisibility(View.GONE);
            } else {
                holder.checkLl.setVisibility(View.VISIBLE);
            }
        } else {
            holder.checkLl.setVisibility(View.VISIBLE);
        }

        try {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.icon.setImageBitmap(mIcon2);
            } else {
                if (f.isFile() & f.getName().endsWith(".png")) {
                    holder.icon.setImageBitmap(Utils.getImageThumbnail(paths.get(position).toString(), 96, 96));
                } else {
                    holder.icon.setImageBitmap(mIcon3);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (selected.get(position)) {
            holder.iv_check.setImageResource(R.mipmap.ic_checkbox_c);
        } else {
            holder.iv_check.setImageResource(R.mipmap.ic_checkbox_n);
        }

        holder.checkLl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (selectType == MULTI_SELECT) {
                    setSelected(position);
                } else {
                    setSingleSelected(position);
                }
            }
        });
        holder.text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                File file = new File(paths.get(position));
                if (selectType == activity.SINGLE_SELECT) {
                    if (getSelectedCount() > 0) {
                        if (fileType == TYPE_FILE) {
                            if (file.isDirectory()) {
                                setSelectedAll(false);
                                activity.getFileDir(paths.get(position), false);
                            } else {
                                setSingleSelected(position);
                            }
                        } else {
                            setSingleSelected(position);
                        }
                    } else {

                        if (file.isDirectory()) {
                            activity.currentPath = paths.get(position);
                            if ((activity.currentPath + "/").equals(activity.rootPath)) {
                                activity.getFileDir(paths.get(position), true);
                            } else {
                                activity.getFileDir(paths.get(position), false);
                            }
                        } else {
                            setSingleSelected(position);
                        }
                    }

                } else {
                    if (getSelectedCount() > 0) {
                        if (fileType == TYPE_FILE) {
                            if (file.isDirectory()) {
                                return;
                            } else {
                                setSelected(position);
                            }
                        } else {
                            setSelected(position);
                        }

                    } else {
                        try {
                            if (file.isDirectory()) {
                                activity.currentPath = paths.get(position);
                                if ((activity.currentPath + "/").equals(activity.rootPath)) {
                                    activity.getFileDir(paths.get(position), true);
                                } else {
                                    activity.getFileDir(paths.get(position), false);
                                }
                            } else {
                                setSelected(position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        LinearLayout checkLl;
        ImageView iv_check;
        TextView text;
        ImageView icon;
    }
}