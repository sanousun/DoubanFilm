package com.shenhui.doubanfilm.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.shenhui.doubanfilm.bean.SubjectBean;

import java.util.List;

/**
 * @author dashu
 * @date 2018/2/25
 * 操作subject的DAO
 */

@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subject")
    List<SubjectBean> getAll();

    @Query("SELECT * FROM subject WHERE id = :id LIMIT 1")
    SubjectBean findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubjectBean subjectBean);

    @Delete()
    void delete(SubjectBean subjectBean);
}
