package com.halla.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.net.URL;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



public class Main2Activity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    String result = "";
    TextView tv;
    TextToSpeech tts;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.banner01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv = (TextView) this.findViewById(R.id.tv);


        Handler h = new Handler() {
            public void handleMessage(Message msg) {
                tv.setText(result);
            }
        };
        new WorkerThread(h).start();

        ImageButton homebtn =(ImageButton) findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/jjsair0412")); //암시적 인텐트를 사용하여 나의 블로그로 이동하게끔 하였다
                startActivity(eIntent);
            }
        });
        tts = new TextToSpeech(this, this);
    }



    protected void onDestroy(){ // 어플리케이션 종료 시 불린다.
        super.onDestroy();
        if(tts != null) tts.shutdown();
    }


    public void onClick2(View view){
        String result2 = result;
    if(result2.length() > 0){
        // 이미 말하고 있으면 기존 음성 합성을 정지시킨다.
        if(tts.isSpeaking()) tts.stop();
        // 음성 합성을 시작한다.
        tts.setSpeechRate(1.0f);
        tts.speak(result2,TextToSpeech.QUEUE_FLUSH,null);
        }
    }



    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            Locale locale = Locale.getDefault();
            if(tts.isLanguageAvailable(locale)>= TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(locale);
            else
                Toast.makeText(this,"지원하지 않는 언어 오류",Toast.LENGTH_LONG).show();
        }else if(status == TextToSpeech.ERROR){
            Toast.makeText(this,"음성 합성 오류",Toast.LENGTH_LONG).show();
        }
    }



    class WorkerThread extends Thread {
        Handler h;

        WorkerThread(Handler h) {
            this.h = h;
        }


        public void run() {
            try {
                URL url = new URL("https://rss.blog.naver.com/jjsair0412.xml");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList itemnodeList = doc.getElementsByTagName("item");
                for (int i = 0; i < itemnodeList.getLength(); i++) {

                    Node itemnode = itemnodeList.item(i);
                    NodeList childNodeList = itemnode.getChildNodes();
                    for (int j = 0; j < childNodeList.getLength(); j++) {
                        Node childNode = childNodeList.item(j);
                        if (childNode.getNodeName().equals("title"))
                            result += childNode.getFirstChild().getNodeValue() + "\n\n";
                        if (childNode.getNodeName().equals("description"))
                            result += childNode.getFirstChild().getNodeValue() + "\n\n\n";


                    }
                    h.sendMessage(new Message());
                }
            } catch (Exception e) {
                tv.setText("파싱에러: " + e.toString());
            }
        }

    }
    public void onClick(View view){
        finish();
    }
}



