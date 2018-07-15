package com.bluesweater.myandroidstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnBasicHandler;
    private Button btnBasicHandler2;
    private Button btnBasicHandler3;
    private Button btnThreadLifeCycle;
    private Button btnThreadLifeCycle2;
    private Button btnHandlerThread;
    private Button btnHandlerThread2;
    private Button btnAsyncActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //01. 기본적인 핸들러의 사용
        btnBasicHandler = findViewById(R.id.btnBasicHandler);
        btnBasicHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicHandlerActivity.class);
                startActivity(intent);
            }
        });

        //02. 핸들러, 루퍼, 큐의 관계
        btnBasicHandler2 = findViewById(R.id.btnBasicHandler2);
        btnBasicHandler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicHandlerActivity2.class);
                startActivity(intent);
            }
        });

        //03. 핸들러의 양방향 통신 활용
        btnBasicHandler3 = findViewById(R.id.btnBasicHandler3);
        btnBasicHandler3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicHandlerActivity3.class);
                startActivity(intent);
            }
        });

        btnThreadLifeCycle = findViewById(R.id.btnThreadLifeCycle);
        btnThreadLifeCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThreadLifeCycleActivity.class);
                startActivity(intent);
            }
        });

        btnHandlerThread = findViewById(R.id.btnHandlerThread);
        btnHandlerThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HandlerThreadActivity.class);
                startActivity(intent);
            }
        });

        btnHandlerThread2 = findViewById(R.id.btnHandlerThread2);
        btnHandlerThread2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChainedNetworkActivity.class);
                startActivity(intent);
            }
        });


        btnAsyncActivity = findViewById(R.id.btnAsyncTask);
        btnAsyncActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AsyncTaskActivity.class);
                startActivity(intent);
            }
        });


    }
}
