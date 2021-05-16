package com.halla.myapplication;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Timer;
import java.util.TimerTask;


public class SubActivity extends AppCompatActivity {


    private MediaRecorder recorder = null;//new MediaRecorder();
    private double frequency = 0.0; // 주파수 저장공간
    private TextView txt; // 사용자 최고 주파수 출력공간
    private TextView monsterhe; // 몬스터 채력 주파수 출력칸
    private TextView aaaa; // 점수 출력공간
    private String point2;
    final int[] count = {0}; // 타이머가 돌면서 1씩 증가시킬 변수.. 랜덤 몬스터를 출력할때 사용
    private TextView timeview;
    private ImageView monsterimage;
    private AdView mAdView;

    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemode);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.banner01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final int[] point = {0}; // 점수 저장 공간입니다
        timer = new Timer();

        txt = (TextView) findViewById(R.id.txt);
        aaaa = (TextView) findViewById(R.id.aaaa);

        final ImageButton monster = (ImageButton) findViewById(R.id.monsterbtn); // 이 버튼 누를시 몬스터 출몰하게 됨
        monster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monster.setEnabled(false);

                monsterimage = (ImageView) findViewById(R.id.monster); //몬스터 사진 나오는곳
                monsterhe = (TextView) findViewById(R.id.monsterhe); //몬스터 채력 출력칸


                timeview = (TextView)findViewById(R.id.timeview);

                CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        new frequencymain().start(); //주파수 스레드 실행
                        txt.setText(String.valueOf(frequency)); // 녹음중인 음성중 가장 높은 대역폭을 가지고왔다

                        count[0]++;
                        timeview.setText("경과 시간 : " + count[0]);

                        if(count[0] <=10){
                            monsterimage.setImageResource(R.drawable.finalmon1);
                            monsterhe.setText("20000.0"); //1번몬스터 채력

                            if (frequency >= 20000.0) {
                                point[0]++;
                                monsterimage.setImageResource(R.drawable.firstmonext);
                                aaaa.setText(point[0] +"점");
                            }
                        }else if(count[0] >10 && count[0] <=20){
                            monsterimage.setImageResource(R.drawable.finalmon2);
                            monsterhe.setText("30000.0"); //2번몬스터 채력 30000.0

                            if (frequency >= 30000.0) {
                                point[0]++;
                                monsterimage.setImageResource(R.drawable.secondmonext);
                                aaaa.setText(point[0] +"점");
                            }
                        }else if(count[0] >20 && count[0] <=30){
                            monsterimage.setImageResource(R.drawable.finalmon3);
                            monsterhe.setText("35000.0"); //3번몬스터 채력 35000.0

                            if (frequency >= 35000.0) {
                                point[0]++;
                                monsterimage.setImageResource(R.drawable.thirdmonext);
                                aaaa.setText(point[0] +"점");
                            }
                        }
                        point2 = Integer.toString(point[0]);
                    }

                    @Override
                    public void onFinish() { //30초가 지났을 경우

                        count[0] =0;
                        timeview.setText("last count=" + count[0]);
                        monsterimage.setImageResource(R.drawable.back2); //초기 몬스터화면으로 변경

                        recorder.stop(); //녹음 멈춤
                        recorder.release();
                        recorder = null;


                        Intent intent =new Intent(SubActivity.this,pointview.class); // 점수확인 클래스인 pointview로 자동으로 넘어감
                        intent.putExtra("extpoint",point2);
                        startActivity(intent);

                        try {
                            Thread.sleep(2000);
                        } catch(Exception e){}

                        finish(); // subActivity 죽여줌

                    }
                }.start();


            }
        });



    }


    class frequencymain extends  Thread{

        int value;

        public void run(){
            try {
                if (recorder == null) {

                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setOutputFile("/dev/null"); // dev/null 이렇게하면 녹음된 파일을 저장하지 않고도 쓸 수 있다

                    try {
                        recorder.prepare(); // 내용을 캡쳐하고 인코딩을 시작하는 레코더를 준비한다.
                        recorder.start(); //녹음시작

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SubActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if ( recorder != null) {
                                            value = recorder.getMaxAmplitude();
                                            frequency = value;
                                            if (value > frequency) {
                                                frequency = value;
                                            }
                                        }
                                    }
                                });
                            }
                        },1000,1000);

                        Button stop = (Button) findViewById(R.id.stop);  // stop버튼을 눌러주면 녹음을 멈춘다..
                        stop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recorder.stop();
                                recorder.release();
                                recorder = null;

                            }
                        });

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) { }

        }
    }

}

