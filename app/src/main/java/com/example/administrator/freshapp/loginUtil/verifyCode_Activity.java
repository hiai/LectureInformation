package com.example.administrator.freshapp.loginUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.freshapp.R;

import org.apache.http.HttpResponse;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class verifyCode_Activity extends Activity implements OnClickListener {

	private Button sensmsButton,verificationButton,setpassword;
    private String check_url="http://1.issdr.sinaapp.com/SMS_test.php";
    private  int ckeck_code;
    private TextView textView;
    private String password_url="http://1.issdr.sinaapp.com/password.php";
    private String  result;
    private static HttpResponse httpResponse;
	private EditText phonEditText,verEditText,passwordEdiText;
    private static String APPKEY = "6bcd4f287d86";
    private static String APPSECRET = "c62a1178ebaf8430e10b8d6fb5a81744";
    private  String mcode;
    public String phString;
    private int flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avtivity_verifycode);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.verifyactivity_title_layout);
            init();
        Intent intent=getIntent();
        flag=intent.getIntExtra("flag",0);
      //  System.loadLibrary("libsmssdk");
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eh);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
   	switch (v.getId()) {
		case R.id.button1://获取验证码

			if(!TextUtils.isEmpty(phonEditText.getText().toString())&&phonEditText.getText().toString().length()==11){
				SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
                phString=phonEditText.getText().toString();

                button_sleep();


            }else if (phonEditText.getText().toString().length()!=11){
				Toast.makeText(this, "号码格式不正确", 1).show();
			}
            else if (TextUtils.isEmpty(phonEditText.getText().toString())){
            Toast.makeText(this, "号码不能为空", 1).show();

        }
			
			break;
		case R.id.button2://校验验证码
			if(!TextUtils.isEmpty(verEditText.getText().toString())&&verEditText.getText().toString().length()==4&&phString!=null){
                verificationButton.setText("正在验证...");
                verificationButton.setEnabled(false);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=Message.obtain();
                        try {
                            if (flag==1) {              //分为未注册验证(1)和已注册验证(2,3)
                                if (Code.CheckCode(verEditText.getText().toString(), phString)) {
                                    message.arg1 = 1;
                                    sleep_handler.sendMessage(message);

                                } else {
                                    message.arg1 = 2;
                                    sleep_handler.sendMessage(message);
                                }
                            }
                            else {
                                int mflag;
                                mflag=Code.Login_Code(verEditText.getText().toString(), phString);
                                if (mflag==201) {
                                    message.arg1 = 1;
                                    sleep_handler.sendMessage(message);

                                } else if (mflag==100){
                                    message.arg1 = 2;
                                    message.arg2=1;
                                    sleep_handler.sendMessage(message);
                                }
                                else if (mflag==101){
                                    message.arg1 = 2;
                                    message.arg2=2;
                                    sleep_handler.sendMessage(message);

                                }


                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            }else if (verEditText.getText().toString().length()!=4){
                Toast.makeText(this, "验证码格式不正确", 1).show();

            }

            else {
				Toast.makeText(this, "格式不正确", 1).show();
			}
			break;

		default:
			break;
		}
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
       if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();

				}
			}
		}
		
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
   private void button_sleep(){
        sensmsButton.setEnabled(false);
       sensmsButton.setBackgroundColor(Color.parseColor("#d5d4d2"));
              sensmsButton.setTextColor(Color.parseColor("#fdfcfa"));
      Thread   thread=new Thread(){
          @Override
          public void run() {
              int waitTime=60;
              while (waitTime-->0) {
                  Message message = new Message();
                  message.what = waitTime;
                  sleep_handler.sendMessage(message);
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }

          }
      };
        thread.start();

   }
    private Handler sleep_handler = new Handler(){

        @Override

        public void handleMessage(Message msg) {

            if (msg.arg1 == 1) {
                Toast.makeText(verifyCode_Activity.this,"验证码正确",Toast.LENGTH_LONG).show();
                verificationButton.setText("验证成功");
                mcode=verEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("phonenumber",phString);
                intent.putExtra("code",mcode);
                setResult(RESULT_OK, intent);
                finish();

            }
            else if (msg.arg1==2){
                verificationButton.setText("验证");
                verificationButton.setEnabled(true);
                if (msg.arg2==1){
                    Toast.makeText(verifyCode_Activity.this,"验证码错误",Toast.LENGTH_LONG).show();
                }
                else if (msg.arg2==2){
                    Toast.makeText(verifyCode_Activity.this,"你还未注册",Toast.LENGTH_LONG).show();
                      finish();
                }

            }else {
                sensmsButton.setText("已发送(" + msg.what + ")");
                if (msg.what == 0) {
                    sensmsButton.setBackgroundColor(Color.parseColor("#1db9f2"));
                    sensmsButton.setTextColor(Color.parseColor("#f1ffe7"));
                    sensmsButton.setEnabled(true);
                    sensmsButton.setText("获取验证码");

                }
            }
        }
    };
private void init(){
    sensmsButton=(Button) findViewById(R.id.button1);
    verificationButton=(Button) findViewById(R.id.button2);
   phonEditText=(EditText) findViewById(R.id.editText1);
    verEditText=(EditText) findViewById(R.id.editText2);
  sensmsButton.setOnClickListener(this);
    verificationButton.setOnClickListener(this);

}
}
