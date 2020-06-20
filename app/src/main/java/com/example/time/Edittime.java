package com.example.time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Edittime extends AppCompatActivity {
    Button[]workset=new Button[9];
    String work_str;
    String begin_str ;
    String end_str ;
    String work_key;
    String begin_key ;
    String end_key ;
    String information;
    SpannableString insertemp_1;
    FloatingActionButton fab;
    TextView ShowDate;
    TextView ShowDate_2;

    Integer Button_num=9;
    BottomNavigationView bottomNavigationView;
    ForegroundColorSpan LightSeaGreen;
    AbsoluteSizeSpan sizeSpan1;

    Integer hour_start;
    Integer min_start;
    Integer hour_end;
    Integer min_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittime);
        setStatusBarFullTransparent();


        ShowDate=findViewById(R.id.showdate);
        ShowDate_2=findViewById(R.id.showdate2);
        ShowDate_init();

        workset[1]=findViewById(R.id.work_1);workset[2]=findViewById(R.id.work_2);workset[3]=findViewById(R.id.work_3);workset[4]=findViewById(R.id.work_4);
        workset[5]=findViewById(R.id.work_5);workset[6]=findViewById(R.id.work_6);workset[7]=findViewById(R.id.work_7);workset[8]=findViewById(R.id.work_8);
        OnClick onClick=new OnClick();
        OnLongClick onLongClick=new OnLongClick();
        for(int i=1;i<Button_num;i++){
            workset[i].setOnClickListener(onClick);
            workset[i].setOnLongClickListener(onLongClick);
        }

        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        LightSeaGreen = new ForegroundColorSpan(Color.parseColor("#20B2AA"));
        sizeSpan1=new AbsoluteSizeSpan(15,true);

        SharedPreferences share = getSharedPreferences("mytime",MODE_PRIVATE);
        for(int i=1;i<Button_num;i++){
            get_str(i,share);
            if(!work_str.equals("")&&!begin_str.equals("")&&!end_str.equals("")) {
                information =begin_str + "  ～  " + end_str+"\n"+work_str+"\n";
                insertemp_1=SpannableString.valueOf(information);
                insertemp_1.setSpan(sizeSpan1, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                insertemp_1.setSpan(LightSeaGreen, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                workset[i].setText(insertemp_1);
                    Log.d("shenme","sbsbsssbbsb");
            }
            else{
                workset[i].setText("");
            }

        }

        butten_gone();


        fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_edit(fab,"","","",0);
            }
        });


        bottomNavigationView = findViewById(R.id.navigation_2);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setResult(3);
                        finish();
                        overridePendingTransition(0,0);
                        break;
                    case R.id.navigation_dashboard:
                        break;
                }
                transaction.commit();
                return false;
            }

        });




    }

    public void alert_edit(View view, String work_before, String begin_before, String end_before, final Integer index){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.time_dia, null);
        final EditText work = textEntryView.findViewById(R.id.work_input);

        final TimePicker timePicker=textEntryView.findViewById(R.id.timepicker_start);
        timePicker.setIs24HourView(true);
        final TimePicker timePicker_2=textEntryView.findViewById(R.id.timepicker_end);
        timePicker_2.setIs24HourView(true);


        new AlertDialog.Builder(this).setTitle("请输入事件")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(textEntryView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if(!index.equals(0)){
                            delete_time(index);
                        }
                        hour_start=timePicker.getHour();
                        min_start=timePicker.getMinute();
                        hour_end=timePicker_2.getHour();
                        min_end=timePicker_2.getMinute();

                        DecimalFormat df=new DecimalFormat("00");

                        String getwork=work.getText().toString();
                        String getbegin=df.format(hour_start)+":"+df.format(min_start);
                        String getend=df.format(hour_end)+":"+df.format(min_end);

                        if(!getwork.equals("")) {
                            input_sort(getwork, getbegin, getend);
                        }
                        else{
                            if(!index.equals(0)) {
                                workset[index].setText("");
                                workset[index].setVisibility(View.GONE);
                            }
                            else {
                                Toast toast=Toast.makeText(Edittime.this,"事件不能为空",Toast.LENGTH_SHORT    );
                                toast.show();
                            }
                        }
                        }
                }).setNegativeButton("取消",null).show();
        work.setText(work_before);
        if(!index.equals(0)) {
            timePicker.setHour(Integer.parseInt(begin_before.substring(0,begin_before.indexOf(":"))));
            timePicker.setMinute(Integer.parseInt(begin_before.substring(begin_before.indexOf(":")+1)));
            timePicker_2.setHour(Integer.parseInt(end_before.substring(0,end_before.indexOf(":"))));
            timePicker_2.setMinute(Integer.parseInt(end_before.substring(end_before.indexOf(":")+1)));
        }
    }

    public void input_sort(String getwork,String getbegin,String getend){
        SharedPreferences share = getSharedPreferences("mytime",MODE_PRIVATE);
        SharedPreferences .Editor edt=share.edit();
        Map<String, Pair<String,String>> WorkMap=new TreeMap();
        Integer index=1;
        boolean NeedSort=true;
        for(int i=1;i<Button_num;i++){
            get_str(i,share);
            if (!begin_str.equals("")&&!end_str.equals("")&&!work_str.equals("")){
            WorkMap.put(begin_str,new Pair<String, String>(work_str,end_str));
            index++;
            }
        }

        if(index<9){
            WorkMap.put(getbegin,new Pair<String, String>(getwork,getend));
            Log.d("input","ckecked");
            Toast toast=Toast.makeText(Edittime.this,"修改成功",Toast.LENGTH_SHORT    );
            toast.show();
        }
        else {
            Toast toast=Toast.makeText(Edittime.this,"事件总数最大值为8",Toast.LENGTH_SHORT    );
            toast.show();
            NeedSort=false;
        }

        index=1;
        if(NeedSort){
            for(Map.Entry<String, Pair<String,String>> entry: WorkMap.entrySet()){
                work_key="work"+index;
                begin_key="begin_"+index;
                end_key="end_"+index;
                edt.putString(work_key,entry.getValue().first);
                edt.putString(begin_key,entry.getKey());
                edt.putString(end_key,entry.getValue().second);
                Log.d("sort","ckecked");
                Log.d("sort",entry.getKey());
                Log.d("sort",entry.getValue().first);
                index++;
            }
        }
        edt.commit();

        for(int i=1;i<Button_num;i++){
            get_str(i,share);
            if(!work_str.equals("")&&!begin_str.equals("")&&!end_str.equals("")) {
                information =begin_str + "  ～  " + end_str+"\n"+work_str+"\n";
                insertemp_1=SpannableString.valueOf(information);
                insertemp_1.setSpan(sizeSpan1, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                insertemp_1.setSpan(LightSeaGreen, 0, information.indexOf("\n")+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                workset[i].setText(insertemp_1);
            }
            else{
                workset[i].setText("");
            }
        }
        butten_gone();
    }

    public void editting(View view,Integer index){
        SharedPreferences share = getSharedPreferences("mytime",MODE_PRIVATE);
        get_str(index,share);
        alert_edit(view,work_str,begin_str,end_str,index);
    }

    public void butten_gone(){
        for(int i=1;i<Button_num;i++){
            if(workset[i].getText().toString().equals("")){
                workset[i].setVisibility(View.GONE);
            }
            else{
                workset[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void get_str(Integer index,SharedPreferences share){
        work_key="work"+index;
        begin_key="begin_"+index;
        end_key="end_"+index;
        work_str = share.getString(work_key, "");
        begin_str = share.getString(begin_key, "");
        end_str = share.getString(end_key, "");
    }

    public void delete_time(Integer index){
        SharedPreferences share = getSharedPreferences("mytime",MODE_PRIVATE);
        SharedPreferences .Editor edt=share.edit();
        work_key="work"+index;
        begin_key="begin_"+index;
        end_key="end_"+index;
        edt.putString(work_key,"");
        edt.putString(begin_key,"");
        edt.putString(end_key,"");
        edt.commit();
        Log.d("delete:", "delete checked!");
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.work_1:
                    editting(workset[1],1);
                    break;
                case R.id.work_2:
                    editting(workset[2],2);
                    break;
                case R.id.work_3:
                    editting(workset[3],3);
                    break;
                case R.id.work_4:
                    editting(workset[4],4);
                    break;
                case R.id.work_5:
                    editting(workset[5],5);
                    break;
                case R.id.work_6:
                    editting(workset[6],6);
                    break;
                case R.id.work_7:
                    editting(workset[7],7);
                    break;
                case R.id.work_8:
                    editting(workset[8],8);
                    break;
                default:
                    break;
            }
        }
    }

    private class OnLongClick implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.work_1:
                    del_edit(workset[1],1);
                    break;
                case R.id.work_2:
                    del_edit(workset[2],2);
                    break;
                case R.id.work_3:
                    del_edit(workset[3],3);
                    break;
                case R.id.work_4:
                    del_edit(workset[4],4);
                    break;
                case R.id.work_5:
                    del_edit(workset[5],5);
                    break;
                case R.id.work_6:
                    del_edit(workset[6],6);
                    break;
                case R.id.work_7:
                    del_edit(workset[7],7);
                    break;
                case R.id.work_8:
                    del_edit(workset[8],8);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public void del_edit(View view,final Integer index){
        new AlertDialog.Builder(this).setTitle("确定要删除？")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_time(index);
                        workset[index].setVisibility(View.GONE);
                    }
                }).setNegativeButton("取消",null).show();
    }

    public  void ShowDate_init(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtf_2=DateTimeFormatter.ofPattern("EE HH:mm", Locale.CHINA);
        LocalDateTime Rightnow=LocalDateTime.now();
        String now=dtf.format(Rightnow);
        String DayOW=dtf_2.format(Rightnow).substring(0,2)+"\n"+Rightnow.getDayOfYear()+"天";
        ShowDate.setText(now);
        ShowDate_2.setText(DayOW);


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


