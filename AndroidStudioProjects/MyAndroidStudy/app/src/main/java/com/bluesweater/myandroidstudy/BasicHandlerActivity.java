package com.bluesweater.myandroidstudy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
    01. 핸들러의 기본적인 사용
    - Handler
    - 핸들러와 루퍼와 메세지큐는 한셋트다.
    - 핸들러 생성시(new키워드) 그 핸들러는 자신을 생성한 스레드의 핸들러가 된다.
    - 따로 명시하지 않는다면 암시적으로 그 핸들러는 그 쓰레드의 루퍼와 큐에 연결된다.
    - 명시적으로 연결도 가능하다. ( ex : new Handler(Looper) )
    - 핸들러는 생산자이자. 소비자 이다.


    - Looper
    - 기본적으로 MAIN스레드(UI)는 루퍼를 처음부터 가지고 있다.
    - 쓰레드는 1루퍼와 1큐를 가진다.
    - quit등에 의해 끝난 looper는 재사용이 불가하다. (Exception발생)
    - MAIN루퍼가 다른 루퍼의 차이점 :
        1Looper.getMainLooper() 메서드를 이용해 전역적인 접근이 가능하다.
        2종료시킬수 없으며, quit는 소용없다(Exception 발생)
        3내부적으로 런타임은 Looper.prepareMainLooper()로 UI쓰레드에 연결한다.
         (이는 응용프로그램당 단 한번만 수행된다.)


    * 1.handler[메세지전달] -> queue -> looper(루퍼가돌면서 메세지를 빼옴) -> handler(callback)에게 전달 및 실행
    * 2.handler[task전달](runnable) -> queue(task자체가 메세지로 래핑됨) -> task 자체의 로직 실행

 */
public class BasicHandlerActivity extends AppCompatActivity {

    private LooperThread mlooperThread;
    private Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_handler);

        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //루퍼 쓰레드에게 (핸들러를 통해)임무 전달
                //그러면 그 핸들러가 자신의 큐에 메세지를 넣고 그 루퍼가 메세지를 꺼내 실행한다
                mlooperThread.mhandler.obtainMessage();
                mlooperThread.mhandler.sendEmptyMessage(0);
            }
        });

        //루퍼스레드 동작
        mlooperThread = new LooperThread();
        mlooperThread.start();
    }


    //static의 이유는 메모리 누수를 막기 위함이다.
    private static class LooperThread extends Thread{
        public Handler mhandler;

        @Override
        public void run() {
            Looper.prepare(); //핸들러와 루퍼 큐의 연결
            mhandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0){
                        doLongRunningOperation();
                    }
                }
            };

            Looper.loop(); //루퍼 돌기 시작 (run이 끝나지 않음을 보장하는 차단적 메서드)
        }


        private void doLongRunningOperation(){

            //실제 작업
            Log.i("K_TAG","긴 작업을 시작합니다...");


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlooperThread.mhandler.getLooper().quit();


    }
}
