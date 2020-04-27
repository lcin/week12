package com.android.uoso.week12.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.uoso.week12.App;
import com.android.uoso.week12.R;
import com.android.uoso.week12.adapter.PersonAdapter;
import com.android.uoso.week12.model.Person;
import com.android.uoso.week12.utils.MySQLiteHelper;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class FrContact extends SupportFragment implements AdapterView.OnItemLongClickListener {
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_age)
    EditText edAge;
    @BindView(R.id.btn_insert)
    Button btnInsert;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_upgrade)
    Button btnUpgrade;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.list_view)
    ListView listView;
    Unbinder unbinder;
    private SQLiteDatabase db;

    private List<Person> list = new ArrayList<>();//人物列表
    private PersonAdapter adapter;
    private DbManager xDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_contact, null);
        MySQLiteHelper helper = new MySQLiteHelper(getContext(), MySQLiteHelper.DB_NAME, null, 1);
        //创建或打开一个可读可写的数据库,如果磁盘满了就只可读，通常采用这种
        db = helper.getReadableDatabase();
        //创建或打开一个可读可写的数据库,如果磁盘满了写入会报错
        //SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        //获取xutils3数据库操作类对象
        xDB = x.getDb(App.getDaoConfig());
        unbinder = ButterKnife.bind(this, view);
        adapter = new PersonAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        query();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_upgrade, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                insert();
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_upgrade:
                break;
            case R.id.btn_query:
                query();
                break;
        }
    }

    /**
     * 查询
     */
    private void query() {
        list.clear();//清空之前的数据
        /**
          String table, 表名
          String[] columns, 查询的列名
          String selection, 筛选条件
         String[] selectionArgs, 筛选值
         String groupBy,  分组条件
         String having,   分组值
         String orderBy,  按...排序
         String limit  “0,20” "a,b" 从第a+1条数据开始，总共查询b条
         */
        //游标
        Cursor cursor = db.query(MySQLiteHelper.TB_PERSON, new String[]{"id", "name", "age"}, null, null, null, null, null);
        while (cursor.moveToNext()){//游标移动到下一位，如果有数据则返回true
            //getColumnIndex通过列名获取对应的下标
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            Person person = new Person(id, name, age);
            list.add(person);
        }
        /*try {
            //查询所有数据
            list = xDB.selector(Person.class).findAll();
            //查询指定某条数据
            *//*Person person = xDB.selector(Person.class).where("name", "=", "xiaoming")
                    .and("id", "=", 10)
                    .findFirst();*//*
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(_mActivity, "查询失败", Toast.LENGTH_SHORT).show();
        }*/
        adapter.setData(list,0);
    }

    /**
     * 增加
     */
    private void insert() {
        String name = edName.getText().toString();
        String age = edAge.getText().toString();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("age",age);
        //nullColumnHack 代表强行插入null值得数据列列名
        //rowId 返回新添标记的行号，该行号是一个内部值，与主键无关，当发生失败时为-1
        long rowId = db.insert(MySQLiteHelper.TB_PERSON, null, values);
        if (rowId==-1){
            Toast.makeText(_mActivity, "插入失败", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(_mActivity, "插入成功", Toast.LENGTH_SHORT).show();
            query();
        }
       /* Person person = new Person();
        person.setAge(Integer.parseInt(age));
        person.setName(name);//保存数据库对象
        try {
            xDB.save(person);
            query();
        } catch (DbException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Person person = list.get(position);
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        getActivity().getMenuInflater().inflate(R.menu.menu_person,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_upgrade:
                        upgrade(person);
                        break;
                    case R.id.menu_delete:
                        delete(person);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
        return false;
    }

    /**
     * 删除
     * @param person
     */
    private void delete(Person person) {
        int number = db.delete(MySQLiteHelper.TB_PERSON, "id=?", new String[]{person.getId() + ""});
        if (number==0){
            Toast.makeText(_mActivity, "删除失败", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(_mActivity, "删除成功", Toast.LENGTH_SHORT).show();
            query();
        }
        /*try {
            xDB.delete(person);//删除数据
           // xDB.deleteById(Person.class,person.getId());
           // xDB.delete(Person.class, WhereBuilder.b("name","=","xiaoming"));
            query();
        } catch (DbException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 修改
     * @param person
     */
    private void upgrade(Person person) {
        String name = edName.getText().toString().trim();
        String age = edAge.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("age",age);
        int number = db.update(MySQLiteHelper.TB_PERSON, values, "id=?", new String[]{person.getId() + ""});
        if (number==0){
            Toast.makeText(_mActivity, "修改失败", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(_mActivity, "修改成功", Toast.LENGTH_SHORT).show();
            query();
        }

        /*KeyValue value_name = new KeyValue("name", name);
        KeyValue value_age = new KeyValue("age", age);
        try {
            //修改数据
            xDB.update(Person.class,WhereBuilder.b("id","=",person.getId()),value_name,value_age);
            query();
        } catch (DbException e) {
            e.printStackTrace();
        }*/
    }
}
