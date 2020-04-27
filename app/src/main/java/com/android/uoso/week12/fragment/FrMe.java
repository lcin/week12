package com.android.uoso.week12.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.uoso.week12.R;
import com.android.uoso.week12.activity.ActLogin;
import com.android.uoso.week12.model.User;
import com.android.uoso.week12.utils.CookieUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class FrMe extends SupportFragment {
    @BindView(R.id.tv_username)
    TextView tvUsername;
    Unbinder unbinder;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_write)
    Button btnWrite;
    @BindView(R.id.btn_read)
    Button btnRead;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.btn_write_obj)
    Button btnWriteObj;
    @BindView(R.id.btn_read_obj)
    Button btnReadObj;
    @BindView(R.id.btn_write_sd)
    Button btnWriteSd;
    @BindView(R.id.btn_read_sd)
    Button btnReadSd;

    public static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                 Manifest.permission.CAMERA   };//需要申请的权限
    public static final int REQUEST_CODE = 101;//请求码
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_me, null);
        unbinder = ButterKnife.bind(this, view);
        getUsername();
        return view;
    }

    /**
     * 获取用户名、密码
     */
    private void getUsername() {
        String username = (String) CookieUtil.get("username", "");
        String password = (String) CookieUtil.get("password", "");
        tvUsername.setText(username);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_logout, R.id.btn_write, R.id.btn_read, R.id.btn_write_obj, R.id.btn_read_obj,R.id.btn_read_sd,R.id.btn_write_sd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_logout://退出登录
                /* //登录状态设置为false
        CookieUtil.put("isLogin",false);*/
                CookieUtil.clear();
                //跳转登录
                Intent intent = new Intent(getContext(), ActLogin.class);
                startActivity(intent);
                getActivity().finish();//关闭当前Activity
                break;
            case R.id.btn_write://写入
                write();
                break;
            case R.id.btn_read://读取
                read();
                break;
            case R.id.btn_write_obj://对象存储
                User user = new User("android", "123456", "安卓", 20, "未知");
                boolean isSuccess = CookieUtil.saveObject(user);
                if (isSuccess) {
                    Toast.makeText(_mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_read_obj://对象读取
                User user2 = CookieUtil.getObject(User.class);
                Toast.makeText(_mActivity, "姓名：" + user2.getRealName() + "年龄：" + user2.getAge() + "岁" + "性别：" + user2.getSex(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_write_sd://SD卡存储
                //判断是否大于等于Android6.0
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                int i = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //判断权限是否开启
                if (i != PackageManager.PERMISSION_GRANTED){
                    //申请动态权限
                    ActivityCompat.requestPermissions(getActivity(),permissions,REQUEST_CODE);
                }else {
                    writeSD();
                }
                }else {
                    writeSD();
                }
                break;
            case R.id.btn_read_sd://SD卡读取
                readSD();
                break;
        }
    }



    /**
     * 读取SD卡
     */
    private void readSD() {
        //只有状态等于Mounted时，SD卡才可以读写
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(_mActivity, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = "";
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
        File file = new File(dir, "test.txt");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line =reader.readLine())!=null){
                builder.append(line);
            }
           content =  builder.toString();
            tvText.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                isr.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 写入SD卡
     */
    public void writeSD() {
        //只有状态等于Mounted时，SD卡才可以读写
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(_mActivity, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = "就在医务人员奋力阻止这场疫情之际，美中之间也在上演大国外交博弈。双方都想利用这次疫情来维护全球影响力，但两国的做法对比鲜明。中国强调国际合作，这迥异于白宫。华盛顿主要是利用危机指责北京，并将重心放在美国国内防控上。但白宫两年前关闭了流行病防范办公室，不久前还提议大幅削减对世卫组织的捐款。本周，特朗普政府的疫情应对小组组长迈克·彭斯及其团队，对与伙伴国家和国际机构合作的必要性只字未提。一名民主党国会助手说：“我觉得在国际上，美国没有什么大的有效战略抗击新冠病毒。本届政府不重视多边机构，只把它们视为替罪羊和只会花钱的机构。”特朗普本人甚至在疫情暴发初期暗示，美国人不能出国旅行可能更好，这反映出“美国优先”的立场";
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
        File file = new File(dir, "test.txt");
        FileOutputStream fos = null;
        try {
            //开启文件输出流
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            Toast.makeText(_mActivity, "保存成功", Toast.LENGTH_SHORT).show();
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

    /**
     * 内部存储-读取
     */
    private void read() {
        FileInputStream fis = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String content = "";
        try {
            //获取文件输入流
            fis = getActivity().openFileInput("news.txt");
            //InputStreamReader 是从字节流到字符流的桥接器，它使用指定的字符集读取字节并将它们解码为字符
            inputStreamReader = new InputStreamReader(fis);
            //BufferedReader是一个为提高读写效率的包装类，它可以包装字符流，从字符输入流中读取文本
            reader = new BufferedReader(inputStreamReader);
            //StringBuilder是一个提高拼接字符串的包装类
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            content = builder.toString();
            tvText.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();//关闭输入流
                inputStreamReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 内部存储-写入
     */
    private void write() {
        String content = "在武汉东湖新城社区，和习近平总书记面对面的，有社区工作者、基层民警、卫生服务站医生，还有下沉干部、志愿者。抗疫期间，社区居民缺米少油了、下水道堵了、垃圾箱满了……这些琐碎繁杂的大事小事，他们都得管。一个多月来，像他们这样的社区防控队伍，是武汉这座英雄城市一道独特的风景线。\n" +
                "\n" +
                "　　下沉干部孔祥锋向总书记讲述了他的25天抗疫经历。过去坐办公室写材料，如今他每天忙着帮居民们买菜送菜、跑这跑那：“我对社区从陌生到熟悉，社区居民对我也从不理解、跟我吵到现在笑脸相迎。一句‘谢谢你，小伙子’，自己再苦再累也值得。”\n" +
                "\n" +
                "　　大家都有很多话要说。有人谈到了最初的害怕和担忧，害怕被感染，担心居民群众不理解；也有人讲述了一个个感动和温暖的瞬间。一个多月来的经历，大家慢慢体会到，面对灾难，每一个人只要愿意发光，就是一束光，既照亮了邻里，也照亮了自己。";
        FileOutputStream fos = null;
        try {
            //获取文件输出流
            fos = getActivity().openFileOutput("news.txt", Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            Toast.makeText(_mActivity, "写入成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();//关闭流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
