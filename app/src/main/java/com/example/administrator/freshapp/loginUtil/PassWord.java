package com.example.administrator.freshapp.loginUtil;

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
 * Created by Administrator on 2015/4/26.
 */
public class PassWord {
    private static HttpResponse httpResponse;
    private static String  result;
    private static String password_url="http://1.issdr.sinaapp.com/password.php";
    public static boolean set_password(final String password,final String user_phonenumber,final String code) throws InterruptedException {
        boolean flag=false;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                Map<String ,String> rawParams=new HashMap<String, String>();
                rawParams.put("user_phonenumber", user_phonenumber);
                rawParams.put("code", code);
                rawParams.put("password", password);
                HttpPost post = new HttpPost(password_url);
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
        thread.join();

        if (result.equals("200")){
            flag=true;
        }
        return flag;
    }

    public static int verify_password(final String password, final String user_phonenumber) throws InterruptedException {
        int flag =0;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                Map<String, String> rawParams = new HashMap<String, String>();
                rawParams.put("user_phonenumber", user_phonenumber);
                rawParams.put("password", password);
                HttpPost post = new HttpPost(password_url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (String key : rawParams.keySet()) {
                    //封装请求参数
                    params.add(new BasicNameValuePair(key, rawParams.get(key)));
                }
                try {
                    post.setEntity(new UrlEncodedFormEntity(
                            params, "gbk"));

                    httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine()
                            .getStatusCode() == 200) {
                        result = EntityUtils
                                .toString(httpResponse.getEntity());
                        Log.i("===result----", result);

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
        thread.join();

        flag =Integer.valueOf(result);


        return flag;


    }
}
