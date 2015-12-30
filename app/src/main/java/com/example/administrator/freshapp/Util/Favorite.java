package com.example.administrator.freshapp.Util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/12.
 */
public class Favorite {
    private static String url="http://1.issdr.sinaapp.com/favorite.php";
    private static String get_url="http://1.issdr.sinaapp.com/get_favorite.php";
    private static HttpResponse httpResponse;
    private static  String result;
    private static Thread thread;
    public  static boolean SendFavorite(final String data_number,final String phonenumber,final String flag){
        thread =new Thread(new Runnable() {
            @Override
            public void run() {


                HttpClient httpClient = new DefaultHttpClient();


                Map<String ,String> rawParams=new HashMap<String, String>();
                rawParams.put("phonenumber", phonenumber);
                rawParams.put("number", data_number);
                rawParams.put("flag", flag);
                // 创建HttpPost对象。
                HttpPost post = new HttpPost(url);
                // 如果传递参数个数比较多的话可以对传递的参数进行封装
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key : rawParams.keySet())
                {
                    //封装请求参数
                    params.add(new BasicNameValuePair(key , rawParams.get(key)));
                }
                // 设置请求参数

                try {
                    post.setEntity(new UrlEncodedFormEntity(
                            params, "gbk"));

                    httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine()
                            .getStatusCode() == 200) {
                        result = EntityUtils
                                .toString(httpResponse.getEntity());


                    }



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.i("e====",e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("ioe====",e.toString());
                    e.printStackTrace();
                }

            }
        });
        thread.start();


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.equals("200")){
            return true;

        }
        else return false;


    }

    public  static String getFavorite(final String length,final String phonenumber){
        thread =new Thread(new Runnable() {
            @Override
            public void run() {


                HttpClient httpClient = new DefaultHttpClient();


                Map<String ,String> rawParams=new HashMap<String, String>();
                rawParams.put("phonenumber", phonenumber);
                rawParams.put("length", length);

                // 创建HttpPost对象。
                HttpPost post = new HttpPost(get_url);
                // 如果传递参数个数比较多的话可以对传递的参数进行封装
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key : rawParams.keySet())
                {
                    //封装请求参数
                    params.add(new BasicNameValuePair(key , rawParams.get(key)));
                }
                // 设置请求参数

                try {
                    post.setEntity(new UrlEncodedFormEntity(
                            params, "gbk"));

                    httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine()
                            .getStatusCode() == 200) {
                        result = EntityUtils
                                .toString(httpResponse.getEntity());


                    }



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.i("e====",e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("ioe====",e.toString());
                    e.printStackTrace();
                }

            }
        });
        thread.start();


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       return result;


    }

}
