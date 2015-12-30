package com.example.administrator.freshapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.freshapp.Util.Favorite;
import com.example.administrator.freshapp.Util.GetDataFromServe;
import com.example.administrator.freshapp.Util.JsonToListMap;
import com.example.administrator.freshapp.iRibbonMenuUtil.RibbonMenuView;
import com.example.administrator.freshapp.iRibbonMenuUtil.iRibbonMenuCallback;
import com.example.administrator.freshapp.loginUtil.login_Activity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.ProgressView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Favorite_Activity extends Activity implements iRibbonMenuCallback {
    private String phonenumber;
    private LinkedList<HashMap<String, Object>> listitem;
    private Handler handler;
    private JsonToListMap jsonToListMap;
    private SimpleAdapter listapter;
    private PullToRefreshListView listView;
    private ProgressView progressView;
    private RibbonMenuView rbmView;
    private long firstTime = 0;
    private long secondTime=0;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_favorite_);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_favorite_title);
        listView=(PullToRefreshListView)findViewById(R.id.fresh2);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        progressView=(ProgressView)findViewById(R.id.progress2);
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView2);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        listView.setVisibility(View.GONE);
        SharedPreferences pref = getSharedPreferences("user_data",
                MODE_PRIVATE);
        phonenumber=pref.getString("phonenumber",null);
  // phonenumber="18819447484";
        pref = getSharedPreferences("favorite_data",MODE_PRIVATE);
     init();




        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label= DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label= DateUtils.formatDateTime(getApplicationContext(),System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new PullUpTask().execute();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map test_map=listitem.get(i-1);
                String data =test_map.get("title").toString();
                Intent intent = new Intent(Favorite_Activity.this, SwipBack_Content.class);
                intent.putExtra("title", data);
                startActivity(intent);


            }
        });

    }

    private  void init() {
        if (phonenumber == null) {

            Intent mintent = new Intent(Favorite_Activity.this, login_Activity.class);
            startActivity(mintent);
            finish();
        } else{

            listitem = new LinkedList<HashMap<String, Object>>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String str = Favorite.getFavorite("0", phonenumber);
               // Log.i("phonenumber====   ", phonenumber);
                Message message = new Message();
                message.obj = str;

                handler.sendMessage(message);


            }
        });
        thread.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null && !msg.obj.equals("401")) {

                    jsonToListMap = new JsonToListMap(Favorite_Activity.this);

                    listitem = jsonToListMap.favorite_ToListMap(msg.obj.toString(), "init");

                    listapter = new SimpleAdapter(Favorite_Activity.this, listitem, R.layout.list_iten, new String[]{"title", "tag", "until"}, new int[]{R.id.title0, R.id.textview_tag0, R.id.textview_date0});

                    listView.setAdapter(listapter);

                    progressView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    super.handleMessage(msg);
                } else {
                    Toast.makeText(Favorite_Activity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    }

    private class PullUpTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            pref = getSharedPreferences("favorite_data",MODE_PRIVATE);
            int sum =pref.getInt("sumOfNumber",10000000);
           Log.i("====pref",String.valueOf(sum));
            String str=Favorite.getFavorite(String.valueOf(sum),phonenumber);

            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("[]"))
                Toast.makeText(Favorite_Activity.this, "没有更多内容了",
                        Toast.LENGTH_SHORT).show();
            LinkedList<HashMap<String, Object>>  listitema;
            listitema=jsonToListMap.favorite_ToListMap(s,"up");
            listitem.addAll(listitema);
            listapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            super.onPostExecute(s);
        }
    }

    private class GetDataTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
         return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null)
                Toast.makeText(Favorite_Activity.this, "没有更多内容了",
                        Toast.LENGTH_SHORT).show();
            listapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            super.onPostExecute(s);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite_, menu);
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
    @Override
    public void RibbonMenuItemClick(int itemId) {
        switch (itemId){
            case R.id.ribbon_menu_home1:
                Intent intent1=new Intent(Favorite_Activity.this,MainActivity.class);
                startActivity(intent1);
                finish();

                break;
            case R.id.ribbon_menu_home2:
                Intent intent2=new Intent(Favorite_Activity.this,Favorite_Activity.class);
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
    public void favoriteMenu(View view){
        rbmView.toggleMenu();
    }
}
