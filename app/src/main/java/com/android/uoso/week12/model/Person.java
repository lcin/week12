package com.android.uoso.week12.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
@Table(name = "tb_person")
public class Person implements Serializable {
    @Column(name="id",isId = true,autoGen = true)//设置表的主键，默认自增
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    //必须有一个无参构造函数
    public Person() {
    }

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
