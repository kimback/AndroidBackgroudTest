package com.bluesweater.myandroidstudy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
   06. 핸들러 쓰레드의 사용

   - 핸들러 쓰레드는 자체적으로 쓰레드를 가지며, 동시에 핸들러와 큐 루퍼등을 가진다.
   - 백쓰레드를 따로 만들어서 핸들러를 설정할 필요없이 편리하게 사용가능
   - 기본적으로 [순차 접근방식] 때문에 쓰레드 동기화가 필요없다
   - 이번 예제는 SharedPreferences 데이터를 읽고 쓸때 안전한 쓰레드 접근을 통해 사용이 가능하다.


 */
public class HandlerThreadActivity extends AppCompatActivity {
    private TextView mTextValue;
    private Button btnRead;
    private Button btnWrite;

    /**
     * 읽은 값을 TextView에 표시한다.
     */
    private Handler mUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Integer i = (Integer) msg.obj;
                mTextValue.setText(Integer.toString(i));
            }
        }
    };


    private class SharedPreferenceThread extends HandlerThread{

        private static final String KEY = "key";
        private SharedPreferences mPrefs;
        private static final int READ = 1;
        private static final int WRITE = 2;

        private Handler mHandler;


        public SharedPreferenceThread(){
            super("SharedPreferenceThread", Process.THREAD_PRIORITY_BACKGROUND);
            mPrefs = getSharedPreferences("LocalPrefs", MODE_PRIVATE);

        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    switch(msg.what){
                        case READ:
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(0,
                                    mPrefs.getInt(KEY, 0)));
                            break;
                        case WRITE:
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putInt(KEY, (Integer)msg.obj);
                            editor.commit();
                            break;

                    }

                }
            };
        }

        public void read(){
            mHandler.sendEmptyMessage(READ);
        }

        public void write(int i){
            mHandler.sendMessage(Message.obtain(mHandler, WRITE, i));
        }


    }


    private int mCount;
    private SharedPreferenceThread mThread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        mTextValue = findViewById(R.id.text06);
        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onButtonClickRead(view);
            }
        });
        btnWrite = findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onButtonClickWrite(view);
            }
        });

        mThread = new SharedPreferenceThread();
        mThread.start();

    }

    /**
     * ui스레드에서 더미값을 기록한다
     * @param v
     */
    public void onButtonClickWrite(View v){
        mThread.write(mCount++);
    }

    /**
     * ui스레드에서 읽기를 시작한다
     * @param v
     */
    public void onButtonClickRead(View v){
        mThread.read();
    }

    /**
     * 액티비티와 함께 백그라운드 스레드가 종료됨을 보장한다.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThread.quit();
    }
}
