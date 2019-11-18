package com.ewide.photograph.common.filebrowsing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ewide.photograph.R;
import com.ewide.photograph.common.base.CommonActivity;
import com.ewide.photograph.common.util.T;
import com.ewide.photograph.common.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件选择
 *
 * @author wkkyo
 */
public class FileReadWindows extends CommonActivity {

    public static final String FILE_PATH = "file_path";
    public static final String TYPE_OF_FILE = "type_of_file";
    public static final String TYPE_OF_SELECT = "type_of_select";
    public static final int TYPE_DIRECTORY = 0;
    public static final int TYPE_FILE = 1;
    public static int TYPE = TYPE_FILE;
    public static final int SINGLE_SELECT = 2;
    public static final int MULTI_SELECT = 3;
    public static int TYPE_SELECT = SINGLE_SELECT;
    private String[] fileFilters;
    private String[] pathArray = null;

    @BindView(R.id.list)
    ListView list;

    @BindView(R.id.pathView)
    PathView pathView;

    @BindView(R.id.checkAllLl)
    LinearLayout checkAllLl;
    @BindView(R.id.checkAllIv)
    ImageView checkAllIv;
    @BindView(R.id.checkAllTv)
    TextView checkAllTv;
    @BindView(R.id.mPathLl)
    LinearLayout mPathLl;

    private List<String> items = null;
    private List<String> paths = null;
    public String rootPath = "/";
    public String currentPath = "/";

    /**
     * 父目录
     */
    private String parentPath = "";
    /**
     * 传递过来的目录
     */
    private String filePath = "";

    private BaseAdapter FileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActionView(R.layout.layout_file_read_menu);
        setContentView(R.layout.activity_window_fileread);
        titleBar.setSubmitBtnTextColor(R.color.white);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        TYPE = intent.getIntExtra(TYPE_OF_FILE, TYPE_FILE);
        if (intent.getExtras().containsKey(TYPE_OF_SELECT)) {
            TYPE_SELECT = intent.getIntExtra(TYPE_OF_SELECT, SINGLE_SELECT);
        }
        if (TYPE_SELECT == MULTI_SELECT) {
            checkAllLl.setVisibility(View.VISIBLE);
        } else {
            checkAllLl.setVisibility(View.GONE);
        }
        if (intent.getExtras().containsKey(FILE_PATH)) {
            filePath = intent.getStringExtra(FILE_PATH);
        }
        fileFilters = intent.getStringArrayExtra("fileFilters");

        titleBar.setTitleText(getString(R.string.file_path));
        titleBar.setBackBtnVisibility(true);
        titleBar.setSubmitBtnVisibility(true);

        rootPath = Utils.getSDPath();

        pathView.setOnPathItemClickListener(new PathView.OnPathItemClickListener() {

            @Override
            public void onItemViewClick(ItemView view) {
                String path = view.getPath();
                if (rootPath.equals(path)) {
                    getFileDir(path, true);
                } else {
                    getFileDir(path, false);
                }
            }
        });

    }

    @OnClick(R.id.checkAllLl)
    void checkAll(View v) {
        if ("全选".equals(checkAllTv.getText().toString())) {
            checkAllIv.setImageResource(R.mipmap.ic_checkbox_c);
            checkAllTv.setText("取消");
            ((FileReadAdapter) list.getAdapter()).setSelectedAll(true);
        } else {
            checkAllIv.setImageResource(R.mipmap.ic_checkbox_n);
            checkAllTv.setText("全选");
            ((FileReadAdapter) list.getAdapter()).setSelectedAll(false);
        }
    }

    @OnClick(R.id.mPathLl)
    void toParentBack(View v) {
        File file = new File(parentPath);
        if (file.isDirectory()) {
            if ((parentPath + "/").equals(rootPath)) {
                getFileDir(parentPath, true);
            } else {
                getFileDir(parentPath, false);
            }
        }
    }

    @Override
    protected void onSubmit() {
        Intent data = new Intent();
        if (((FileReadAdapter) list.getAdapter()).getSelectedCount() > 0) {
            pathArray = ((FileReadAdapter) list.getAdapter()).getSelectedPath();
            data.putExtra(FILE_PATH, pathArray);
            setResult(RESULT_OK, data);
            finish();
        } else {
            T.showShort(this,"请先勾选之后再确定");
        }
    }

    @Override
    protected boolean onBack() {
        finish();
        return super.onBack();
    }

    @Override
    public void onInitData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(filePath)) {
            getFileDir(rootPath, true);
        } else {
            if ((filePath + "/").equals(rootPath)) {
                getFileDir(rootPath, true);
            } else {
                getFileDir(filePath, false);
            }
        }

    }

    public void getFileDir(String filePath, boolean isRoot) {
        pathView.init(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File f = new File(filePath);
        if (!f.exists()) {
            T.showShort(this,"当前目录不存在,跳转至根目录");
            pathView.init(rootPath);
            f = new File(rootPath);
            isRoot = true;
        }
        File[] files = f.listFiles();
        files = FileSort.sortFile(files);
        if (!isRoot) {
            parentPath = f.getParent();
            mPathLl.setVisibility(View.VISIBLE);
        } else {
            mPathLl.setVisibility(View.GONE);
            parentPath = rootPath;
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (TYPE == TYPE_FILE) {
                    File file = files[i];
                    if (file.isDirectory()) {
                        items.add(file.getName());
                        paths.add(file.getPath());
                    } else if (fileFilters != null && fileFilters.length > 0) {
                        for (String extName : fileFilters) {
                            if (file.getName().endsWith(extName)) {
                                items.add(file.getName());
                                paths.add(file.getPath());
                                break;
                            }
                        }
                    } else {
                        items.add(file.getName());
                        paths.add(file.getPath());
                    }
                } else if (TYPE == TYPE_DIRECTORY) {
                    File file = files[i];
                    if (file.isDirectory()) {
                        items.add(file.getName());
                        paths.add(file.getPath());
                    }
                }
            }
        }
        FileAdapter = new FileReadAdapter(this, items, paths, TYPE, TYPE_SELECT);
        list.setAdapter(FileAdapter);

    }
}
