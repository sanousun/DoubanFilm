package com.shenhui.doubanfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.bean.SubjectBean;
import com.shenhui.doubanfilm.support.Constant;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class FilmDataSource {

    private SQLiteDatabase mDatabase;
    private DBHelper mHelper;
    private String[] allColumnsForCol = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_FILM,
            DBHelper.COLUMN_CONTENT
    };
    private String[] allColumnsForTop = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TOP,
            DBHelper.COLUMN_CONTENT
    };

    /**
     * 创建DBHelper通过open()得到数据库
     */
    public FilmDataSource(Context context) {
        mHelper = DBHelper.getInstance(context);
    }

    /**
     * 从SQLiteHelper实例得到读写数据库
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        mDatabase = mHelper.getWritableDatabase();
    }

    //------------------------操作收藏电影-------------------------------

    /**
     * 插入filmId对应的film
     */
    private void insertFilm(String id, String content) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_FILM, id);
        values.put(DBHelper.COLUMN_CONTENT, content);
        mDatabase.insert(DBHelper.TABLE_NAME_COL, null, values);
    }

    /**
     * 更新filmId对应的film
     */
    private void upDataFilm(String id, String content) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_FILM, id);
        values.put(DBHelper.COLUMN_CONTENT, content);
        mDatabase.update(DBHelper.TABLE_NAME_COL, values, DBHelper.COLUMN_FILM + " = " + id, null);
    }

    /**
     * 通过filmId的到对应的film
     */
    public SubjectBean filmOfId(String id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME_COL, allColumnsForCol,
                DBHelper.COLUMN_FILM + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        SubjectBean sub = cursorToSubject(cursor);
        cursor.close();
        return sub;
    }

    /**
     * 从数据库查询得到的游标中得到film
     */
    public SubjectBean cursorToSubject(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            return new Gson().fromJson(content, Constant.subType);
        }
        return null;
    }

    /**
     * 如果filmId对应的film存在，更新数据；
     * 否则，插入数据
     */
    public void insertOrUpDataFilm(String id, String content) {
        if (filmOfId(id) == null) {
            insertFilm(id, content);
        } else {
            upDataFilm(id, content);
        }
    }

    /**
     * 得到所有的collectTable中的电影
     */
    public List<SubjectBean> getFilmForCollected() {
        Cursor cursor = mDatabase.query(
                DBHelper.TABLE_NAME_COL, allColumnsForCol, null, null, null, null, null);
        cursor.moveToFirst();
        List<SubjectBean> res = new ArrayList<>();
        do {
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            SubjectBean sub = new Gson().fromJson(content, Constant.subType);
            res.add(sub);
        } while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    /**
     * 删除filmId对应的film数据
     */
    public void deleteFilm(String id) {
        mDatabase.delete(DBHelper.TABLE_NAME_COL, DBHelper.COLUMN_FILM + " = " + id, null);
    }

    //------------------------操作收藏电影-------------------------------

    //------------------------操作top250数据-----------------------------

    /**
     * 插入top排名对应的数据内容
     */
    public List<SimpleSubjectBean> insertTop(String top, String content) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TOP, top);
        values.put(DBHelper.COLUMN_CONTENT, content);
        long insertId = mDatabase.insert(DBHelper.TABLE_NAME_TOP, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME_TOP, allColumnsForTop,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        List<SimpleSubjectBean> data = CursorToList(cursor);
        cursor.close();
        return data;
    }

    /**
     * 更新top排名对应的数据内容
     */
    public List<SimpleSubjectBean> upDateTop(String top, String content) {
        ContentValues values = new ContentValues();
        values.put(
                DBHelper.COLUMN_TOP,
                top);
        values.put(
                DBHelper.COLUMN_CONTENT,
                content);
        mDatabase.update(
                DBHelper.TABLE_NAME_TOP,
                values,
                DBHelper.COLUMN_TOP + " = " + top,
                null);
        Cursor cursor = mDatabase.query(
                DBHelper.TABLE_NAME_TOP,
                allColumnsForTop,
                DBHelper.COLUMN_TOP + " = " + top,
                null, null, null, null);
        cursor.moveToFirst();
        List<SimpleSubjectBean> data = CursorToList(cursor);
        cursor.close();
        return data;
    }

    /**
     * 查询top排名对应的数据内容
     */
    public List<SimpleSubjectBean> getTop(String top) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME_TOP, allColumnsForTop,
                DBHelper.COLUMN_TOP + " = " + top, null, null, null, null);
        cursor.moveToFirst();
        List<SimpleSubjectBean> data = CursorToList(cursor);
        cursor.close();
        return data;
    }

    public List<SimpleSubjectBean> insertOrUpDateTop(String top, String content) {
        if (getTop(top) == null) {
            return insertTop(top, content);
        } else {
            return upDateTop(top, content);
        }
    }

    private List<SimpleSubjectBean> CursorToList(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            return new Gson().fromJson(content, Constant.simpleSubTypeList);
        }
        return null;
    }

    //------------------------操作top250数据-----------------------------
}
