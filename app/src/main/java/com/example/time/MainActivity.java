package com.example.time;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    private IntentFilter intentFilter;

    TextView time;
    TextView work;
    TextView TopView;

    BottomNavigationView bottomNavigationView;

    ForegroundColorSpan Snow;
    AbsoluteSizeSpan sizeSpan1;
    String information;
    SpannableString insertemp_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarFullTransparent();

        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        Snow = new ForegroundColorSpan(Color.parseColor("#FFFAFA"));
        sizeSpan1=new AbsoluteSizeSpan(20,true);

        work=findViewById(R.id.work);
        time=findViewById(R.id.time_1);
        TopView=findViewById(R.id.TopView);
        getime_show();

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        registerReceiver(receiver,intentFilter);



        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(MainActivity.this, Edittime.class);
                        startActivityForResult(intent1,1);
                        overridePendingTransition(0,0);
                        break;
                    }
                    transaction.commit();
                    return false;
                }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            getime_show();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                getime_show();
            }
        }
    };

    public void getime_show(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd EE HH:mm", Locale.CHINA);
        LocalDateTime Rightnow=LocalDateTime.now();
        String now=dtf.format(Rightnow);

        time.setText(now.substring(9));
        String dating=now.substring(0,8);
        TopView.setText(dating);


        float time_hour=Float.parseFloat(now.substring(9,11));
        float time_min=Float.parseFloat(now.substring(12));
        float time=time_hour+time_min/100;


        SharedPreferences share = getSharedPreferences("mytime",MODE_PRIVATE);
            String begin_str;
            String end_str;
            String work_str;
            String begin_key;
            String end_key;
            String work_key;
            float begin_num=0;
            float end_num=0;
            boolean IsWorkNow=false;

            for(int i=1;i<9;i++){
                begin_key="begin_"+i;
                end_key="end_"+i;
                begin_str = share.getString(begin_key, "");
                end_str = share.getString(end_key, "");
                if (!begin_str.equals("")&& !end_str.equals("")){
                    begin_num = Integer.parseInt(begin_str.substring(0,begin_str.indexOf(":")))+ Integer.parseInt(begin_str.substring(begin_str.indexOf(":")+1))/100;
                    end_num = Integer.parseInt(end_str.substring(0,end_str.indexOf(":")))+ Integer.parseInt(end_str.substring(end_str.indexOf(":")+1))/100;
                }//continue?
                if (begin_num < time && time < end_num) {
                    work_key="work"+i;
                    work_str = share.getString(work_key, "");
                    information =begin_str + "  ～  " + end_str+"\n"+work_str;
                    insertemp_1= SpannableString.valueOf(information);
                    insertemp_1.setSpan(sizeSpan1, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    insertemp_1.setSpan(Snow, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    work.setText(insertemp_1);
                    IsWorkNow=true;
                    break;
                }
            }
            if(IsWorkNow==false){
                work.setText("无事件");
            }



    }
    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


}

