package com.android.uoso.week12.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.uoso.week12.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 网络下载工具类
 */
public class HttpUtil {
    //回调函数
    public interface CallBack{
        void onProgress(int progress);
        void onFinish(byte[] result);
        void onError(String errMsg);
    }
    private static CallBack mCallback;
    //下载
    public static void download(final String path,CallBack callBack){
        if(TextUtils.isEmpty(path)){
            return;
        }
        mCallback = callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //网络请求
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        int totalLength = connection.getContentLength();//文件总大小
                        int curLength = 0;//当前已下载文件大小
                        int curProgress = 0;//当前进度

                        is = connection.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len =is.read(bytes))!=-1){
                            baos.write(bytes,0,len);
                            curLength += len;
                            curProgress  = (int)(((double)curLength/(double) totalLength)*100);
                            mCallback.onProgress(curProgress);
                        }
                        byte[] array = baos.toByteArray();
                        writeSD(array,path);

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        is.close();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    /**
     * 写入SD卡
     * @param array
     */
    public static void writeSD(byte[] array,String path) {
        //只有状态等于Mounted时，SD卡才可以读写
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            mCallback.onError("请检查SD卡是否可用");
            return;
        }

        //获取需要创建的文件夹的绝对路径
        //getExternalStorageDirectory().getAbsolutePath()   获取SD卡根目录的绝对路径
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AAA/BBB";
        //new一个文件夹的对象
        File dir = new File(dirPath);
        if(!dir.exists()){//如果文件夹不存在，则创建
            // dir.mkdir();创建指定文件夹
            dir.mkdirs();//创建指定文件夹，包含未存在的父文件夹
        }
        //创建待写入的文件对象
        //获取文件名
        String fileName = "";
        if(path.contains("/")){
            String[] split = path.split("/");//通过斜线分割路径，取最后一个作为文件名
            fileName =  split[split.length-1];
        }else {
            fileName = path;
        }
        File file = new File(dir, fileName);
        FileOutputStream fos = null;
        try {
            //开启文件输出流
            fos = new FileOutputStream(file);
            fos.write(array);
            mCallback.onFinish(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
