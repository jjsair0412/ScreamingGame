package com.halla.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    private static MediaPlayer mp;


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        mAdView = findViewById(R.id.banner01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mp = MediaPlayer.create(this,R.raw.wait);
        mp.setLooping(true);
        mp.start();

        // 해당 권한이 부여되어 있는지 확인하는 메소드
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "오디오 권한 주어져 있음.", Toast.LENGTH_SHORT).show();
        } else { // PackageManager.PERMISSION_DENIED
            Toast.makeText(this, "오디오 권한 없음.", Toast.LENGTH_SHORT).show();

            // 해당 권한에 대한 설명이 필요한 경우.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "SMS 권한 설명 필요함.", Toast.LENGTH_SHORT).show();
            }
            // 시스템이 권한 요청 대화상자를 띄움.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        }


        ImageButton endbtn = (ImageButton) findViewById(R.id.endbtn); // 종료
        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });



        ImageButton startbtn = (ImageButton) findViewById(R.id.startbtn); // 시작해서 게임화면으로 넘어가는 인텐트
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent1);


            }
        });



        ImageButton scorebtn = (ImageButton) findViewById(R.id.scorebtn);
        scorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), gamemode1.class); //gmaemode1은 스코어가 띄워져있는 화면이다
                startActivity(intent2);
            }
        });



        ImageButton howplay =(ImageButton) findViewById(R.id.howtoplay); // 게임 도움말 화면입니다
        howplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(),Main2Activity.class); // 메인2엑티비티는 도움말 화면입니다
                startActivity(intent3);
            }
        });


        ImageButton apibtn =(ImageButton) findViewById(R.id.apibtn);
        apibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent4);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "오디오 권한을 사용자가 허용함.", Toast.LENGTH_SHORT).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "오디오 권한을 사용자가 거부함.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "오디오 권한을 부여받지 못함.", Toast.LENGTH_SHORT).show();
                }
        }
    }


}

