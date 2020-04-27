package com.android.uoso.week12.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/** 下载图片的异步任务
 * Params 传递参数的数据类型
 * Progress 进度的数据类型
 * Result  返回结果的数据类型
 */
public class MyAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private Context context;
    private ImageView imageView;
    private InputStream is;
    private ByteArrayOutputStream baos;
    private final ProgressDialog dialog;

    public MyAsyncTask(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        //进度加载对话框
        dialog = new ProgressDialog(context);
        dialog.setMax(100);
        dialog.setTitle("下载图片");
        dialog.setMessage("正在下载....");
        dialog.show();
    }

    /**
     * 做准备工作，在主线程执行，在doInBackground（）之前执行
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 在子线程中执行，耗时操作就写在此方法
     * @param strings
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlPath = strings[0]; //获取请求地址
        try {
            URL url = new URL(urlPath);
            //打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//设置请求方式
            connection.connect();//连接
            //当响应码等于200表示连接成功
            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                int currentProgress = 0; //当前进度
                int imageLength = connection.getContentLength();//图片总长度
                int currentLength = 0; //当前已经读取的字节长度
                //获取输入流
                is = connection.getInputStream();
                /*//使用图片工厂将输入流转换为图片
                Bitmap bitmap = BitmapFactory.decodeStream(is);*/
                //创建输出流用于接受数据
                baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];//一般为128 256 1024
                int len ;
                while ((len = is.read(bytes))!=-1){//当len等于-1表示已经读完
                    baos.write(bytes,0,len);
                    //计算当前进度
                    currentLength += len;
                    currentProgress =  (int)((double)currentLength/imageLength *100);//已读长度除以总长度
                    publishProgress(currentProgress);//发布进度
                }
                byte[] array = baos.toByteArray();//输出流转换为字节数组
                //String s = new String(array,0,array.length);
                //字节数组转图片
                Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                return bitmap;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                is.close();//关闭输入流
                baos.close();//关闭输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 返回结果，在主线程执行，在doInBackground（）之后执行
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        dialog.dismiss();
        if (bitmap!=null){
            //下载成功
            imageView.setImageBitmap(bitmap);
        }else {
            //下载失败
            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新进度，在主线程中执行
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        dialog.setProgress(progress);
    }
}
