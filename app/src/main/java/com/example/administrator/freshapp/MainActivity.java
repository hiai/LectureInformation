package com.example.administrator.freshapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.freshapp.Util.GetDataFromServe;
import com.example.administrator.freshapp.Util.JsonToListMap;
import com.example.administrator.freshapp.iRibbonMenuUtil.RibbonMenuView;
import com.example.administrator.freshapp.iRibbonMenuUtil.iRibbonMenuCallback;
import com.example.administrator.freshapp.loginUtil.login_Activity;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.ProgressView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



public class MainActivity extends Activity implements iRibbonMenuCallback {
   private   SharedPreferences pref;
    private ProgressView progressView;
    private JsonToListMap jsonToListMap;
    private GetDataFromServe serve;
    private int listlength=5;
    private long firstTime = 0;
    private  Handler handler;
    private long secondTime;
    private PullToRefreshListView listView;
    private SimpleAdapter listapter;
    private  LinkedList<HashMap<String,Object>> listitem;
    private RibbonMenuView  rbmView;

    @Override
    protected void onResume() {
        super.onResume();
 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title_layout);
        jsonToListMap=new JsonToListMap(this);
        listView=(PullToRefreshListView)findViewById(R.id.fresh1);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        progressView=(ProgressView)findViewById(R.id.progress);
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        listView.setVisibility(View.GONE);
        init();

         pref = getSharedPreferences("content_data",
                MODE_PRIVATE);


        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label= DateUtils.formatDateTime(getApplicationContext(),System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label= DateUtils.formatDateTime(getApplicationContext(),System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new getdata2().execute();
            }
        });
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map test_map=listitem.get(i-1);
               String data =test_map.get("title").toString();
                 Intent intent = new Intent(MainActivity.this, SwipBack_Content.class);
                 intent.putExtra("title", data);
                 startActivity(intent);


             }
         });


    }
    private  void init(){
        serve=new GetDataFromServe(this);
        listitem=new LinkedList<HashMap<String, Object>>();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                    String str=serve.init_GetData(listlength);
                    Message message=new Message();
                    message.obj=str;
                    handler.sendMessage(message);


            }
        });
        thread.start();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj!=null){

                    jsonToListMap=new JsonToListMap(MainActivity.this);
                    listitem=jsonToListMap.ToListMap(msg.obj.toString(),"init");

                    listapter=new SimpleAdapter(MainActivity.this,listitem,R.layout.list_iten,new String[]{"title","tag","until"},new int[]{R.id.title0,R.id.textview_tag0,R.id.textview_date0});

                    listView.setAdapter(listapter);

                    progressView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    super.handleMessage(msg);
                }
                else { Toast.makeText(MainActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();}
            }
        };
     }

    private class GetDataTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            int end_number =pref.getInt("up_number",10000006);
            String str=serve.GetData(end_number+1,end_number+listlength+1);

            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("[]"))
            Toast.makeText(MainActivity.this, "没有更多内容了",
                    Toast.LENGTH_SHORT).show();
            LinkedList<HashMap<String, Object>> listitem1;
            listitem1=jsonToListMap.ToListMap(s,"up");
            for (int i=listitem1.size()-1;i>=0;i--){
                HashMap<String, Object> map;
                map=listitem1.get(i);
                listitem.addFirst(map);
            }



            listapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            super.onPostExecute(s);
        }
    }
    private class getdata2 extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
           int start_number =pref.getInt("down_number",10000006);
            String str=serve.GetData(start_number-listlength,start_number-1);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("[]"))
                Toast.makeText(MainActivity.this, "没有更多内容了",
                        Toast.LENGTH_SHORT).show();
          LinkedList<HashMap<String, Object>>  listitema;
            listitema=jsonToListMap.ToListMap(s,"down");
          listitem.addAll(listitema);
            listapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            super.onPostExecute(s);
        }
    }


    @Override
    public void onBackPressed() {
        secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {              //如果两次按键时间间隔大于2秒，则不退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;//更新firstTime

        } else {                                         //两次按键小于2秒时，退出应用
            Intent intent=new Intent();

            intent.setAction(intent.ACTION_MAIN);
            intent.addCategory(intent.CATEGORY_HOME);
            startActivity(intent);
     }

    }
    public void toMenu(View view){
      rbmView.toggleMenu();
 }
    @Override
    public void RibbonMenuItemClick(int itemId) {
        switch (itemId){
            case R.id.ribbon_menu_home1:
              Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

                break;
            case R.id.ribbon_menu_home2:
                Intent intent2=new Intent(MainActivity.this,Favorite_Activity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.ribbon_menu_home3:
                Intent home3=new Intent(this,UserActivity.class);
                startActivity(home3);

                break;

        }
  }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
