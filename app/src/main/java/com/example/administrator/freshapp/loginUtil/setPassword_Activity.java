package com.example.administrator.freshapp.loginUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.freshapp.R;


public class setPassword_Activity extends Activity {
    private String phonumber;
    private String mcode;
  private Button button;
    private EditText editText,editText2;
    private String password,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_set_password_);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.setpassword_activity_title_layout);
        Intent intent=getIntent();
        phonumber=intent.getStringExtra("phonenumber");
        mcode=intent.getStringExtra("code");
       button=(Button)findViewById(R.id.setPassword);
        editText=(EditText)findViewById(R.id.edit_password);
        editText2=(EditText)findViewById(R.id.edit_password2);

//        phonumber="18819447484";
//        mcode="8099";

    }

public void setpw(View v){
    password=editText.getText().toString();
    password2=editText2.getText().toString();

    if (!password.equals("")&&password.equals(password2)){
        button.setText("正在设置密码");
        button.setEnabled(false);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=Message.obtain();
                try {
                    if (PassWord.set_password(createHash.createEncrypPassword(password),phonumber,mcode)){
                        message.arg1=1;
                        handler.sendMessage(message);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
      }
    else if (!password.equals(password2)){
        Toast.makeText(this, "两次输入密码不一样", Toast.LENGTH_LONG).show();
  }
    else {

        Toast.makeText(this, "格式不正确", Toast.LENGTH_LONG).show();
    }



}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_password_, menu);
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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==1){
                button.setText("密码设置成功");
                Intent intent = new Intent();
                intent.putExtra("phonenumber",phonumber);
                intent.putExtra("code",mcode);
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    };
}
