package com.android.uoso.week12.model;

import java.io.Serializable;
//用于发布和接收EventBus事件的实体类
public class MessageEvent implements Serializable {
    private int type;//表示事件的类型
    private Object obj;//传递的参数

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
