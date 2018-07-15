package com.bluesweater.myandroidstudy;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
    플래그먼트 내에서 쓰레드를 생성하여
    쓰레드의 유지부분을 맡아서 처리한다.
    내부의 setRetainInstance(true); 메서드를 통하여 쉽게 설정이 가능하다.


 */
public class ThreadLifeCycleFragment extends android.app.Fragment {

    private ThreadLifeCyclyeFragmentActivity myActivity;
    private MyThread t;

    private class MyThread extends Thread{
        @Override
        public void run() {
            final String text = getTextFromNetwork();
            myActivity.setText(text);
        }


        private String getTextFromNetwork(){
            SystemClock.sleep(10000);
            return "네트워크 메세지";
        }


    }


    public ThreadLifeCycleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thread_life_cycle, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (ThreadLifeCyclyeFragmentActivity) context;
        Log.i("K_TAG","activity 정보 : " + myActivity.hashCode());
        if(t != null) {
            Log.i("K_TAG", "Thread 정보 : " + t.hashCode());
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }


    public void execute(){
        t = new MyThread();
        t.start();
        Log.i("K_TAG","Thread 정보 : " + t.hashCode());
    }

}
