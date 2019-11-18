package com.ewide.photograph.app.main.model;

import com.ewide.photograph.app.project.model.entry.Project;
import com.ewide.photograph.common.base.BaseFailureCallback;

import java.util.List;

/**
 * Created by Taoze on 2018/9/12.
 */

public interface MainCallback extends BaseFailureCallback {

    public void onGetProjectListSuccess(List<Project> projectList);

}
