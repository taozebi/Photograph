package com.ewide.photograph.app.main.model;

import android.content.Context;

import com.ewide.photograph.app.db.MeterDatabase;
import com.ewide.photograph.app.db.dao.ProjectDao;
import com.ewide.photograph.app.global.ThreadManager;
import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.common.base.BaseModel;

import java.io.File;
import java.util.List;

/**
 * Desc:
 * Created by taoze
 * on 2019/11/17
 **/
public class MainModel extends BaseModel {

    private ProjectDao projectDao;

    public MainModel(Context context) {
        super(context);
        projectDao = MeterDatabase.getInstance(context).getProjectDao();
    }

    public void getProjectListData(MainCallback callback){
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<Project>  projects = projectDao.getAllProjects();
                if(projects != null && projects.size()>0){
                    for (int i = 0; i < projects.size(); i++) {
                        Project pro = projects.get(i);
                        File file = new File(pro.getProjectPath());
                        if(file.exists() && file.isDirectory()){
                            File[] files = file.listFiles();
                            if(files != null){
                                pro.setFileCount(files.length);
                            }
                        }
                    }
                }
                callback.onGetProjectListSuccess(projects);
            }
        });

    }
}
