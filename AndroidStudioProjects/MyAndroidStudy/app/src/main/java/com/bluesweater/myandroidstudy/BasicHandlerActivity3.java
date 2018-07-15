package com.bluesweater.myandroidstudy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

/*
    03. 핸들러 쓰레드(백그라운드) 와 UI 간의 양방향통신

    다소 좋은 예제는 아님
    UI에 의해 이벤트가 발생하고 백스레드에서 작업동작후 UI에게 결과 전달의 과정을 보여준다
 */
public class BasicHandlerActivity3 extends AppCompatActivity {

    private final static int SHOW_PROGRESS_BAR = 1;
    private final static int HIDE_PROGRESS_BAR = 0;
    private BackThread backThread;

    private TextView textView;
    private Button btnClick;
    private ProgressBar progressBar;

    public final Handler uiHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_PROGRESS_BAR :
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case HIDE_PROGRESS_BAR :
                    progressBar.setVisibility(View.GONE);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_handler3);


        backThread = new BackThread();
        backThread.start();


        textView = findViewById(R.id.text3);
        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                backThread.doWork();
            }
        });
        progressBar = findViewById(R.id.pb3);


    }

    private class BackThread extends Thread{

        private Handler backGroundHandler;

        @Override
        public void run() {
            Looper.prepare();
            backGroundHandler = new Handler();
            Looper.loop();
        }

        public void doWork(){

            backGroundHandler.post(new Runnable() {
                @Override
                public void run() {
                        Message uiMsg = uiHandler.obtainMessage(SHOW_PROGRESS_BAR, 0, 0, null);
                        uiHandler.sendMessage(uiMsg);

                    Random r = new Random();
                    int randomInt = r.nextInt(5000);
                    SystemClock.sleep(randomInt);

                    uiMsg = uiHandler.obtainMessage(HIDE_PROGRESS_BAR, 0, 0, null);
                    uiHandler.sendMessage(uiMsg);
                }
            });


        }

        public void BackThreadExit(){
            backGroundHandler.getLooper().quit();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        backThread.BackThreadExit();


    }
}
