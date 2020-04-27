package com.android.uoso.week12.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.uoso.week12.App;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 存储工具类
 */
public class CookieUtil {
    private static final String SP_NAME = "my_sp";
    /**
     * 存储
     */
    public static void put(String key,Object object){
        SharedPreferences sp = App.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (object instanceof String){
            edit.putString(key,(String)object);
        }else if(object instanceof Integer){
            edit.putInt(key, (Integer) object);
        }else if(object instanceof Float){
            edit.putFloat(key, (Float) object);
        }else if(object instanceof Long){
            edit.putLong(key, (Long) object);
        }else if(object instanceof Boolean){
            edit.putBoolean(key, (Boolean) object);
        }
        edit.commit();
    }

    /**
     * 读取
     * @param key 数据key
     * @param defaultValue 默认值
     */
    public static Object get(String key,Object defaultValue){
        SharedPreferences sp = App.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (defaultValue instanceof  String){
            return sp.getString(key, (String) defaultValue);
        }else if (defaultValue instanceof  Integer){
            return sp.getInt(key, (Integer) defaultValue);
        }else if (defaultValue instanceof  Long){
            return sp.getLong(key, (Long) defaultValue);
        }else if (defaultValue instanceof  Float){
            return sp.getFloat(key, (Float) defaultValue);
        }else if (defaultValue instanceof  Boolean){
            return sp.getBoolean(key, (Boolean) defaultValue);
        }
        return null;
    }

    /**
     * 清除缓存
     */
    public static void clear(){
        SharedPreferences sp = App.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 保存对象
     */
    public static  boolean saveObject(Object object){
        FileOutputStream fos = null;
        ObjectOutputStream objStream = null;
        try {
            //开启文件输出流                       把对象的类型名称作为文件名
           fos = App.getAppContext().openFileOutput(object.getClass().getName(), Context.MODE_PRIVATE);
           //开启对象输出流
           objStream = new ObjectOutputStream(fos);
           //写入对象
           objStream.writeObject(object);
            //返回成功
           return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();//关闭流
                objStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 读取对象
     */
    public static <T>T getObject(Class<T> clazz){
        FileInputStream fis = null;
        ObjectInputStream objStream = null;
        try {
            //开启文件输入流
            fis = App.getAppContext().openFileInput(clazz.getName());
            //开启对象输入流
            objStream = new ObjectInputStream(fis);
            //读取对象
            Object obj = objStream.readObject();
            //返回对象
            return (T) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();//关闭流
                objStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
