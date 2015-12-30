package com.example.administrator.freshapp.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.LinkedList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Administrator on 2015/3/30.
 */
public class JsonToListMap {
    private static HashMap<String,Object> map;
    private static Data data;
    private int down_number=0;
    private int up_number=0;
    private  SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private LinkedList<HashMap<String,Object>> mapList;
    private static String $content,$address,$way, $until, $state,$date,$title,$tag,$data_number;
    private SharedPreferences.Editor favorite_editor,content_editor;
    private SharedPreferences pref;
    public JsonToListMap(Context context){
        data=new Data(context);
        content_editor= context.getSharedPreferences("content_data", 0).edit();

        favorite_editor= context.getSharedPreferences("favorite_data",  0).edit();

        pref = context.getSharedPreferences("favorite_data",  0);


    }
    public LinkedList<HashMap<String,Object>> ToListMap(String json,String state){

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            mapList=new LinkedList<HashMap<String, Object>>();

            for (int i = 0; i < jsonArray.length(); i++) {
                map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                $content = jsonObject.getString("content");
                $address = jsonObject.getString("address");
                $way = jsonObject.getString("way");
                $until = jsonObject.getString("until");
                $state = jsonObject.getString("state");
                $date = jsonObject.getString("date");
                $title = jsonObject.getString("title");
                $tag = jsonObject.getString("tag");
                $data_number = jsonObject.getString("data_number");
                int data_number=Integer.parseInt($data_number);
//                Log.i("data$$===",String.valueOf($data_number));
                if (i==0){down_number=data_number;
                    up_number=data_number;}
                if (data_number>up_number){up_number=data_number;}
                else if (data_number<down_number){down_number=data_number;}
                Object[] params_a = {$content, $address, $way, $until, $state, $date,$title, $tag,$data_number};
                data.addData(params_a);
                map.put("title", $title);

                map.put("tag", $tag);

                map.put("until", Until_Convent_Todate.Convent($until));
                mapList.add(map);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (state.equals("init")){
            content_editor.putInt("down_number",down_number);
            content_editor.putInt("up_number", up_number);}
        else if (state.equals("up")){ content_editor.putInt("up_number", up_number);}
        else if (state.equals("down")){content_editor.putInt("down_number",down_number);}
        content_editor.apply();
//        Log.i("===data_number", String.valueOf(down_number)+"   "+String.valueOf(up_number));
        return mapList;
    }

    public LinkedList<HashMap<String,Object>> favorite_ToListMap(String json,String state){
        Log.i("obj ====  ",json);
     int sum=0;
        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(json);
            mapList=new LinkedList<HashMap<String, Object>>();
            Log.i("length() ====  ",String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                $content = jsonObject.getString("content");
                $address = jsonObject.getString("address");
                $way = jsonObject.getString("way");
                $until = jsonObject.getString("until");
                $state = jsonObject.getString("state");
                $date = jsonObject.getString("date");
                $title = jsonObject.getString("title");
                $tag = jsonObject.getString("tag");
                $data_number = jsonObject.getString("data_number");
                int data_number=Integer.parseInt($data_number);
               // Log.i("data$$===",String.valueOf($data_number));
                if (i==0){down_number=data_number;
                    up_number=data_number;}
                if (data_number>up_number){up_number=data_number;}
                else if (data_number<down_number){down_number=data_number;}
                Object[] params_a = {$content, $address, $way, $until, $state, $date,$title, $tag,$data_number};
                data.addData(params_a);
                map.put("title", $title);

                map.put("tag", $tag);

                map.put("until", Until_Convent_Todate.Convent($until));
                mapList.add(map);


            }

        } catch (JSONException e) {
            Log.i("ee ====  ",e.toString());
            e.printStackTrace();
        }


        ;
        if (state.equals("init")){
            int sumOfNumber=jsonArray.length();
            Log.i("====preflist",String.valueOf(sumOfNumber));
            favorite_editor.putInt("sumOfNumber",sumOfNumber);

       }
        else if (state.equals("up")){

            int sumOfNumber= pref.getInt("sumOfNumber",0)+jsonArray.length();
            favorite_editor.putInt("sumOfNumber",sumOfNumber);}
        else if (state.equals("down")){
           }
        favorite_editor.apply();
       return mapList;
    }
}
