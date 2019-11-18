package com.ewide.photograph.app.project.model;

import android.content.Context;

import com.ewide.photograph.app.db.MeterDatabase;
import com.ewide.photograph.app.db.dao.ProjectDao;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.common.base.BaseModel;

/**
 * Desc:
 * Created by taoze
 * on 2019/11/17
 **/
public class ProjectModel extends BaseModel {

    private ProjectDao projectDao;

    public ProjectModel(Context context) {
        super(context);
        projectDao = MeterDatabase.getInstance(mContext).getProjectDao();
    }

    public void createNewProject(Project project, ProjectCallback callback){
        try{
            projectDao.insert(project);
            callback.onNewProjectSuccess();
        }catch (Exception e){
            callback.onFailure("±£¥Ê ß∞‹");
        }
    }
}
