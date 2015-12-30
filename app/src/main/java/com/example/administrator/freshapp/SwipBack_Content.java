package com.example.administrator.freshapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.freshapp.SwipUtil.SwipeBackLayout;
import com.example.administrator.freshapp.SwipUtil.slip.app.SwipeBackActivity;
import com.example.administrator.freshapp.Util.Data;
import com.example.administrator.freshapp.Util.Favorite;

import com.example.administrator.freshapp.loginUtil.login_Activity;

import java.util.Map;


public class SwipBack_Content extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;
    private TextView textView_title,textView_content,textView_way,textView_address,textView_date,favoriteView;

    private float mScaleFactor = 1;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private static final int ZOOM_IN = 4;
    private static final int ZOOM_OUT = 5;
    private final int MAX_ZOOM_IN_SIZE = 60;
    private final int MAX_ZOOM_OUT_SIZE = 20;
    private final int THE_SIZE_OF_PER_ZOOM = 9;
    private float mTextSize = 27;
    private int mZoomMsg = -1;
    private Button favorite;
    private  String data_number;
    private  String phonenumber;
    String flag=null;

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_swip_back__content_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.swip_content_title);
        mSwipeBackLayout = getSwipeBackLayout();
        int edgeFlag = SwipeBackLayout.EDGE_LEFT;
        mSwipeBackLayout.setEdgeTrackingEnabled(edgeFlag);
            findview();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        Data data=new Data(this);
        Map map=data.readDataByTitle(title);
        data_number=map.get("data_number").toString();
        textView_title.setText(map.get("title").toString());
        textView_content.setText(map.get("content").toString());
        textView_way.setText("报名方式："+map.get("way").toString());
        textView_address.setText("地址："+map.get("address").toString());
        textView_date.setText("日期："+map.get("date").toString());

        SharedPreferences pref = getSharedPreferences("user_data",
                MODE_PRIVATE);
       phonenumber=pref.getString("phonenumber",null);
        if (phonenumber==null){
            Intent mintent=new Intent(this,login_Activity.class);
            startActivity(mintent);
            finish();
        }
        verrifyFavorite();
        initUi();
    }
    private void findview(){
        textView_title=(TextView)findViewById(R.id.swip_title);
        textView_content=(TextView)findViewById(R.id.swip_content);
        textView_way=(TextView)findViewById(R.id.swip_way);
        textView_date=(TextView)findViewById(R.id.swip_date);
        textView_address=(TextView)findViewById(R.id.swip_address);
        favorite=(Button)findViewById(R.id.favorite);
        favoriteView=(TextView)findViewById(R.id.favorite_view);

    }
    private void initUi() {      //复制功能
        textView_title.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        textView_content.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        textView_way.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        textView_date.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        textView_address.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());

        textView_title.setTextIsSelectable(true);
        textView_content.setTextIsSelectable(true);
        textView_way.setTextIsSelectable(true);
        textView_date.setTextIsSelectable(true);
        textView_address.setTextIsSelectable(true);
        mScaleDetector = new ScaleGestureDetector(this, new MyScaleListener());
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                });
        mGestureDetector.setOnDoubleTapListener(null);
   }
    private Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ZOOM_IN:
                    zoomIn();
//                    text.invalidate();
                    break;
                case ZOOM_OUT:
                    zoomOut();
//                    text.invalidate();
                    break;
                default:
                    break;
            }
        }
    };
    private void zoomIn() {
        mTextSize = mTextSize + THE_SIZE_OF_PER_ZOOM <= MAX_ZOOM_IN_SIZE ? mTextSize
                + THE_SIZE_OF_PER_ZOOM
                : MAX_ZOOM_IN_SIZE;
        if (mTextSize >= MAX_ZOOM_IN_SIZE) {
            mTextSize = MAX_ZOOM_IN_SIZE;
        }
//        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    private void zoomOut() {
        mTextSize = mTextSize - THE_SIZE_OF_PER_ZOOM < MAX_ZOOM_OUT_SIZE ? MAX_ZOOM_OUT_SIZE
                : mTextSize - THE_SIZE_OF_PER_ZOOM;
        if (mTextSize <= MAX_ZOOM_OUT_SIZE) {
            mTextSize = MAX_ZOOM_OUT_SIZE;
        }
//        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }
    private class MyScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            if (scale < 0.999999 || scale > 1.00001) {
                mScaleFactor = scale;
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            if (mScaleFactor > 1.0) {
                mZoomMsg = ZOOM_IN;
            } else if (mScaleFactor < 1.0) {
                mZoomMsg = ZOOM_OUT;
            }
        }
    }                  //复制功能

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swip_back__content, menu);
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
    public void do_favorite(View v){

        String favorite_text=favoriteView.getText().toString();

        if (favorite_text.equals("收藏")){
            favoriteView.setText("正在收藏");
            flag="2";
        }
        else if (favorite_text.equals("已收藏")){
            flag="1";

        }


        favorite.setEnabled(false);


     Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {
         Boolean  Save= Favorite.SendFavorite(data_number, phonenumber, flag);
             Message message=Message.obtain();
             message.what=1;
             message.obj=flag;
             if (Save){
                message.arg1=1;
             }
             else message.arg1=0;

           handler.sendMessage(message);
   }
     });
        thread.start();




    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                if (msg.obj.equals("2")) {
                    if (msg.arg1 == 1) {
                        favorite.setEnabled(true);
                        favoriteView.setText("已收藏");
                        Toast.makeText(SwipBack_Content.this, "收藏成功", Toast.LENGTH_LONG).show();
                    } else {
                        favoriteView.setText("收藏");
                        favorite.setEnabled(true);
                        Toast.makeText(SwipBack_Content.this, "收藏失败", Toast.LENGTH_LONG).show();
                    }
                } else if (msg.obj.equals("1")) {
                    if (msg.arg1 == 1){
                        favorite.setEnabled(true);
                        favoriteView.setText("收藏");
                    Toast.makeText(SwipBack_Content.this, "删除收藏成功", Toast.LENGTH_LONG).show();
                }
                    else {
                        favorite.setEnabled(true);
                        favoriteView.setText("已收藏");
                        Toast.makeText(SwipBack_Content.this, "删除收藏失败", Toast.LENGTH_LONG).show();


                    }
            }
            }
            else if(msg.what==2){
               if (msg.arg1==1){
                   favoriteView.setText("已收藏");

               }
                else {
                   favoriteView.setText("收藏");
               }
            }
        }
    };

public void verrifyFavorite(){
    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {

            Boolean isSave=Favorite.SendFavorite(data_number,phonenumber,"3");
            Message message=Message.obtain();
            message.what=2;
            if (isSave)message.arg1=1;
            else message.arg1=0;
            handler.sendMessage(message);


        }
    });
    thread.start();

}
}
