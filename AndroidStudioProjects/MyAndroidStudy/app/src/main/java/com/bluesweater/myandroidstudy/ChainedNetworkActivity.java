package com.bluesweater.myandroidstudy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

/*
    07. 핸들러 쓰레드의 사용 2

    - 태스크 연쇄작업의 예제
    - 1번작업이 종료되고 나서 다음 동작을 이어서 할지 종료 할지 판단
    - 두번째 태스크는 독립적으로 실행할수 없고 반드시 1번의 종료에 의해서 시작됨


 */
public class ChainedNetworkActivity extends AppCompatActivity {

    private static final int DIALOG_LOADING = 0;
    private static final int SHOW_LOADING = 1;
    private static final int DISMISS_LOADING = 2;

    Handler dialogHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_LOADING:
                    showDialog(DIALOG_LOADING);
                    break;

                case DISMISS_LOADING:
                    dismissDialog(DIALOG_LOADING);
                    break;


            }
        }
    };

    private class NetworkHandlerThread extends HandlerThread {

        private static final int STATE_A = 1;
        private static final int STATE_B = 2;
        private Handler mHandler;


        public NetworkHandlerThread(){
            super("NetworkHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case STATE_A:
                            dialogHandler.sendEmptyMessage(SHOW_LOADING);
                            String result = networkOperation1();
                            if(result != null){
                                sendMessage(obtainMessage(STATE_B, result));
                            }else{
                                dialogHandler.sendEmptyMessage(DISMISS_LOADING);
                            }
                            break;

                        case STATE_B:
                            networkOperation2((String)msg.obj);
                            dialogHandler.sendEmptyMessage(DISMISS_LOADING);
                            break;

                    }
                }
            };
            fetchDataFromNetwork();
        }

        private String networkOperation1(){
            SystemClock.sleep(2000);
            return "A string";
        }

        private void networkOperation2(String data){
            //네트워크로 데이터를 전달했다고 치고...
            SystemClock.sleep(2000);
        }


        /**
         * 공개적으로 노출된 네트워크 작업
         */
        public void fetchDataFromNetwork(){
            mHandler.sendEmptyMessage(STATE_A);

        }
    }

    private NetworkHandlerThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_handler);

        mThread = new NetworkHandlerThread();
        mThread.start();

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id){
            case DIALOG_LOADING:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Loading...");
                dialog = builder.create();
                break;

        }
        return dialog;
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
