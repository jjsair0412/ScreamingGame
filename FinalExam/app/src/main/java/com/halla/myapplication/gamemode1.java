package com.halla.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class gamemode1 extends AppCompatActivity {

    private Intent intent = getIntent(); //SharedPoint 점수 데이터 수신
    static String sharpoint2; // pointview 클래스에서 받아온 sharpoint 점수가 저장된 extpoint2를 받아온 변수 sharpoint2
    static SQLiteDatabase db;
    private AdView mAdView;
    static String ppoint;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode1);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.banner01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        dbHelper helper = new dbHelper(this);
        TextView shapoint = (TextView) findViewById(R.id.shapoint);

        try {
            db  = helper.getWritableDatabase();
        }catch (Exception e){
            Toast.makeText(gamemode1.this,"db 생성 오류",Toast.LENGTH_SHORT).show();
        }



        try {
            intent = getIntent();
            sharpoint2 = intent.getStringExtra("extpoint2"); // pointview 클래스의 extpoint2 이름으로 저장되어있던 점수값을 받아옴
            try{
                if(sharpoint2!=null){
                    db.execSQL("INSERT INTO 랭크 VALUES ('" + sharpoint2 + "');");
                    list();
                }else{
                    list();
                }

            }catch (Exception e){
                Toast.makeText(gamemode1.this, "리스트 실행오류", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(gamemode1.this, "점수 가져오기 오류", Toast.LENGTH_SHORT).show();
        }


        ImageButton backbtn1 = (ImageButton) findViewById(R.id.back); //메인화면으로 돌아감
        backbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Cursor cursor2 = db.rawQuery("SELECT 점수 FROM 랭크;",null);
        while (cursor2.moveToNext()){ //cursor2가 움직이면서
            count ++; // 한번 움직일때마다 count변수를 증가시킨다
        }
    if (count>=15){ //count변수가 15개 이상이면( 점수칼럼의 데이터개수가 15개 이상이면)
        db.execSQL("DELETE FROM 랭크"); //랭크 테이블의 모든 데이터를 삭제시킨다.
    }

    }



    public void list() {
        TextView shapoint = (TextView) findViewById(R.id.shapoint);
        Cursor cursor = db.rawQuery("SELECT 점수 FROM 랭크 ORDER BY 점수 DESC;",null);
        ppoint = " ";
        while (cursor.moveToNext()) {
            String n = cursor.getString(0);
            ppoint += n + " \n";
        }

        shapoint.setText(ppoint);
    }

}


class dbHelper extends SQLiteOpenHelper {
    public dbHelper(Context context) {
        super(context, "point.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE 랭크 ( 점수 TEXT );"); //point 속성가지고있는 테이블
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS 랭크");
        onCreate(db);
    }

}



