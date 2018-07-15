package com.bluesweater.myandroidstudy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;

/*
    02. 핸들러의 활용
    - 다수의 핸들러와 루퍼 큐의 관계

    다수의 핸들러가 동시에 메세지를 생성 전달 한다고 해도
    큐와 루퍼는 하나이므로 비동기적 실행은 불가능함.
    큐에 정렬되는 룰(시간매개변수)에 따라서 루퍼는 가져가서 전달한다.





 */
public class BasicHandlerActivity2 extends AppCompatActivity {

    private Button btnClick;
    private BackThread backThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_handler2);

        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backThread.sendMsg();
            }
        });


        backThread = new BackThread("lesson2-BT");
        backThread.start();

    }


    private static class BackThread extends Thread{

        private String name = "";
        private Handler h1;
        private Handler h2;
        private Handler h3;

        private BackThread(String name){
            setName(name);
        }


        public void sendMsg(){
            h1.sendEmptyMessage(1);
            h2.sendEmptyMessage(1);
            h3.sendEmptyMessage(1);

            //1. 큐의 추적
            h1.dump(new LogPrinter(Log.DEBUG, "DUMP_TAG"), "");
            //2. 큐의 추적
            Looper.myLooper().setMessageLogging(new LogPrinter(Log.DEBUG, "LOOPER_TAG"));

        }

        @Override
        public void run() {
            Looper.prepare();
            h1 = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Log.i("K_TAG","11111");
                }
            };

            h2 = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Log.i("K_TAG","22222");
                }
            };

            h3 = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Log.i("K_TAG","33333");
                }
            };


            Looper.loop();

        }

        public void safeQuit(){
            Looper.myLooper().quit();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        backThread.safeQuit();

    }
}
