package com.android.uoso.week12;
//网络请求地址
public class IpAddress {
    public static final String SERVERS = "http://v.juhe.cn"; //服务器地址
    public static final String APP_KEY ="9fbfe1092fa33bf4bf99d8b6a661963e";
    public static String getApi(String api){
        return SERVERS+api;
    }

    public static final String GET_NEWS = "/toutiao/index";//新闻接口
    public static final String GET_WEATHER = "/tianqi/index";//天气接口
}
