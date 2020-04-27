package com.android.uoso.week12.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite数据库帮助类
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "my_database";//数据库名称
    public static final String TB_PERSON="tb_person";//人物表的名称
    /**
     * @param context 上下文
     * @param name   数据库名称
     * @param factory 游标工厂
     * @param version 版本号（只能增加，不能减少）
     */
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 数据库第一次创建会调用此方法，一般在此创建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行SQ语句，创建一张表
        db.execSQL("create table "+TB_PERSON+" (id integer primary key autoincrement,name varchar(64),age integer)");
    }

    /**
     * 数据库版本升级的时候调用此方法，一般在此删除旧表，创建新表
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TB_PERSON);
        onCreate(db);
    }
}
