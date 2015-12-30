package com.example.administrator.freshapp.Util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.freshapp.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 从sqlite中增加，删除，读取数据
 * Created by Administrator on 2015/3/21.
 */
public class Data implements DataService {
    private SQLiteHelper helper;
     private Thread thread;
    private Data data;
    private Context context1;
    public Data(Context context) {
        helper = new SQLiteHelper(context);
context1=context;
    }

    @Override
    public Map<String,String> readDataByTitle(String title) {
        Map<String, String> map = new HashMap<String, String>();
       String[] strings={title};
        SQLiteDatabase database = null;
        String sql = "select * from data where title=? ";

        try {
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, strings);
            int cursors = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursors; i++) {
                    String cols_name = cursor.getColumnName(i);

                    String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));

                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }


            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }


        return map;

    }

    @Override
    public boolean deleteData(Object[] number) {
        boolean flag = false;
        SQLiteDatabase database = null;
        String sql = "delete from data where data_number=?";
        try {
            database = helper.getWritableDatabase();


            database.execSQL(sql, number);
            flag = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
        return flag;
    }

    @Override
    public boolean updateData(Object[] number) {
        return false;
    }

    @Override
    public Map<String, String> readData(String[] number) {
        Map<String, String> map = new HashMap<String, String>();
      Log.i("===data",number.toString());
        boolean flag = false;
        SQLiteDatabase database = null;
        String sql = "select * from data where data_number=? ";

        try {
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, number);
            int cursors = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursors; i++) {
                    String cols_name = cursor.getColumnName(i);

                    String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));

                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }


            }


        } catch (Exception e) {
          //  Toast.makeText(context1, e.toString(), Toast.LENGTH_LONG ).show();


            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }


        return map;
    }

    @Override
    public boolean addData(final Object[] params) {

          String[]  strings={params[8].toString()};
         final Map<String,String> map=this.readData(strings);
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                  SQLiteDatabase database = null;
                    try {
                     if (map.isEmpty()) {
                            String sql = "insert into data values ( ?,?,?, ?, ?,?,?,?,?)";
                            database = helper.getWritableDatabase();
                            database.execSQL(sql, params);
                        }
                   } catch (Exception e) {
                        Log.i("===e",e.toString());
                    } finally {
                        if (database != null)
                            database.close();
                    }


                }

            });
        thread.start();

      return true;
}
}