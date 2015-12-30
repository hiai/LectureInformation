package com.example.administrator.freshapp.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/3/20.
 */
public class SQLiteHelper extends SQLiteOpenHelper{
private static String name="data.db";
    private static int version=1;
    public SQLiteHelper(Context context)
    {
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table data(content  varchar(300),address varchar(200),way varchar(200),until int(6),state int(1),date varchar(100),title varchar(100),tag varchar(50),data_number int(8))";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
