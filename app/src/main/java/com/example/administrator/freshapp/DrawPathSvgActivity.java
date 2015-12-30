package com.example.administrator.freshapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.freshapp.loginUtil.login_Activity;
import com.nineoldandroids.DrawPathSvg.SvgCompletedCallBack;
import com.nineoldandroids.DrawPathSvg.SvgView;


/**
 * Created by thibaultguegan on 29/05/2014.
 */
public class DrawPathSvgActivity extends Activity {
	
	private Button DoAgainBtn;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.svg_activity);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = getLayoutInflater();
        Log.i("====1",inflater.toString());
//        DoAgainBtn = (Button)findViewById(R.id.svg_btn_doagain);
        addSvgView(inflater, container);


    }

    private void addSvgView(LayoutInflater inflater, LinearLayout container)
    {
        final View view = inflater.inflate(R.layout.item_svg, container, false);

        final SvgView svgView = (SvgView) view.findViewById(R.id.svg);

        svgView.setSvgResource(R.raw.house);

        view.setBackgroundResource(R.color.accent);
        svgView.setmCallback(new SvgCompletedCallBack() {

			@Override
			public void onSvgCompleted() {
                Log.i("====5","comple");
                SharedPreferences pref = getSharedPreferences("user_data",
                        MODE_PRIVATE);
              String  phonenumber=pref.getString("phonenumber",null);
              //  phonenumber="18819447484";
                if (phonenumber==null){
                Intent intent = new Intent(DrawPathSvgActivity.this, login_Activity.class);
                startActivity(intent);
                    finish();
                }
                else {
                    Intent intent=new Intent(DrawPathSvgActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
//				DoAgainBtn.setEnabled(true);
			}
		});
        
        container.addView(view);
        Log.i("====6",view.toString());
//        DoAgainBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				DoAgainBtn.setEnabled(false);
//				svgView.startAnimation();
//
//			}
//		});

        Handler handlerDelay = new Handler();
        handlerDelay.postDelayed(new Runnable(){
            public void run() {
                Log.i("====7","run");
                svgView.startAnimation();
                Log.i("====8","run00");
            }}, 2000);

    }

}
