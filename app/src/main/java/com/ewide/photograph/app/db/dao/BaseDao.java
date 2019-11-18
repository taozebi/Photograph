package com.ewide.photograph.app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * author：Taozebi
 * date：2018/11/13 17:49
 * describe：Dao 基类
 */

@Dao
public interface BaseDao<T> {

    @Insert
    void insert(T... list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T obj);

    @Insert
    void insert(List<T> list);

    @Delete
    public void delete(T obj);

    @Delete
    public void delete(List<T> list);

    @Delete
    public void delete(T...list);

    @Update
    public void update(T obj);

    @Update
    public void update(List<T> list);
}
