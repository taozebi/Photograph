package com.ewide.photograph.app.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ewide.photograph.R;
import com.ewide.photograph.app.event.EventCenter;
import com.ewide.photograph.app.event.EventCode;
import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.app.project.model.ProjectCallback;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.app.project.presenter.ProjectPresenter;
import com.ewide.photograph.common.base.CommonActivity;
import com.ewide.photograph.common.filebrowsing.FileReadWindows;
import com.ewide.photograph.common.util.ProgressDialogUtil;
import com.ewide.photograph.common.util.T;
import com.ewide.photograph.common.util.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Desc: 新建项目
 * Created by taoze
 * on 2019/11/17
 **/
public class NewProjectActivity extends CommonActivity implements ProjectCallback {

    private static final int REQUEST_CODE_FILE_PATH = 1001;

    private static final int ON_NEW_PROJECT = 1002;

    @BindView(R.id.mProjectNameEt)
    public EditText mProjectNameEt;

    @BindView(R.id.mNewProjectPathTv)
    public TextView mNewProjectPathTv;

    private ProjectPresenter mProjectPresenter;

    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        if(msg.what == ON_NEW_PROJECT){
            ProgressDialogUtil.dismiss();
            showSuccessDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_new);
    }

    @Override
    public void onInitData() {
        titleBar.setTitleText("新建项目");
        titleBar.setBackBtnVisibility(true);
        String time = Utils.getDateStr("yyyyMMdd_HHmmss");
        time = "Pro_"+ time;
        mProjectNameEt.setText(time);
        mProjectPresenter = new ProjectPresenter(this);
    }

    @OnClick({R.id.mNewProjectPathLl,R.id.mNewProjectBtn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mNewProjectPathLl:
                String path = mNewProjectPathTv.getText().toString();
                Intent intent = new Intent(this, FileReadWindows.class);
                intent.putExtra(FileReadWindows.TYPE_OF_FILE,FileReadWindows.TYPE_DIRECTORY);
                intent.putExtra(FileReadWindows.TYPE_OF_SELECT,FileReadWindows.SINGLE_SELECT);
                if(!TextUtils.isEmpty(path)){
                    intent.putExtra(FileReadWindows.FILE_PATH, path);
                }
                startActivityForResult(intent,REQUEST_CODE_FILE_PATH);
                break;
            case R.id.mNewProjectBtn:
                String projectPath = mNewProjectPathTv.getText().toString();
                String projectName = mProjectNameEt.getText().toString();
                Project project = new Project();
                project.setProjectName(projectName);
                project.setProjectPath(projectPath);
                ProgressDialogUtil.show(NewProjectActivity.this,"",getString(R.string.loading));
                mProjectPresenter.createNewProject(project,this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE_FILE_PATH == requestCode && resultCode == RESULT_OK){

            if(data!=null){
                String[] filePath = data.getExtras().getStringArray(FileReadWindows.FILE_PATH);
                if(filePath.length > 0){
                    mNewProjectPathTv.setText(filePath[0]);
                }
            }
        }
    }

    private void showSuccessDialog(){
        MRApplication.getInstance().showDialog(this, "提示", "项目创建成功!", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResult(RESULT_OK);
                EventBus.getDefault().post(new EventCenter(EventCode.CODE_0));
                finish();
            }
        });
    }

    @Override
    public void onNewProjectSuccess() {
        sendMessage(ON_NEW_PROJECT);
    }

    @Override
    public void onFailure(String errMsg) {
        sendToastMessage(errMsg);
    }
}
