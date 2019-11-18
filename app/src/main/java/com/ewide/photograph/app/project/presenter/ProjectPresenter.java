package com.ewide.photograph.app.project.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.ewide.photograph.app.project.model.ProjectCallback;
import com.ewide.photograph.app.project.model.ProjectModel;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.common.base.BasePresenter;
import com.ewide.photograph.common.util.Utils;

import okhttp3.internal.Util;

/**
 * Desc:
 * Created by taoze
 * on 2019/11/17
 **/
public class ProjectPresenter extends BasePresenter {

    private ProjectModel projectModel;

    public ProjectPresenter(Context context) {
        super(context);
        projectModel = new ProjectModel(context);
    }

    public void createNewProject(Project project, ProjectCallback callback){
        if(TextUtils.isEmpty(project.getProjectName())){
            callback.onFailure("����д��Ŀ����");
            return;
        }
        if(TextUtils.isEmpty(project.getProjectName())){
            callback.onFailure("��ѡ����Ŀ·��");
            return;
        }
        String time = Utils.getDateStr("yyyy-MM-dd HH:mm:ss");
        project.setCreateTime(time);
        projectModel.createNewProject(project,callback);
    }
}
