package com.example.administrator.freshapp.loginUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.freshapp.MainActivity;
import com.example.administrator.freshapp.R;

import org.apache.http.HttpResponse;


public class login_Activity extends Activity {
    private EditText editText;
    private TextView textView;
    private String password_url = "http://1.issdr.sinaapp.com/password.php";
    private static HttpResponse httpResponse;
    private EditText password_edit,phone_edit;
    private Button change_button,login_button;
   private String result;
    private long firstTime = 0;

    private long secondTime;
//    private final String phonenumber;
//    private final String password;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_login);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.login_title_layout);
        view_init();

  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
  public void code_login(View v){
      Intent intent=new Intent(this,verifyCode_Activity.class);
      intent.putExtra("flag",3);
      startActivityForResult(intent, 1);


  }


    public void sign_up(View v){

        Intent intent=new Intent(this,verifyCode_Activity.class);
        intent.putExtra("flag",1);
        startActivityForResult(intent, 2);

    }
    public void setpassword_again(View v){
        Intent intent=new Intent(this,verifyCode_Activity.class);
        intent.putExtra("flag",2);
        startActivityForResult(intent, 2);

    }
    public void verrify(View v) {
   String   mpassword = password_edit.getText().toString();
   final String phonenumber=phone_edit.getText().toString();
        login_button.setText("正在登陆...");
         login_button.setEnabled(false);


        if (!mpassword.equals("")&&!phonenumber.equals("")&&phonenumber.length()==11) {
            final String   password;
            password=    createHash.createEncrypPassword(mpassword);
                   Thread thread=new Thread(new Runnable() {
                       @Override
                       public void run() {
                           Message message=Message.obtain();
                           try {
                               int mflag=PassWord.verify_password(password, phonenumber);
                               if(mflag==200){
                                  message.obj=phonenumber;
                                   message.what=1;
                                   handler.sendMessage(message);
                               }
                               else if (mflag==201){
                                   message.what=201;
                                   handler.sendMessage(message);
                               }
                               else if (mflag==202){
                                   message.what=202;
                                   handler.sendMessage(message);
                               }
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }


                       }
                   });
              thread.start();

     }
        else {
            login_button.setEnabled(true);
           login_button.setText("登陆");
            Toast.makeText(this, "格式不正确", Toast.LENGTH_LONG).show();
            textView.setText("格式不正确");
        }
    }

private void view_init(){
    password_edit = (EditText) findViewById(R.id.login_password);
    phone_edit=(EditText)findViewById(R.id.login_phonenumber);
    change_button=(Button)findViewById(R.id.change_button);
    login_button=(Button)findViewById(R.id.button_login);

    password_edit.setHint("密码");
    phone_edit.setHint("手机号码");
    login_button.setText("登陆");

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {       //验证码登陆成功结果
                    String phonenumber = data.getStringExtra("phonenumber");
                    String code = data.getStringExtra("code");
                    SharedPreferences.Editor editor = getSharedPreferences("user_data",
                            MODE_PRIVATE).edit();
                    editor.putString("phonenumber", phonenumber);
                    editor.apply();
                    Intent intent=new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                   // textView.setText("登陆成功code");
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {       //验证码注册结果
                    String phonenumber = data.getStringExtra("phonenumber");
                    String code = data.getStringExtra("code");
                    Intent intent=new Intent(this,setPassword_Activity.class);
                    intent.putExtra("phonenumber",phonenumber);
                    intent.putExtra("code",code);
                    startActivityForResult(intent, 3);

                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {       //注册成功结果
                    String phonenumber = data.getStringExtra("phonenumber");
                    String code = data.getStringExtra("code");
                    SharedPreferences.Editor editor = getSharedPreferences("user_data",
                            MODE_PRIVATE).edit();
                    editor.putString("phonenumber", phonenumber);
                    editor.commit();
                    Intent intent=new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                   // textView.setText("注册成功");
                }
                break;
            default:
                break;
        }
    }
 android.os.Handler handler=new android.os.Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what==1) {
            String phonenumber = String.valueOf(msg.obj);
            SharedPreferences.Editor editor = getSharedPreferences("user_data",
                    MODE_PRIVATE).edit();
            editor.putString("phonenumber", phonenumber);
            editor.commit();
            login_button.setText("登陆成功");
            Intent intent=new Intent(login_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
            //textView.setText("登陆成功1");
        }
        else if (msg.what==201){
            login_button.setEnabled(true);
            login_button.setText("登陆");
            Toast.makeText(login_Activity.this, "密码错误", Toast.LENGTH_LONG).show();
            textView.setText("false");
        }
        else if (msg.what==202){
            login_button.setEnabled(true);
            login_button.setText("登陆");
            Toast.makeText(login_Activity.this, "你还未注册，请先注册", Toast.LENGTH_LONG).show();
            textView.setText("false");
        }
    }
};
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
     }
