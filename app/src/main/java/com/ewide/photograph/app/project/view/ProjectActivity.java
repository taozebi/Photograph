package com.ewide.photograph.app.project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.ewide.photograph.R;
import com.ewide.photograph.app.event.EventCenter;
import com.ewide.photograph.app.event.EventCode;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.common.base.CommonActivity;
import com.ewide.photograph.common.view.MediaGridView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;

/**
 * Desc:
 * Created by taoze
 * on 2019/11/17
 **/
public class ProjectActivity extends CommonActivity{

    private static final int REFRESH_IMAGE = 200;

    @BindView(R.id.mProjectCountTv)
    public TextView mProjectCountTv;

    @BindView(R.id.mMgv)
    public MediaGridView mMgv;

    private Project mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
    }

    @Override
    public void onInitData() {
        titleBar.setBackBtnVisibility(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("data")) {
            mProject = (Project) bundle.get("data");
        }
        if(mProject != null){
            titleBar.setTitleText(mProject.getProjectName());
            File file = new File(mProject.getProjectPath());
            if (!file.exists()) {
                file.mkdirs();
            }
            mMgv.setScanDir(file, this, true, 4);
        }

        sendMessage(REFRESH_IMAGE);

    }

    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        switch (msg.what) {
            case REFRESH_IMAGE:
                if (mMgv != null) {
                    mMgv.refresh(false);
                    if(mProject != null){
                        String path = mProject.getProjectPath();
                        File file = new File(path);
                        if(file.exists() && file.isDirectory()){
                            File[] files = file.listFiles();
                            if(files != null){
                                mProjectCountTv.setText(files.length+"");;
                            }
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
//                //在手机相册中显示刚拍摄的图片
//                if(!TextUtils.isEmpty(mMgv.getCachePath())){
//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri contentUri = Uri.fromFile(new File(mMgv.getCachePath()));
//                    mediaScanIntent.setData(contentUri);
//                    getActivity().sendBroadcast(mediaScanIntent);
//                }
                sendMessage(REFRESH_IMAGE);
//                mMgv.refresh(false);
                EventBus.getDefault().post(new EventCenter(EventCode.CODE_0));
            }
        }
    }
}
