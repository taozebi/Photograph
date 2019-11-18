package com.ewide.photograph.app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ewide.photograph.app.project.model.entry.Project;

import java.util.List;

/**
 * author：Taozebi
 * date：2018/11/13 19:54
 * describe：数据字典dao
 */
@Dao
public interface ProjectDao extends BaseDao<Project>{

    @Query("select * from Project order by createTime desc")
    public List<Project> getAllProjects();

}
