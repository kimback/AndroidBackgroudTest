package com.bluesweater.myandroidstudy;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    05. 쓰레드 라이프 사이클 유지 (플래그먼트편)

    역시나 액티비티내의 설정변경등의 이유로 쓰레드와의 메세지 통신이
    잘 이루어지지 않는 문제가 생길수 있다.
    더미 플래그먼트를 셋팅하고 그안에서 쓰레드를 생성 , 실행 한다.
    유지에 관련한 부분은 플래그먼트에 위임한다.


 */
public class ThreadLifeCyclyeFragmentActivity extends AppCompatActivity {
    private ThreadLifeCycleFragment mThreadFragment;
    private TextView mTextView;
    private Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_life_cyclye_fragment);

        mTextView = findViewById(R.id.text5);
        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onStartThread(view);
            }
        });

        FragmentManager manager = getFragmentManager();
        mThreadFragment = (ThreadLifeCycleFragment) manager.findFragmentByTag("threadfragment");
        if(mThreadFragment == null){
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            mThreadFragment = new ThreadLifeCycleFragment();
            transaction.add(mThreadFragment, "threadfragment");
            transaction.commit();
        }

    }

    public void onStartThread(View v){
        mThreadFragment.execute();
    }


    public void setText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(text);
            }
        });
    }


}
