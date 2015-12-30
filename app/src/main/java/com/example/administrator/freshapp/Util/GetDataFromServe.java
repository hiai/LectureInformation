package com.example.administrator.freshapp.Util;

import android.content.Context;
import android.os.Handler;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**从服务器上读取数据
 * Created by Administrator on 2015/3/20.
 */
public class GetDataFromServe {

    private static String url="http://1.issdr.sinaapp.com/response_to_client.php";
    private String init_url="http://1.issdr.sinaapp.com/client_init.php";
    private static Thread thread;
    private JsonToListMap jsonToListMap;
    private static String result=null;
     private static HttpResponse httpResponse;
    public GetDataFromServe(Context context){
       jsonToListMap=new JsonToListMap(context);

    }
    public   String GetData(final int start_number,final int end_number){
        thread =new Thread(new Runnable() {
            @Override
            public void run() {


                HttpClient httpClient = new DefaultHttpClient();


                Map<String ,String> rawParams=new HashMap<String, String>();
                rawParams.put("start_number", String.valueOf(start_number));
                rawParams.put("end_number", String.valueOf(end_number));
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
                       Log.i("===12result",result);

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
    public String init_GetData(final int length){
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
             Map<String ,String> rawParams=new HashMap<String, String>();
                rawParams.put("json_length", String.valueOf(length));
                HttpPost post = new HttpPost(init_url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key : rawParams.keySet())
                {
                    //封装请求参数
                    params.add(new BasicNameValuePair(key , rawParams.get(key)));
                }
                try {
                    post.setEntity(new UrlEncodedFormEntity(
                            params, "gbk"));

                    httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine()
                            .getStatusCode() == 200) {
                        result = EntityUtils
                                .toString(httpResponse.getEntity());
                        Log.i("===result----",result);

                    }



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
