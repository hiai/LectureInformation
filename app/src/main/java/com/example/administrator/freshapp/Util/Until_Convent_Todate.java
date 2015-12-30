package com.example.administrator.freshapp.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/4/6.
 */
public class Until_Convent_Todate {
     private static int day;
   private static String Week;
    private static  String mMonth;
    private static  String mDay;
    public static String Convent(String until){
        mMonth=until.substring(2,4);
        mDay=until.substring(4);
     Calendar nowDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date="20"+until.substring(0,2)+"-"+mMonth+"-"+mDay;
//        Log.i("===day",date);
        try {
            nowDate.setTime(sdf.parse(date));//'2010-08-10'这是自己输入的需要计算的年月日
            day = nowDate.get(Calendar.DAY_OF_WEEK);
//            Log.i("=====day",String.valueOf(day));

        } catch (ParseException e) {
            Log.i("====eeday",e.toString());
            e.printStackTrace();
        }
    switch(day)
        {
            case 1:
                Week="星期日";
                break;
            case 2:
                Week="星期一";
                break;
            case 3:
                Week="星期二";
                break;
            case 4:
                Week="星期三";
                break;
            case 5:
                Week="星期四";
                break;
            case 6:
                Week="星期五";
                break;
            case 7:
                Week="星期六";
                break;
            default:
                break;
        }

return mMonth+"."+mDay+"   "+Week;
    }
}
