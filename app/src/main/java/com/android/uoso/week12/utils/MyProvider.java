package com.android.uoso.week12.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 自定义ContentProvider类
 */
public class MyProvider extends ContentProvider{

    private SQLiteDatabase db;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);//Uri的匹配类
    private static final String AUTH = "com.android.uoso.week12.utils.MyProvider";//授权信息
    private static final int CODE_PERSON=1001; //人物表的匹配码

    //静态代码块，当类加载的时候就会执行一次，不存在任何方法体中，不能访问普通变量，一个类可以写多个，按照先后顺序执行
    static {
        //用于向UriMatcher对象注册uri
        matcher.addURI(AUTH,MySQLiteHelper.TB_PERSON,CODE_PERSON);
    }

    /**
     * 创建会调用此方法，一般做初始化操作
     * @return
     */
    @Override
    public boolean onCreate() {
        MySQLiteHelper helper = new MySQLiteHelper(getContext(), MySQLiteHelper.DB_NAME, null, 1);
        db = helper.getReadableDatabase();
        return false;
    }

    /**
     * 查询
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableNameByUri(uri);
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }
    //通过uri匹配对应的表，获取表名
    private String getTableNameByUri(Uri uri){
        int code = matcher.match(uri);
        switch (code){
            case CODE_PERSON:
                return MySQLiteHelper.TB_PERSON;
        }
        return null;
    }

    /**
     * 用来返回一个Uri请求对应的MIME类型
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * 插入数据
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = db.insert(getTableNameByUri(uri), null, values);
        if (rowId!=-1){
            //通知刷新
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return uri;
    }

    /**
     * 删除
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int number = db.delete(getTableNameByUri(uri), selection, selectionArgs);
        if (number!=0){
            //通知刷新
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return number;
    }

    /**
     * 修改
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int number = db.update(getTableNameByUri(uri), values, selection, selectionArgs);
        if (number!=0){
            //通知刷新
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return number;
    }
}
