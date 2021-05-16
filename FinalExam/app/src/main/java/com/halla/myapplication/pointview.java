package com.halla.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class pointview extends AppCompatActivity {

    static String sharpoint = null;
    private Intent intent;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointview);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.banner01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        TextView pointcul =(TextView) findViewById(R.id.pointcul);

        try {
            intent = getIntent();
            sharpoint = null;
            sharpoint = intent.getStringExtra("extpoint");


            pointcul.setText(sharpoint+" 점");
        }catch (Exception e){
            Toast.makeText(this,"점수공유 오류",Toast.LENGTH_SHORT).show();
        }

    }

    public void onSave(View view){

        Intent intent =new Intent(pointview.this,gamemode1.class); // 점수판 클래스인 gamemode1로 넘어감
        intent.putExtra("extpoint2",sharpoint); // 받아온 sharpoint 점수를 extpoint2에 넣어놓고 점수판 클래스로 던져줌
        startActivity(intent);
        finish();
    }


}


