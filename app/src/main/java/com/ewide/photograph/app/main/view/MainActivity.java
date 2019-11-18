package com.ewide.photograph.app.main.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.ewide.photograph.R;
import com.ewide.photograph.app.adapter.ProjectListAdapter;
import com.ewide.photograph.app.event.EventCenter;
import com.ewide.photograph.app.event.EventCode;
import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.app.main.model.MainCallback;
import com.ewide.photograph.app.main.presenter.MainPresenter;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.app.project.view.NewProjectActivity;
import com.ewide.photograph.app.project.view.ProjectActivity;
import com.ewide.photograph.common.base.CommonActivity;
import com.ewide.photograph.common.util.ProgressDialogUtil;
import com.ewide.photograph.common.util.T;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Taoze on 2018/3/23.
 */
public class MainActivity extends CommonActivity implements MainCallback, XRecyclerView.LoadingListener{
    private static final int GET_BOF_SUCCESS = 100;
    private static final int REQUEST_PERMISSIONS = 3;
    private static final int REQUEST_NEW_PROJECT = 400;
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @BindView(R.id.mBofRv)
    public XRecyclerView mBofRv;
    @BindView(R.id.mBofEmptyView)
    public LinearLayout mBofEmptyView;

    private ProjectListAdapter mAdapter;
    private List<Project> mDatas;


    private MainPresenter mMainPresenter;

    @Override
    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
            case GET_BOF_SUCCESS:
                List<Project> bofs = (List<Project>) msg.obj;
                ProgressDialogUtil.dismiss();
                mBofRv.refreshComplete();
                mBofRv.loadMoreComplete();
                if (bofs != null) {
                    mDatas = bofs;
                    refreshListDatas();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        //注册
        EventBus.getDefault().register(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

    }


    @Override
    public void onInitData() {
        titleBar.setTitleText(R.string.app_name);
        titleBar.setBackBtnVisibility(false);
        titleBar.setRightBtnVisibility(true);
        titleBar.getRightBtn().setImageResource(R.drawable.btn_add_style);
        titleBar.setOnRightBtnClickListener(rightClick);
        mDatas = new ArrayList<>();
        refreshListDatas();
        mMainPresenter = new MainPresenter(this);
        getData();
    }

    private void refreshListDatas() {
        if (mDatas == null || mDatas.size() == 0) {
            mBofRv.setVisibility(View.GONE);
            mBofEmptyView.setVisibility(View.VISIBLE);
        } else {
            mBofEmptyView.setVisibility(View.GONE);
            mBofRv.setVisibility(View.VISIBLE);
            //创建LinearLayoutManager 对象
            LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
            layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
            //设置RecyclerView 布局
            mBofRv.setLayoutManager(layoutmanager);
            //设置Adapter
            mAdapter = new ProjectListAdapter(this, mDatas);

            mAdapter.setItemClickListener(new ProjectListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Project bof = (Project) mAdapter.getItem(postion);
                    Intent intent = new Intent(MainActivity.this,ProjectActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data",bof);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


            mAdapter.setItemLongClickListener(new ProjectListAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int postion) {

                }
            });

            mBofRv.setAdapter(mAdapter);
            mBofRv.setLoadingMoreEnabled(false);
            mBofRv.setLoadingListener(this);
        }
    }

    private View.OnClickListener rightClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,NewProjectActivity.class);
//            startActivityForResult(intent, REQUEST_NEW_PROJECT);
            startActivity(intent);
        }
    };

    private void checkPermission() {
        //申请定位权限申请相机权限
        if (ActivityCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED
                ) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
            // }
        } else {
        }
    }

    private void getData(){
        ProgressDialogUtil.show(this,"","加载中...");
        mMainPresenter.getProjectListData(this);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(REQUEST_NEW_PROJECT == requestCode && resultCode == RESULT_OK){
//            getData();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                T.showLong(this, "请打开相关权限才能正常使用APP");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //订阅抄表界面发送消息，刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowNoticeEvent(Object event) {
        if (event instanceof EventCenter) {
            EventCenter eventCenter = (EventCenter) event;
            if (EventCode.CODE_0 == eventCenter.getEventCode()) {
                getData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        ((MRApplication) getApplication()).toExit(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册 , 防止Activity内存泄漏
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetProjectListSuccess(List<Project> projectList) {
        sendMessage(GET_BOF_SUCCESS, projectList);
    }

    @Override
    public void onFailure(String errMsg) {
        sendToastMessage(errMsg);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {

    }
}
