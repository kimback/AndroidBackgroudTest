package com.bluesweater.myandroidstudy;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    04. 쓰레드 라이프 사이클 유지

    쓰레드의 생명주기는 구성요소와 같지 않다.
    액티비티에서 쓰레드가 동작시 액티비티가 설정변경된(회전 등) 후에 그 쓰레드객체를 사용하기위해
    유지를 위한 설정이 필요하다.

    1. activity 의 경우


 */
public class ThreadLifeCycleActivity extends AppCompatActivity {


    private static class MyThread extends Thread{

        private ThreadLifeCycleActivity myActivity;

        public MyThread(ThreadLifeCycleActivity activity){
            this.myActivity = activity;

        }


        private void attach(ThreadLifeCycleActivity activity){
            this.myActivity = activity;
        }

        private String getTextFromNetwork(){
            Log.i("K_TAG","THREAD안에서 ACTIVITY 확인 : " + myActivity.hashCode());
            SystemClock.sleep(10000);
            return "네트워크에서온메세지";
        }

        @Override
        public void run() {
            final String text = getTextFromNetwork();
            myActivity.setText(text);
        }
    }

    private static MyThread t;
    private TextView text4;
    private Button btnClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_life_cycle);


        // 마지막에 설정된 객체를 가져온다
        Object retainedObject = getLastNonConfigurationInstance();
        if(retainedObject != null){
            t = (MyThread) retainedObject;
            t.attach(this);

            Log.i("K_TAG","ACTIVITY에서 THREAD의 정보 : " + t.hashCode());
        }


        text4 = findViewById(R.id.text4);
        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t == null || !t.isAlive()) {
                    t = new MyThread(ThreadLifeCycleActivity.this);
                    t.start();
                }
                Log.i("K_TAG","생성시 THREAD의 정보 : " + t.hashCode());

            }
        });

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() { //설정변경이 일어나기전에 쓰레드를 등록

        if(t != null && t.isAlive()){
            return t;
        }

        return null;
    }


    public void setText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text4.setText(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
