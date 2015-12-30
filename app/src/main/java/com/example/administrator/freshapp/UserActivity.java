package com.example.administrator.freshapp;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.freshapp.loginUtil.login_Activity;
import com.example.administrator.freshapp.SwipUtil.SwipeBackLayout;
import com.example.administrator.freshapp.SwipUtil.slip.app.SwipeBackActivity;


public class UserActivity extends SwipeBackActivity{
    private TextView userDataView;
    private Button  getout;
    private String phonenumber;
    private SwipeBackLayout mSwipeBackLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_user);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.useractivity_title);
        mSwipeBackLayout = getSwipeBackLayout();
        int edgeFlag = SwipeBackLayout.EDGE_LEFT;
        mSwipeBackLayout.setEdgeTrackingEnabled(edgeFlag);

        SharedPreferences pref = getSharedPreferences("user_data",
                MODE_PRIVATE);
        phonenumber=pref.getString("phonenumber",null);
        //phonenumber="18819447484";
        if (phonenumber==null){
            Intent mintent=new Intent(this,login_Activity.class);
            startActivity(mintent);
            finish();
        }
        initUi();

        userDataView.setText("账号："+phonenumber);
    }
private void initUi(){
    userDataView=(TextView)findViewById(R.id.userData);
    getout=(Button)findViewById(R.id.getOut);

}

    public void getOut(View view){
        SharedPreferences.Editor editor = getSharedPreferences("user_data",
                MODE_PRIVATE).edit();
        editor.putString("phonenumber", null);
        editor.apply();
        Intent mintent=new Intent(this,login_Activity.class);
        startActivity(mintent);
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
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
